package com.celeste.thermco.Utilities

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.appcompat.app.AppCompatActivity
import com.celeste.thermco.R

class Status(
    var start: Int,
    var stop: Int,
    var day: Int,
    var temp_set: Float
){
    override fun toString(): String{
        var final = ""
        final += "${start.toString()} -> ${stop.toString()} @ ${temp_set} "
        return final
    }
}