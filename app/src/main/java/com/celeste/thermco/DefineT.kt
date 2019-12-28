package com.celeste.thermco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.celeste.thermco.Services.ContactServ.sendToServer
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.Utilities.Pref
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.activity_define_t.*


class DefineT : AppCompatActivity() {

    var adresseServer:String = ""
    private var type = 0
    private var day= Array(7){ i -> false}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = Pref(this)

        setContentView(R.layout.activity_define_t)
        type = intent.getIntExtra(EXTRA_SELECTOR, 0)

        if(type == 1){
            FC_button.isChecked = true
            FC_button.isClickable = false
        }else
            FC_button.isChecked = pref.lastChaleur == 1

        val spinner: Spinner = findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }



        fun getItemSelected(){
            val allDayArray = resources.getStringArray(R.array.planets_array)
            var i =  0
            for(days in allDayArray){
                if(days ==  spinner.getSelectedItem().toString()){
                    day[i] = true
                }
                i += 1
            }
        }



        sendBtn.setOnClickListener {
            getItemSelected()
            var chaleur = 1;

            if(FC_button.isChecked){
               chaleur = 1
            }else {
                chaleur = 2
            }
            pref.lastChaleur = chaleur
            if(chaleur == 2 && type == 1){
                chaleur = 1
            }
            //TODO:faire que les valeur par defaut sont celle de la derniere fois pour les boutona
            if(degre_field.text.isEmpty() || hours_field.text.isEmpty() || duree_field.text.isEmpty() ){
                Toast.makeText(this, "Tout remplir", Toast.LENGTH_LONG).show()
            }else{
                val temporaire = Chauffage(
                    chaleur,
                    day,
                    degre_field.text.toString().toFloat(),
                    hours_field.text.toString().toInt(),
                    duree_field.text.toString().toInt(),
                    type)
                println(temporaire.toJSON().toString())

                if(type == 2)
                    pref.lastClimTemp = degre_field.text.toString().toFloat()
                else
                    pref.lastGeoTemp = degre_field.text.toString().toFloat()



                adresseServer = pref.adresseServeur

                val builder = AlertDialog.Builder(this)

                sendToServer(adresseServer, this,temporaire.toJSON()) { ok ->
                    if (ok) {

                        builder.setTitle("Envoyer")
                        builder.setMessage("Les donnees ont bien ete envoye")
                        builder.setPositiveButton("OK") { dialog, with ->


                        }


                    } else {
                        builder.setTitle("Erreur")
                        builder.setMessage("Rien n'est envoyer")
                        builder.setPositiveButton("ressayer") { dialog, with ->

                        }
                    }
                    val dialog = builder.create()
                    dialog.show()

                }






            }

        }



        retour_define_btn.setOnClickListener {
            val retourIntent = Intent(this, MainActivity::class.java)


            startActivity(retourIntent)
        }

    }


    


}
