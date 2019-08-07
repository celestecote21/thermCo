package com.celeste.thermco

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.celeste.thermco.Services.ContactServ.sendToServer
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.activity_define_t.*


class DefineT : AppCompatActivity() {


    private var type = 0
    private var day= Array(7){ i -> false}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_define_t)
        type = intent.getIntExtra(EXTRA_SELECTOR, 0)

        sendBtn.setOnClickListener {
            //TODO:faire que les valeur par defaut sont celle de la derniere fois pour les boutona
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
                val sharedPref = this.getSharedPreferences(getString(R.string.saved_server_key), Context.MODE_PRIVATE)

                val adresseServer = sharedPref.getString(getString(R.string.saved_server_key), "http://192.168.0.4:300/V1/appData")
                val builder = AlertDialog.Builder(this)

                if(adresseServer != null) {
                    sendToServer(adresseServer, this,temporaire.toJSON()){ok ->
                        if(ok){

                            builder.setTitle("Envoyer")
                            builder.setMessage("Les donnees ont bien ete envoye")
                            builder.setPositiveButton("OK"){dialog, with ->

                            }


                        }else {
                            builder.setTitle("Erreur")
                            builder.setMessage("Rien n'est envoyer")
                            builder.setPositiveButton("ressayer") { dialog, with ->
                                sendToServer(adresseServer, this,temporaire.toJSON()){ok ->
                                    if(ok){

                                        builder.setTitle("Envoyer")
                                        builder.setMessage("Les donnees ont bien ete envoye")
                                        builder.setPositiveButton("OK"){dialog, with ->

                                        }


                                    }else {
                                        builder.setTitle("Inaccessible")
                                        builder.setMessage("Rien n'est envoyer")
                                        builder.setPositiveButton("OK") { dialog, with ->

                                        }
                                    }
                                    val dialog = builder.create()
                                    dialog.show()

                                }
                            }
                        }
                        val dialog = builder.create()
                        dialog.show()

                    }
                }else {
                    println("adresse server non defini")
                }


            }

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
