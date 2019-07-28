package com.celeste.thermco.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.system.measureTimeMillis

object  ContactServ{


    fun sendToServer(adress: String, context: Context, jsonObject: JSONObject){
        val requestBody = jsonObject.toString()

        val registerRequest = object: StringRequest(
            Method.POST, adress, Response.Listener { responce ->
                println(responce)

                //complet(true)
            },
            Response.ErrorListener { erreur ->
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
    }


    fun receiveTemperature(adress: String, context: Context): Float{
        var servRes = ""
        var temperature: Float?
        val registerRequest = object: StringRequest(Method.GET, adress, Response.Listener { responce ->
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

        temperature = servRes.toFloatOrNull()

        if(temperature != null){
            return temperature
        }else{
            return 0.toFloat()
        }


    }
}