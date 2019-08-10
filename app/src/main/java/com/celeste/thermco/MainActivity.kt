package com.celeste.thermco

import android.content.Intent
import android.graphics.Color
import android.os.Bundle

import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.celeste.thermco.Services.ContactServ
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.Utilities.Pref
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    var adresseServer :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = Pref(this)

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
            val date = LocalDate.now()
            val dow = date.dayOfWeek

            val day = Array(7){ i ->
                //true
                dow.value == i
            }

            val temporaire = Chauffage(1, day, pref.last_geo_temp ,10,3)
            val builder = AlertDialog.Builder(this)

            ContactServ.sendToServer(adresseServer, this, temporaire.toJSON()){ ok ->
                if(ok){
                    println(temporaire.toJSON())
                    Toast.makeText(this, "chauffage activer pour 2h", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "probleme", Toast.LENGTH_SHORT).show()
                }


            }
            true
        }

        clim_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 2)

            startActivity(selectorIntent)
        }
        arret_main_btn.setOnClickListener {
            val day = Array(7){ i -> false}
            val temporaire = Chauffage(1, day,0.toFloat(),0,0)
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
            R.id.action_settings -> true
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
