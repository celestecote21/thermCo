package com.celeste.thermco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.celeste.thermco.Services.MQTTConnectionParams
import com.celeste.thermco.Services.MQTTmanager
import com.celeste.thermco.UIinterface.UIUpdaterInterface
import com.celeste.thermco.Utilities.Pref
import kotlinx.android.synthetic.main.activity_portail.*
import kotlinx.android.synthetic.main.activity_volet.*
import java.lang.Exception

class VoletActivity : AppCompatActivity(), UIUpdaterInterface {


    private var mqttManager: MQTTmanager? = null
    private var mqttAddressServeur: String? = null


    override fun resetUIWithConnection(status: Boolean) {

    }

    override fun update(message: String, topic: String?) {
        if(message == "1") {
            if(topic == "Maison/volet/up")
                button_up.setBackgroundResource(R.drawable.button_activ)
            else if(topic == "Maison/volet/down")
                button_down.setBackgroundResource(R.drawable.button_activ)

        }else{
            if(topic == "Maison/volet/up")
                button_up.setBackgroundResource(R.drawable.button_check)
            else if(topic == "Maison/volet/down")
                button_down.setBackgroundResource(R.drawable.button_check)
        }
    }

    override fun updateStatusViewWith(status: String) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_volet)
        val prefs = Pref(this)
        try {


            mqttAddressServeur = prefs.serveurMqtt


            val mqttConnectioParams = MQTTConnectionParams(
                "telephoneAndroid",
                "tcp://$mqttAddressServeur:1883",
                prefs.topicVoletUp,
                prefs.usernameBroker,
                prefs.password
            )


            mqttManager = MQTTmanager(mqttConnectioParams, applicationContext, this)

            mqttManager?.connect() { connected ->
                if (connected) {
                    mqttManager?.subscribe(prefs.topicVoletUp)
                    mqttManager?.subscribe(prefs.topicVoletDown)
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this, "verifier l'addresse, l'user name et le password", Toast.LENGTH_LONG).show()
            onBackPressed()
        }

        button_up.setOnClickListener{
            mqttManager?.publish("1", prefs.topicVoletUp)
        }
        button_down.setOnClickListener{
            mqttManager?.publish("1", prefs.topicVoletDown)
        }

    }
}

