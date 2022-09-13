package com.tkusevic.stadiums.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tkusevic.stadiums.App
import com.tkusevic.stadiums.preferences.PreferencesHelperImpl
import com.tkusevic.stadiums.ui.signIn.SignInActivity
import com.tkusevic.stadiums.ui.stadiums.NotesActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class SplashActivity : Activity(), SplashView {


    private val prefs: PreferencesHelperImpl by lazy {
        PreferencesHelperImpl(App.prefs)
    }

    private val mqttClient: MqttAndroidClient by lazy {
        App.mqttClient
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (prefs.userIdExists())
            this.startApp()
        else
            this.startSignIn()
    }


    override fun startApp() {
        startActivity(Intent(this, NotesActivity::class.java))
        finish()
    }

    override fun startSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }


    private fun publish(message: String) {
        if (mqttClient.isConnected)
            mqttClient.publish("notes", message.toByteArray(), 1, false)
        else
            Log.d("MqttClient", "MqttMessage is not published correctly")
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
}
