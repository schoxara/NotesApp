package com.tkusevic.stadiums

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.tkusevic.stadiums.commons.constants.MQTT_HOST
import com.tkusevic.stadiums.commons.constants.MQTT_PORT
import com.tkusevic.stadiums.commons.constants.PREFS_NAME
import org.eclipse.paho.android.service.MqttAndroidClient

class App : Application() {

    companion object {
        internal lateinit var prefs: SharedPreferences
        internal lateinit var context: Context
        internal lateinit var mqttClient: MqttAndroidClient
    }

    override fun onCreate() {
        val rnds = (0..10000).random()

        super.onCreate()
        context = this.applicationContext
        val url = "tcp://${MQTT_HOST}:${MQTT_PORT}"
        mqttClient = MqttAndroidClient(this.applicationContext, url,"mqttx_${rnds}")
        prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    }

}