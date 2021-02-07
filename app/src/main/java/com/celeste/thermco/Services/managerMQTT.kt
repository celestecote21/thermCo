package com.celeste.thermco.Services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.celeste.thermco.UIinterface.UIUpdaterInterface
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.android.service.MqttAndroidClient

class MQTTmanager(val connectionParms: MQTTConnectionParams, val context: Context, var uiUpdater: UIUpdaterInterface){
    private val client = MqttAndroidClient(context, connectionParms.host, connectionParms.clientId)


    init {
        client.setCallback(object: MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.w("mqtt", "connection complet to $serverURI")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.w("mqtt", "the message is arrived")
                uiUpdater.update(message.toString(), topic)
//                lastMessage = message.toString()

            }

            override fun connectionLost(cause: Throwable?) {
                Log.w("mqtt", "the connection is lost")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.w("mqtt", "the delivry is complete ")
            }

        })
    }

    fun connect(complet:(Boolean) -> Unit){
        val mqttConnectOptions = MqttConnectOptions()
        
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false
        mqttConnectOptions.password = this.connectionParms.password.toCharArray()
        mqttConnectOptions.userName = this.connectionParms.username

        try {
            client.connect(mqttConnectOptions, context, object : IMqttActionListener {
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.w("connection", "no connection")
                    Log.w("mqtt", exception.toString())
                    complet(false)
                }

                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                    disconnectedBufferOptions.isDeleteOldestMessages = false
                    disconnectedBufferOptions.isPersistBuffer = false
                    disconnectedBufferOptions.isBufferEnabled = true
                    disconnectedBufferOptions.bufferSize = 100
                    client.setBufferOpts(disconnectedBufferOptions)
                    Log.w("connection", "success connection")
                    complet(true)
                }

            })
        }
        catch (ex:MqttException) {
            ex.printStackTrace()
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
            complet(false)
        }
    }


    fun disconnect(){

        try {


            client.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.w("mqtt", "disconnection succesful")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.w("mqtt", "unable to disconnect")
                }
            })
        }catch (ex:MqttException){
            ex.printStackTrace()
        }
    }


    fun publish(message: String, topic: String){
        try {
            client.publish(topic, message.toByteArray(), 0, false, null, object: IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.w("mqtt", "the message is delivered on the topic")
                    if(message != "ok") // que ca mette pas un dialogue pour le status
                        uiUpdater.mqttError("no", true)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.w("mqtt", "not delivered the message")
                    uiUpdater.mqttError(exception.toString(), false)
                }
            })
        }catch (ex:MqttException){
            ex.printStackTrace()
        }
    }

    fun subscribe(topic: String){
        Log.w("mqtt", "okai ca marche")
        try {
            client.subscribe(topic, 0, context, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.w("mqtt", "success subscribtiokn")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.w("mqtt", "uneable to subscribe")
                }
            })
        }catch (ex: MqttException){
            Log.w("mqtt", "gros probleme subscrition")
            ex.printStackTrace()
        }
    }



    // Unsubscribe the topic
    fun unsubscribe(topic: String){

        try
        {
            client.unsubscribe(topic,null,object :IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.w("mqtt", "success unsubscribtiokn")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.w("mqtt", "uneable to unsubscribe")
                }
            })
        }
        catch (ex:MqttException) {
            System.err.println("Exception unsubscribe")
            ex.printStackTrace()
        }
    }


    fun isConnected():Boolean{
        return client.isConnected
    }

}



data class MQTTConnectionParams(val clientId:String, val host: String, val topic: String, val username: String, val password: String){

}