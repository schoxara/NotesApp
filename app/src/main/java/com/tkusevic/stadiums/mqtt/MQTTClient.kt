import android.content.Context
import android.util.Log
import android.widget.Toast
import com.tkusevic.stadiums.App
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MQTTClient(
    context: Context?,
    serverURI: String,
    clientID: String = ""
) {
    private var mqttClient = MqttAndroidClient(context, serverURI, clientID)

    fun subscribeProcess(listener: IMqttMessageListener, noteId: String?) {
        connect(
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MqttClient", "Connection success")
                    if (noteId != null) {
                        subscribe("notes/${noteId}", listener)
                    } else {
                        subscribe("notes", listener)
                    }
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

    private fun subscribe(noteId: String, listener: IMqttMessageListener) {
        if (mqttClient.isConnected) {
            this.mqttClient.subscribe(
                "notes/${noteId}",
                1, listener
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
            this.connect(options, null, cbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean {
        return this.mqttClient.isConnected
    }

    fun connect(
        options: MqttConnectOptions,
        userContext: Object?,
        callback: IMqttActionListener
    ): IMqttToken? {
        return this.mqttClient.connect(
            options, userContext, callback
        )
    }

    fun publish(
        topic: String,
        payload: ByteArray,
        qos: Int,
        retained: Boolean
    ) {
        this.mqttClient.publish(
            topic, payload, qos, retained
        )
    }

    fun setCallback(callback: MqttCallback) {
        this.mqttClient.setCallback(callback)
    }
}