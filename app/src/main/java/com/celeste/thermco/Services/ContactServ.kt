package com.celeste.thermco.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import kotlin.system.measureTimeMillis

object  ContactServ{


    fun sendToServer(adress: String, context: Context, jsonObject: JSONObject,complet: (Boolean) -> Unit){
        val requestBody = jsonObject.toString()

        val registerRequest = object: StringRequest(
            Method.POST, adress, Response.Listener { responce ->
                println(responce)

                complet(true)
            },
            Response.ErrorListener { erreur ->
                Log.d("ERROR", "couldn't register: $erreur")
                complet(false)
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


    fun receiveTemperature(adress: String, context: Context, complet:(Boolean, Float) -> Unit){
        val registerRequest = object: StringRequest(Method.GET, adress, Response.Listener { responce ->

            val temperature = responce.toFloatOrNull()
            if(temperature != null)
                complet(true, responce.toFloat())
            else
                complet(false, 0.toFloat())

        },
            Response.ErrorListener {erreur ->
                Log.d("ERROR", "couldn't register: $erreur")
                complet(false, 0.toFloat())
            })
        {}
        Volley.newRequestQueue(context).add(registerRequest)


    }
}