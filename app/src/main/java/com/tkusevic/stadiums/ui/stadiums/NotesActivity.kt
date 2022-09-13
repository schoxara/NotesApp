package com.tkusevic.stadiums.ui.stadiums

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkusevic.stadiums.App
import com.tkusevic.stadiums.R
import com.tkusevic.stadiums.commons.constants.RESPONSE_OK_201
import com.tkusevic.stadiums.commons.extensions.onClick
import com.tkusevic.stadiums.data.model.request.CreateNoteRequest
import com.tkusevic.stadiums.ui.stadiums.pager.CustomPagerAdapter
import hr.ferit.matijavrabelj.corona.model.response.AddNoteResponse
import hr.ferit.matijavrabelj.corona.networking.BackendFactory
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.popup_window.view.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotesActivity : AppCompatActivity(), IMqttMessageListener {

    var popupWindow: PopupWindow = PopupWindow();
    lateinit var allNotesFragment: AllNotesFragment
    lateinit var myNotesFragment: MyNotesFragment
    private val mqttClient: MqttAndroidClient by lazy {
        App.mqttClient
    }

    private val interactor = BackendFactory.getNotesInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        initAdapter()
        initListeners()
        subscribeProcess()
    }

    private fun initAdapter() {
        allNotesFragment = AllNotesFragment();
        myNotesFragment = MyNotesFragment();
        val navigationPagerAdapter = CustomPagerAdapter(supportFragmentManager)
        navigationPagerAdapter.addFragment(allNotesFragment)
        navigationPagerAdapter.addFragment(myNotesFragment)

        viewPager.adapter = navigationPagerAdapter
        viewPager.offscreenPageLimit = 2

    }

    private fun initListeners() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_all_stadiums -> viewPager.currentItem = 0
                R.id.action_favorites -> viewPager.currentItem = 1
            }
            true
        }
        fab.onClick {
            showPopupAdd(bottomNavigation)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showPopupAdd(view: View?) {
        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
        popupWindow.contentView.backAdd.onClick {
            popupWindow.dismiss()

        }
        popupWindow.contentView.addNotes.onClick {
            val body = CreateNoteRequest(
                popupWindow.contentView.titleNotesAdd.text.toString(),
                popupWindow.contentView.descriptionNotesAdd.text.toString()
            )

            this.interactor.createNote(body, addNotesCallback())
        }
    }

    private fun addNotesCallback(): Callback<AddNoteResponse> = object :
        Callback<AddNoteResponse> {
        override fun onFailure(call: Call<AddNoteResponse>?, t: Throwable?) {
            // TODO
        }

        override fun onResponse(call: Call<AddNoteResponse>?, response: Response<AddNoteResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK_201 -> onSuccessfulRequest()
                    else -> { // TODO }
                    }
                }
            }
        }
    }

    private fun onSuccessfulRequest() {
        Toast.makeText(
            App.context, "Added Note Successfully",
            Toast.LENGTH_LONG
        ).show()

        popupWindow.dismiss()
    }

    private fun subscribeProcess() {
        connect(
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MqttClient", "Connection success")
                    subscribe();
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

    private fun subscribe() {
        if (mqttClient.isConnected) {
            mqttClient.subscribe(
                "notes",
                1, this)

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
        Log.d("MqttClient11", topic!!)
        Log.d("MqttClient22", String(message!!.payload))
        allNotesFragment.getStadiums()
        myNotesFragment.getNotes()
    }
}