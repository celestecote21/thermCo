package com.celeste.thermco

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.celeste.thermco.Services.ContactServ.sendToServer
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.activity_define_t.*
import org.json.JSONObject
import java.lang.reflect.Method

class defineT : AppCompatActivity() {


    var type = 0
    var day= Array(7, {i -> false})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_define_t)
        type = intent.getIntExtra(EXTRA_SELECTOR, 0)

        sendBtn.setOnClickListener {

            if(degre_field.text.isEmpty() || hours_field.text.isEmpty() || duree_field.text.isEmpty() ){
                Toast.makeText(this, "Tout remplir", Toast.LENGTH_LONG).show()
            }else{
                val temporaire: Chauffage = Chauffage(
                    type,
                    day,
                    degre_field.text.toString().toFloat(),
                    hours_field.text.toString().toInt(),
                    duree_field.text.toString().toInt())
                //println(temporaire.toJSON().toString())
                val sharedPref = this.getSharedPreferences(getString(R.string.saved_server_key), Context.MODE_PRIVATE)

                val adresseServer = sharedPref.getString(getString(R.string.saved_server_key), "http://192.168.0.4:300/V1/appData")

                if(adresseServer != null) {
                    sendToServer(adresseServer, this,temporaire.toJSON() )
                }else {
                    println("adresse server non defini")
                }


            }

        }

    }

    fun onLundiClicked(view: View){
        if(day[0]){
            day[0] = false
        }else{
            day[0] = true

        }


    }
    fun onMardiClicked(view: View){
        if(day[1]){
            day[1] = false
        }else{
            day[1] = true
        }
    }
    fun onMercrediClicked(view: View){
        if(day[2]){
            day[2] = false
        }else{
            day[2] = true
        }

    }
    fun onJeudiClicked(view: View){
        if(day[3]){
            day[3] = false
        }else{
            day[3] = true
        }

    }
    fun onVendrediClicked(view: View){
        if(day[4]){
            day[4] = false
        }else{
            day[4] = true
        }

    }
    fun onSamediClicked(view: View){
        if(day[5]){
            day[5] = false
        }else{
            day[5] = true
        }

    }
    fun onDimancheClicked(view: View){
        if(day[6]){
            day[6] = false
        }else{
            day[6] = true
        }

    }

    /*fun sendToServer(weekJSONObject: JSONObject, context: Context,  get: Boolean): String{


        val sharedPref = context.getSharedPreferences(getString(R.string.saved_server_key), Context.MODE_PRIVATE) ?: return "pas ok"

        val adresseServer = sharedPref.getString(getString(R.string.saved_server_key), "http://192.168.0.4:300/V1/appData")
        println(adresseServer)
        var servRes = ""

        if (get){

            val registerRequest = object: StringRequest(Method.GET, adresseServer, Response.Listener { responce ->
                println(responce)
                servRes = responce
                //complet(true)
            },
                Response.ErrorListener {erreur ->
                    Log.d("ERROR", "couldn't register: $erreur")
                    //complet(false)
                    servRes = " probleme mais normal"
                })
            {}
            Volley.newRequestQueue(context).add(registerRequest)
            //return servRes
        }else{
            val requestBody = weekJSONObject.toString()

            val registerRequest = object: StringRequest(Method.POST, adresseServer, Response.Listener { responce ->
                println(responce)

                //complet(true)
            },
                Response.ErrorListener {erreur ->
                    Log.d("ERROR", "couldn't register: $erreur")
                    //complet(false)
                })
            {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }
            Volley.newRequestQueue(context).add(registerRequest)
            //return servRes
        }


        return servRes

    }*/


}
