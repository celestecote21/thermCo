package com.celeste.thermco

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.celeste.thermco.Utilities.Pref
import com.celeste.thermco.Utilities.SERVER_PREF
import kotlinx.android.synthetic.main.activity_setting.*


class Setting : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val pref = Pref(this)


        adress_setting_field3.setText(pref.adresseServeur)

        save_btn_setting.setOnClickListener {

            pref.adresseServeur = adress_setting_field3.text.toString()

        }

    }



}