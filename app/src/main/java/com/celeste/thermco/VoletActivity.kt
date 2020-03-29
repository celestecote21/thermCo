package com.celeste.thermco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.celeste.thermco.Services.MQTTConnectionParams
import com.celeste.thermco.Services.MQTTmanager
import com.celeste.thermco.UIinterface.UIUpdaterInterface
import com.celeste.thermco.Utilities.Pref
import kotlinx.android.synthetic.main.activity_volet.*
import java.lang.Exception

class VoletActivity : AppCompatActivity(), UIUpdaterInterface {


    private var mqttManager: MQTTmanager? = null
    private var mqttAddressServeur: String? = null

    override fun update(message: String, topic: String?) {
        if(message == "OK") {
                button_up.setBackgroundResource(R.drawable.button_check)
                button_down.setBackgroundResource(R.drawable.button_check)
        }else{
            if(message == "up")
                button_up.setBackgroundResource(R.drawable.button_activ)
            else if(message == "down")
                button_down.setBackgroundResource(R.drawable.button_activ)
        }
    }

    override fun mqttError(error: String, complete: Boolean) {
        val builder = AlertDialog.Builder(this)
        if(!complete) {
            builder.setTitle("erreur dans l'envoi")
            builder.setMessage("merci de verifier la connection")
            builder.setPositiveButton("OK") { _, _ -> }
            builder.create().show()
        }
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
                prefs.topicVolet,
                prefs.usernameBroker,
                prefs.password
            )


            mqttManager = MQTTmanager(mqttConnectioParams, applicationContext, this)

            mqttManager?.connect() { connected ->
                if (connected) {
                    mqttManager?.subscribe(prefs.topicVolet)
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this, "verifier l'addresse, l'user name et le password", Toast.LENGTH_LONG).show()
            onBackPressed()
        }

        button_up.setOnClickListener{
            mqttManager?.publish("up", prefs.topicVolet)
        }
        button_down.setOnClickListener{
            mqttManager?.publish("down", prefs.topicVolet)
        }

    }
}

