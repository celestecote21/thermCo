package com.celeste.thermco.Utilities

import android.content.Context
import com.celeste.thermco.Utilities.*

class Pref(contex: Context){
    val PREF_FILE_NAME = "pref"
    val prefs = contex.getSharedPreferences(PREF_FILE_NAME, 0)


    var adresseServeur: String
        get()  {
            val tmp =  prefs.getString(SERVER_PREF, null)
            if (tmp != null)
                return tmp
            else
                return "http://192.168..0.4/rest/endpoint"

        }
        set(value) = prefs.edit().putString(SERVER_PREF, value).apply()


    var last_clim_temp: Float
        get() = prefs.getFloat(CLIM_PREF, 24.toFloat())
        set(value) = prefs.edit().putFloat(CLIM_PREF, value).apply()

    var last_geo_temp: Float
        get() = prefs.getFloat(GEQO_PREF, 24.toFloat())
        set(value) = prefs.edit().putFloat(GEQO_PREF, value).apply()

    var isTheFirstLogging: Boolean
        get() = prefs.getBoolean(FIRST_LOGIN, true)
        set(value) = prefs.edit().putBoolean(FIRST_LOGIN, value).apply()
}
