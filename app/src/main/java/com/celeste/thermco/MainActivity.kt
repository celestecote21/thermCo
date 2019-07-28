package com.celeste.thermco

import android.content.Context
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
import com.celeste.thermco.Services.ContactServ
import com.celeste.thermco.Utilities.EXTRA_SELECTOR
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val sharedPref = this.getSharedPreferences(getString(R.string.saved_server_key), Context.MODE_PRIVATE)

        val adresseServer = sharedPref.getString(getString(R.string.saved_server_key), "http://192.168.0.4:300/V1/appData")

        if(adresseServer != null) {
            temperature = ContactServ.receiveTemperature(adresseServer, this)
        }else {
            println("adresse server non defini")
        }

        temperature_main_txt.text = temperature.toString()

        navView.setNavigationItemSelectedListener(this)

        geo_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 1)

            startActivity(selectorIntent)
        }
        clim_main_btn.setOnClickListener {
            val selectorIntent = Intent(this, DefineT::class.java)
            selectorIntent.putExtra(EXTRA_SELECTOR, 2)

            startActivity(selectorIntent)
        }


        if(temperature < 10){
            layout_de_base_main.setBackgroundColor(Color.BLACK)
            geo_main_btn.setTextColor(Color.WHITE)
            clim_main_btn.setTextColor(Color.WHITE)
            temperature_main_txt.setTextColor(Color.WHITE)
            ilFait_txt.setTextColor(Color.WHITE)
        }else if(temperature >= 10 && temperature < 23){
            layout_de_base_main.setBackgroundColor(Color.parseColor("#FF3673B9"))
        }else{
            layout_de_base_main.setBackgroundColor(Color.RED)
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
}
