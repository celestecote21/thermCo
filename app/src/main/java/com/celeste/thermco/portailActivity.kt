package com.celeste.thermco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.celeste.thermco.Services.MQTTConnectionParams
import com.celeste.thermco.Services.MQTTmanager
import com.celeste.thermco.UIinterface.UIUpdaterInterface
import com.celeste.thermco.Utilities.Pref
import kotlinx.android.synthetic.main.activity_portail.*
import java.lang.Exception

class portailActivity : AppCompatActivity(), UIUpdaterInterface {


    private var mqttManager: MQTTmanager? = null
    private var mqttAddressServeur: String? = null


    override fun resetUIWithConnection(status: Boolean) {

    }

    override fun update(message: String, topic: String?) {
        println("un message $message")
        button_portail.text = message
    }

    override fun updateStatusViewWith(status: String) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portail)
        val prefs = Pref(this)
        try {


            mqttAddressServeur = prefs.serveurMqtt
            println(mqttAddressServeur)

            val mqttConnectioParams = MQTTConnectionParams(
                "telephoneAndroid",
                "tcp://$mqttAddressServeur:1883",
                prefs.topicPortail,
                prefs.usernameBroker,
                prefs.password
            )


            mqttManager = MQTTmanager(mqttConnectioParams, applicationContext, this)

            mqttManager?.connect() { connected ->
                if (connected) {
                    mqttManager?.subscribe(prefs.topicPortail)
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
        }

        button_portail.setOnClickListener{
            mqttManager?.publish("1", prefs.topicPortail)
        }

    }
}
