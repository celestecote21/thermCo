package com.celeste.thermco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.activity_define_t.*

class defineT : AppCompatActivity() {


    var type = 0
    var day= Array(7, {i -> false})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_define_t)
        type = intent.getIntExtra(EXTRA_SELECTOR, 0)

        sendBtn.setOnClickListener {

            if(degre_field.text.isEmpty() || hours_field.text.isEmpty() || duree_field.text.isEmpty() ){
                Toast.makeText(this, "Tout remplir", Toast.LENGTH_LONG).show()
            }else{
                val temporaire: Chauffage = Chauffage(
                    type,
                    day,
                    degre_field.text.toString().toFloat(),
                    hours_field.text.toString().toInt(),
                    duree_field.text.toString().toInt())
                println(temporaire.toJSON().toString())
            }






        }



    }

    fun onLundiClicked(view: View){
        if(day[0]){
            day[0] = false
        }else{
            day[0] = true

        }


    }
    fun onMardiClicked(view: View){
        if(day[1]){
            day[1] = false
        }else{
            day[1] = true
        }
    }
    fun onMercrediClicked(view: View){
        if(day[2]){
            day[2] = false
        }else{
            day[2] = true
        }

    }
    fun onJeudiClicked(view: View){
        if(day[3]){
            day[3] = false
        }else{
            day[3] = true
        }

    }
    fun onVendrediClicked(view: View){
        if(day[4]){
            day[4] = false
        }else{
            day[4] = true
        }

    }
    fun onSamediClicked(view: View){
        if(day[5]){
            day[5] = false
        }else{
            day[5] = true
        }

    }
    fun onDimancheClicked(view: View){
        if(day[6]){
            day[6] = false
        }else{
            day[6] = true
        }

    }


}
