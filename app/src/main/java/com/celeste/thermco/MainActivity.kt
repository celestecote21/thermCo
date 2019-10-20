package com.celeste.thermco

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.textclassifier.ConversationActions
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.celeste.thermco.Services.ContactServ
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.Utilities.Pref
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.content_main.*
import java.time.*
import java.time.format.TextStyle
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    var adresseServer :String = ""
    val calendar = GregorianCalendar()
    var hour = calendar.get(Calendar.HOUR_OF_DAY)

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        val pref = Pref(this)
        println(hour)


        val date = Date()
        println(date)
        calendar.time = date

        if(pref.isTheFirstLogging){
            val builderFL = AlertDialog.Builder(this)
            builderFL.setView(R.layout.first_login_layout)
            builderFL.setPositiveButton("ok"){ p1, p2 ->
                pref.isTheFirstLogging = false
            }
            builderFL.setNegativeButton("revoir"){p1, p2 ->

            }
            builderFL.create().show()
        }

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var temperature: Float = 0.toFloat()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        adresseServer = pref.adresseServeur



        ContactServ.receiveTemperature(adresseServer, this){ ok:Boolean, temp:Float ->
                changementDeTemperature(ok, temp)
        }


        navView.setNavigationItemSelectedListener(this)
        geo_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 1)

            startActivity(selectorIntent)
        }

        geo_main_btn.setOnLongClickListener {
            //val date = LocalDate.now()
            //val dow = date.dayOfWeek
            val dow = calendar.get(Calendar.DAY_OF_WEEK)
            //hour = 4

            val day = Array(7){ i ->
                //true
                dow - 2 == i
            }

            val temporaire = Chauffage(pref.last_chaleur, day, pref.last_geo_temp ,hour,2, 1)
            val builder = AlertDialog.Builder(this)


            ContactServ.sendToServer(adresseServer, this, temporaire.toJSON()){ ok ->
                if(ok){

                    builder.setTitle("chauffage mis")
                    builder.setMessage("la geothermie est mise a ${pref.last_geo_temp} degres pour 2h")
                    builder.setPositiveButton("OK"){dialog, with ->

                    }


                }else{
                    builder.setTitle("Erreur")
                    builder.setMessage("le serveur n'est pas accessible")
                    builder.setPositiveButton("OK"){dialog, with ->

                    }
                }
                builder.create().show()

            }
            true
        }

        clim_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 2)

            startActivity(selectorIntent)
        }


        clim_main_btn.setOnLongClickListener {
            //val date = LocalDate.now()
            //val dow = date.dayOfWeek
            val dow = calendar.get(Calendar.DAY_OF_WEEK)


            val day = Array(7){ i ->
                //true
                dow - 2 == i
            }

            val temporaire = Chauffage(pref.last_chaleur, day, pref.last_clim_temp ,hour,2, 2)
            val builder = AlertDialog.Builder(this)

            ContactServ.sendToServer(adresseServer, this, temporaire.toJSON()){ ok ->
                if(ok){
                    builder.setTitle("clim mis")
                    builder.setMessage("la clim est mise a ${pref.last_clim_temp} degres pour 2h")
                    builder.setPositiveButton("OK"){dialog, with ->

                    }


                }else{
                    println(temporaire.toJSON())
                    builder.setTitle("Erreur")
                    builder.setMessage("le serveur n'est pas accessible")
                    builder.setPositiveButton("OK"){dialog, with ->

                    }
                }
                builder.create().show()

            }
            true
        }


        arret_main_btn.setOnClickListener {
            val day = Array(7){ i -> false}
            val temporaire = Chauffage(1, day,0.toFloat(),0,0, 1)
            val builder = AlertDialog.Builder(this)

            ContactServ.sendToServer(adresseServer, this, temporaire.toJSON()){ ok ->
                if(ok){

                    builder.setTitle("Envoyer")
                    builder.setMessage("Les donnees ont bien ete envoye")
                    builder.setPositiveButton("OK"){dialog, with ->

                    }


                }else{
                    builder.setTitle("Erreur")
                    builder.setMessage("Rien ce c'est arrete")
                    builder.setPositiveButton("OK"){dialog, with ->

                    }
                }
                val dialog = builder.create()
                dialog.show()

            }

            Toast.makeText(this,"arret de tout", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        ContactServ.receiveTemperature(adresseServer, this) { ok: Boolean, temp: Float ->
            changementDeTemperature(ok, temp)
        }
        hour = calendar.get(Calendar.HOUR_OF_DAY)



    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingIntent = Intent(this, Setting::class.java)


                startActivity(settingIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_setting -> {
                val settingIntent = Intent(this, Setting::class.java)


                startActivity(settingIntent)
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun changementDeTemperature(good: Boolean, temperature: Float) {
        if (good) {
            temperature_main_txt.text = temperature.toString()
            if (temperature < 10) {
                layout_de_base_main.setBackgroundColor(Color.BLACK)
                geo_main_btn.setTextColor(Color.WHITE)
                clim_main_btn.setTextColor(Color.WHITE)
                arret_main_btn.setTextColor(Color.WHITE)
                temperature_main_txt.setTextColor(Color.WHITE)
                ilFait_txt.setTextColor(Color.WHITE)
            } else if (temperature >= 10 && temperature < 23) {
                layout_de_base_main.setBackgroundColor(Color.parseColor("#FF3673B9"))
            } else {
                layout_de_base_main.setBackgroundColor(Color.RED)
            }
        } else {
            Toast.makeText(this, "serveur pas accessible", Toast.LENGTH_LONG).show()

            temperature_main_txt.text = 0.toString()
            if (temperature < 10) {
                layout_de_base_main.setBackgroundColor(Color.BLACK)
                geo_main_btn.setTextColor(Color.WHITE)
                clim_main_btn.setTextColor(Color.WHITE)
                arret_main_btn.setTextColor(Color.WHITE)
                temperature_main_txt.setTextColor(Color.WHITE)
                ilFait_txt.setTextColor(Color.WHITE)
            } else if (temperature >= 10 && temperature < 23) {
                layout_de_base_main.setBackgroundColor(Color.parseColor("#FF3673B9"))
            } else {
                layout_de_base_main.setBackgroundColor(Color.RED)
            }
        }
    }
}
