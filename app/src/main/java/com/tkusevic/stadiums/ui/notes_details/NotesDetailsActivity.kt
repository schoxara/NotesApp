package com.tkusevic.stadiums.ui.notes_details

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkusevic.stadiums.App
import com.tkusevic.stadiums.R
import com.tkusevic.stadiums.commons.constants.NOTES_KEY
import com.tkusevic.stadiums.commons.constants.RESPONSE_CONFLICT
import com.tkusevic.stadiums.commons.constants.RESPONSE_OK
import com.tkusevic.stadiums.commons.extensions.hide
import com.tkusevic.stadiums.commons.extensions.onClick
import com.tkusevic.stadiums.commons.extensions.toast
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.data.model.request.UpdateNoteRequest
import com.tkusevic.stadiums.preferences.PreferencesHelperImpl
import hr.ferit.matijavrabelj.corona.networking.BackendFactory
import kotlinx.android.synthetic.main.activity_details.*
import org.eclipse.paho.client.mqttv3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class NotesDetailsActivity : AppCompatActivity(), DetailsView, IMqttMessageListener {

    private val mqttClient = App.mqttClient

    private val interactor = BackendFactory.getNotesInteractor()

    private var noteId: String = "";

    private val prefs: PreferencesHelperImpl by lazy {
        PreferencesHelperImpl(App.prefs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        getDetails()
        initListeners()
    }

    private fun initListeners() {
        saveDetails.onClick {
            interactor.updateNote(
                noteId,
                UpdateNoteRequest(
                    title = titleNotesDetails.text.toString(),
                    description = descriptionNotesDetails.text.toString()
                ),
                updateCallback()
            )
        }
        backDetails.onClick { finish() }
        detailsDelete.onClick {
            this.interactor.deleteNote(noteId, deleteCallback())
        }
    }

    private fun getDetails() {
        val intent = this.intent
        val bundle: Bundle? = intent.extras
        val note: Serializable? = bundle?.getSerializable(NOTES_KEY)
        showData(note as Notes)
        noteId = note.id

        subscribeProcess(note.id)
    }

    // view functions
    override fun showData(note: Notes) {
        titleNotesDetails.setText(String.format(note.title))
        authorDetails.text = String.format("Author: %s", note.user?.name)
        descriptionNotesDetails.setText(String.format(note.description))
        createdAtNotesDetails.text = String.format("CreatedAt: " + note.createdAt)

        if (prefs.getUserId() != note.user?.id) {
            saveDetails.hide()
            detailsDelete.hide()
        }
    }

    private fun subscribeProcess(noteId: String) {
        connect(
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MqttClient", "Connection success")
                    subscribe(noteId);
                    Toast.makeText(App.context, "MQTT Connection success", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("MqttClient", "Connection failure: ${exception.toString()}")

                    Toast.makeText(
                        App.context, "MQTT Connection fails: ${exception.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val msg = "Receive message: ${message.toString()} from topic: $topic"
                    Log.d("MqttClient", msg)
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.d("MqttClient", "Connection lost ${cause.toString()}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d("MqttClient", "Delivery complete")
                }
            })


    }

    private fun subscribe(noteId: String) {
        if (mqttClient.isConnected) {
            mqttClient.subscribe(
                "notes/${noteId}",
                1, this
            )

        } else
            Log.d("MqttClient", "MqttMessage is not subscribed correctly")
    }

    private fun connect(
        cbConnect: IMqttActionListener,
        cbClient: MqttCallback
    ) {
        mqttClient.setCallback(cbClient)
        val options = MqttConnectOptions()

        try {
            mqttClient.connect(options, null, cbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        message?.run {
            val stringedValue = String(this.payload)

            if (stringedValue == "Update") {
                interactor.getNotesByid(noteId, getNotesCallback())
            } else if (stringedValue == "Delete") {
                closeAndShowMessage()
            }
        }
    }

    private fun getNotesCallback(): Callback<Notes> = object :
        Callback<Notes> {
        override fun onFailure(call: Call<Notes>, t: Throwable?) {
            handleSomethingWentWrong()
        }

        override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> onItemsReceived(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun deleteCallback(): Callback<Notes> = object :
        Callback<Notes> {
        override fun onFailure(call: Call<Notes>, t: Throwable?) {
            handleSomethingWentWrong()
        }

        override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> closeAndShowMessage()
                    else -> handleSomethingWentWrong()
                }
            }
            when (response.code()) {
                RESPONSE_CONFLICT -> handleConflict(response)
            }
        }
    }

    private fun updateCallback(): Callback<Notes> = object :
        Callback<Notes> {
        override fun onFailure(call: Call<Notes>, t: Throwable?) {
            handleSomethingWentWrong()
        }

        override fun onResponse(call: Call<Notes>, response: Response<Notes>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> updatedMessage()
                    else -> handleSomethingWentWrong()
                }
            }
            when (response.code()) {
                RESPONSE_CONFLICT -> handleConflict(response)
            }
        }
    }

    private fun handleSomethingWentWrong() {
        this.toast("Error");
    }

    private fun onItemsReceived(note: Notes?) {
        note?.run {
            this@NotesDetailsActivity.showData(note)
            this@NotesDetailsActivity.toast("Refreshed!");
        }
    }

    private fun closeAndShowMessage() {
        this.toast("Note deleted!")
        finish()
    }

    private fun handleConflict(response: Response<Notes>) {
        this.toast(response.message().toString())
    }

    private fun updatedMessage() {
        this.toast("Updated!")
    }

}