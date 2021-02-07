package com.celeste.thermco.Utilities

import android.content.Context
import androidx.preference.PreferenceManager
import com.celeste.thermco.Utilities.*

class Pref(contex: Context){
    val PREF_FILE_NAME = "pref"
    //val prefs = contex.getSharedPreferences(PREF_FILE_NAME, 0)
    val prefs = PreferenceManager.getDefaultSharedPreferences(contex)


    var adresseServeur: String
        get()  {
            val tmp =  prefs.getString(SERVER_PREF, null)
            return if (tmp != null)
                tmp
            else
                "http://192.168.0.4/rest/endpoint"

        }
        set(value) = prefs.edit().putString(SERVER_PREF, value).apply()


    var lastClimTemp: Float
        get() = prefs.getFloat(CLIM_PREF, 24.toFloat())
        set(value) = prefs.edit().putFloat(CLIM_PREF, value).apply()

    var lastGeoTemp: Float
        get() = prefs.getFloat(GEQO_PREF, 24.toFloat())
        set(value) = prefs.edit().putFloat(GEQO_PREF, value).apply()

    var isTheFirstLogging: Boolean
        get() = prefs.getBoolean(FIRST_LOGIN, true)
        set(value) = prefs.edit().putBoolean(FIRST_LOGIN, value).apply()

    var lastChaleur: Int
        get() = prefs.getInt(CHALEUR_PREF, 1)
        set(value) = prefs.edit().putInt(CHALEUR_PREF, value).apply()

    var defaultTempGeo: Float
        get() {
            val temp = prefs.getString("temperature_base_geothermie_pref", "15")
            return if(temp != null)
                        temp.toFloat()
                    else
                        15.toFloat()
        }
        set(value) {
            prefs.edit().putString("temperature_base_geothermie_pref", value.toString()).apply()
        }

    var serveurMqtt: String
        get() {
            val temp = prefs.getString(SERVUR_MQTT, "192.168.1.26")
            return if(temp != null)
                temp
            else
                "192.168.1.26"

        }
        set(value) = prefs.edit().putString(SERVUR_MQTT,value).apply()

    var topicPortail: String
        get(){
            val temp = prefs.getString("topic_portail_pref", "")
            return if(temp != null)
                temp
            else
                "Maison/test"

        }
        set(value) = prefs.edit().putString("topic_portail_pref", value).apply()

    var topicVolet: String
        get(){
            val temp = prefs.getString("topic_volet_pref", "")
            return if(temp != null)
                temp
            else
                "Maison/test"

        }
        set(value) = prefs.edit().putString("topic_volet_pref", value).apply()


    var topicThermostatSet: String
        get(){
            val temp = prefs.getString("topic_thermostat_set_pref", "")
            return if(temp != null)
                temp
            else
                "Maison/test"

        }
        set(value) = prefs.edit().putString("topic_thermostat_set_pref", value).apply()

    var topicThermostatStatus: String
        get(){
            val temp = prefs.getString("topic_thermostat_status_pref", "")
            return if(temp != null)
                temp
            else
                "Maison/test"
        }
        set(value) = prefs.edit().putString("topic_thermostat_status_pref", value).apply()

    var topicThermostatGet: String
        get(){
            val temp = prefs.getString("topic_thermostat_get_pref", "")
            return if(temp != null)
                temp
            else
                "Maison/test"

        }
        set(value) = prefs.edit().putString("topic_thermostat_get_pref", value).apply()



    var usernameBroker: String
        get(){
            val temp = prefs.getString("user_name_pref", "")
            return if(temp != null)
                temp
            else
                ""

        }
        set(value) = prefs.edit().putString("user_name_pref", value).apply()

    var password: String
        get(){
            val temp = prefs.getString("password_pref", "")
            return if(temp != null)
                temp
            else
                ""

        }
        set(value) = prefs.edit().putString("password_pref", value).apply()

    var deviceName: String
        get(){
            val temp = prefs.getString("device_name", "1234")
            return if(temp != null)
                temp
            else
                "1234"

        }
        set(value) = prefs.edit().putString("device_name", value).apply()
}
