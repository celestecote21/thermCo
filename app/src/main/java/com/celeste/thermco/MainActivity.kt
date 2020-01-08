package com.celeste.thermco

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.BLACK
import android.os.Bundle

import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.celeste.thermco.Services.ContactServ
import com.celeste.thermco.Services.MQTTConnectionParams
import com.celeste.thermco.Services.MQTTmanager
import com.celeste.thermco.UIinterface.UIUpdaterInterface
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import com.celeste.thermco.Utilities.Pref
import com.celeste.thermco.models.Chauffage
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, UIUpdaterInterface{


    private var adresseServer :String = ""
    private var mqttManager: MQTTmanager? = null

    private var lastMessage: String = ""

    private val calendar = GregorianCalendar()
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val pref = Pref(this)
        println(hour)


        val date = Date()
        println(date)
        calendar.time = date

        if(pref.isTheFirstLogging){
            val builderFL = AlertDialog.Builder(this)
            builderFL.setView(R.layout.first_login_layout)
            builderFL.setPositiveButton("ok"){ _, _ ->
                pref.isTheFirstLogging = false
            }
            builderFL.setNegativeButton("revoir"){ _, _ ->

            }
            builderFL.create().show()
        }

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        try {
            val mqttAddressServeur = pref.serveurMqtt

            val mqttConnectioParams = MQTTConnectionParams(
                "telephoneAndroid",
                "tcp://$mqttAddressServeur:1883",
                pref.topicThermostatGet,
                pref.usernameBroker,
                pref.password
            )
            mqttManager = MQTTmanager(mqttConnectioParams, applicationContext, this)

            mqttManager?.connect() { connected ->
                if (connected) {
                    mqttManager?.subscribe(pref.topicThermostatGet)
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this, "verifier l'addresse, l'user name et le password", Toast.LENGTH_LONG).show()
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingIntent)
        }
        adresseServer = pref.adresseServeur



        navView.setNavigationItemSelectedListener(this)
        geo_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 1)

            startActivityForResult(selectorIntent, 12)
        }

        geo_main_btn.setOnLongClickListener {
            //val date = LocalDate.now()
            //val dow = date.dayOfWeek
            var dow = calendar.get(Calendar.DAY_OF_WEEK)

            println(dow)
            if(dow == 1){
                dow = 8
            }

            val day = Array(7){ i ->
                //true
                dow - 2 == i
            }
            val temporaire = Chauffage(pref.lastChaleur, day, pref.lastGeoTemp ,hour,2, 1, pref.defaultTempGeo)
            mqttManager?.publish(temporaire.toJSON().toString(), pref.topicThermostatSet)

            lastMessage = "la geothermie est mise a ${pref.lastGeoTemp} degres pour 2h"
             true
        }

        clim_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 2)

            startActivityForResult(selectorIntent, 12)
        }


        clim_main_btn.setOnLongClickListener {
            //val date = LocalDate.now()
            //val dow = date.dayOfWeek
            var dow = calendar.get(Calendar.DAY_OF_WEEK)

            println(dow)
            if(dow == 1){
                dow = 8
            }

            val day = Array(7){ i ->
                //true
                dow - 2 == i
            }

            val temporaire = Chauffage(pref.lastChaleur, day, pref.lastClimTemp ,hour,2, 2, pref.defaultTempGeo)

            mqttManager?.publish(temporaire.toJSON().toString(), pref.topicThermostatSet)
            lastMessage = "la clim est mise a ${pref.lastClimTemp} degres pour 2h"

            true
        }


        arret_main_btn.setOnClickListener {
            //val day = Array(7){ i -> false}
            //val temporaire = Chauffage(1, day,0.toFloat(),0,0, 1, pref.defaultTempGeo)

            mqttManager?.publish("arret", pref.topicThermostatSet)
            lastMessage = "l'arret a ete envoyer"


            Toast.makeText(this,"arret de tout", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        hour = calendar.get(Calendar.HOUR_OF_DAY)

        if(mqttManager?.isConnected() != true){
            mqttManager?.connect { okai ->
                if(okai)
                    mqttManager?.subscribe(Pref(this).topicThermostatGet)
            }
        }else{
            mqttManager?.subscribe(Pref(this).topicThermostatGet)
        }

    }

    override fun onPause() {
        super.onPause()
        mqttManager?.unsubscribe(Pref(this).topicThermostatGet)
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
                val settingIntent = Intent(this, SettingsActivity::class.java)


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
                val settingIntent = Intent(this, SettingsActivity::class.java)


                startActivity(settingIntent)
            }
            R.id.nav_portail -> {
                try{
                    val portailIntent = Intent(this, portailActivity::class.java)


                    startActivity(portailIntent)}
                catch (ex: Exception){
                    Toast.makeText(this, "merci de renseigner un bon mot de passe et user pour le broker", Toast.LENGTH_LONG).show()
                }
            }
            R.id.nav_volet -> {
                try{
                    val voletIntent = Intent(this, VoletActivity::class.java)


                    startActivity(voletIntent)}
                catch (ex: Exception){
                    Toast.makeText(this, "merci de renseigner un bon mot de passe et user pour le broker", Toast.LENGTH_LONG).show()
                }
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    override fun update(message: String, topic: String?) {
        temperature_main_txt.text = message
        val temperature = try {
            message.toFloat()
        }catch (ex:Exception){
            0.toFloat()
        }
        geo_main_btn.setTextColor(BLACK)
        clim_main_btn.setTextColor(BLACK)
        arret_main_btn.setTextColor(BLACK)
        temperature_main_txt.setTextColor(BLACK)
        ilFait_txt.setTextColor(BLACK)
        if (temperature < 10) {
            layout_de_base_main.setBackgroundColor(BLACK)
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

    override fun mqttError(error: String, complete: Boolean) {
        val builder = AlertDialog.Builder(this)
        if(complete) {
            builder.setTitle("donnees envoye")
            builder.setMessage(lastMessage)
            builder.setPositiveButton("OK") { _, _ -> }
            builder.create().show()
        }else{
            builder.setTitle("erreur dans l'envoi")
            builder.setMessage("merci de verifier la connection")
            builder.setPositiveButton("OK") { _, _ -> }
            builder.create().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val message = data?.getStringExtra("extra_result")
            val topic = data?.getStringExtra("extra_topic")// pour eviter une instance des preferences
            if(message != null && topic != null){
                lastMessage = "donne envoyer"
                mqttManager?.publish(message, topic)
            }

        }
    }
}
