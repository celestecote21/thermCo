package com.celeste.thermco

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        sendBtn.setOnClickListener {

            if(degre_field.text.isEmpty() || hours_field.text.isEmpty() || duree_field.text.isEmpty() ){
                Toast.makeText(this, "Tout remplir", Toast.LENGTH_LONG).show()
            }else{
                val temporaire = Chauffage(
                    type,
                    day,
                    degre_field.text.toString().toFloat(),
                    hours_field.text.toString().toInt(),
                    duree_field.text.toString().toInt())
                println(temporaire.toJSON().toString())

                if(type == 2)
                    pref.last_clim_temp = degre_field.text.toString().toFloat()
                else
                    pref.last_geo_temp = degre_field.text.toString().toFloat()



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

    fun onLundiClicked(view: View){
        day[0] = !day[0]


    }
    fun onMardiClicked(view: View){
        day[1] = !day[1]
    }
    fun onMercrediClicked(view: View){
        day[2] = !day[2]

    }
    fun onJeudiClicked(view: View){
        day[3] = !day[3]

    }
    fun onVendrediClicked(view: View){
        day[4] = !day[4]

    }
    fun onSamediClicked(view: View){
        day[5] = !day[5]

    }
    fun onDimancheClicked(view: View){
        day[6] = !day[6]

    }


}
