package com.celeste.thermco

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONObject

class Setting : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val sharedPref1 = this.getSharedPreferences(getString(R.string.saved_server_key), Context.MODE_PRIVATE) ?: return

        var adresseServer = sharedPref1.getString(getString(R.string.saved_server_key), "http://192.168.0.4:300/V1/appData")

        adress_setting_field.setText(adresseServer)

        save_btn_setting.setOnClickListener {
            val sharedPref = this.getSharedPreferences(getString(R.string.saved_server_key),Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(getString(R.string.saved_server_key), adress_setting_field.text.toString())
                apply()
            }
        }

    }



}