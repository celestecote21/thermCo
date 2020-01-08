package com.celeste.thermco.models

import android.content.Context
import com.celeste.thermco.Utilities.Pref
import org.json.JSONObject
//______________FROID / CHAUD____JOUR ACTIVER____________TEMPERATURE ____________HEURE DEPART_______DUREE________GEO / CLIM

class Chauffage(
    var type: Int,
    var day: Array<Boolean>,
    var temperature: Float,
    var startTime: Int,
    var duree: Int,
    val choix: Int,
    val tempBaseGeo: Float
){


    fun toJSON(): JSONObject{

        // type c'est si c'est froid ou chaud

        val hours = Array(24)
            {j ->
                if((j + 1) >= startTime && (j + 1) <= startTime + duree){
                    if(type == 1){
                        "$temperature,"
                    }else if(type == 2){
                        "-$temperature,"
                    }else{
                        "0,"
                    }
                }
                else {
                    "0,"
                }
            }

            var hourStr = ""
            for(j in hours){
                hourStr += j
            }


        val weekJSON = JSONObject()

        var i = 0
        for(d in day ){
            if(d){
                weekJSON.put("day", i)
            }
            i++
        }

        weekJSON.put("hours", hourStr)
        weekJSON.put("type", choix) // si c'est la geo ou la clim
        if(choix == 1){
            weekJSON.put("default", tempBaseGeo.toString())
        }
        println(weekJSON.toString())


        return weekJSON
    }
}

