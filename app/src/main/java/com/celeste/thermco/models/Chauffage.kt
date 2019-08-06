package com.celeste.thermco.models

import org.json.JSONObject

class Chauffage(var type: Int, var day: Array<Boolean>, var temperature: Float, var startTime: Int, var duree: Int){


    fun toJSON(): JSONObject{

        val week = Array(7)
        {i -> Array(24)
            {j ->
                //println(i)
                if(day[i] == true){
                    if((j + 1) >= startTime && (j + 1) <= startTime + duree){
                        if(type == 1){
                            "$temperature,"
                        }else if(type == 2){
                            "-$temperature,"
                        }else{
                            "0,"
                        }
                    }
                    else{
                        "0,"
                    }
                }else{
                    "0,"
                }
            }
        }

        val weekStr = Array<String>(7){
            i ->
            var str = ""
            for(j in week[i]){
                str += j
            }
            str
        }

        println(weekStr[3])
        val weekJSON = JSONObject()

        weekJSON.put("lundi", weekStr[0])
        weekJSON.put("mardi", weekStr[1])
        weekJSON.put("mercredi", weekStr[2])
        weekJSON.put("jeudi", weekStr[3])
        weekJSON.put("vendredi", weekStr[4])
        weekJSON.put("samedi", weekStr[5])
        weekJSON.put("dimanche", weekStr[6])
        weekJSON.put("celeste","celeste")


        return weekJSON
    }
}

