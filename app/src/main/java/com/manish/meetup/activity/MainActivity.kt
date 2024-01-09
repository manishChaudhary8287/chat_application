package com.manish.meetup.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.manish.meetup.R
import com.manish.meetup.fragment.CricketFragment
import com.manish.meetup.fragment.FootballFragment

class MainActivity : AppCompatActivity() {

    lateinit var menubar:NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar:Toolbar
    lateinit var sp: SharedPreferences

    var previousMenuItem:MenuItem?=null

    lateinit var mauth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menubar=findViewById(R.id.MainNavigationView)
        drawerLayout=findViewById(R.id.drawerLayout)
        toolbar=findViewById(R.id.MainToolbar)

        sp=getSharedPreferences("Exposys", Context.MODE_PRIVATE)

        setUpToolbar()
        openCricket()


        mauth= FirebaseAuth.getInstance()

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.nav_open,R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        menubar.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){

                R.id.SelectCricket->{
                    openCricket()
                    drawerLayout.closeDrawers()
                }
                R.id.SelectFootball->
                {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameLayout,FootballFragment()).commit()
                    supportActionBar?.title="Football Profiles"
                    drawerLayout.closeDrawers()
                }

                R.id.logout->
                {
                    mauth.signOut()

                    val intent=Intent(this@MainActivity,LoginActivity::class.java)
                    sp.edit().clear().apply()
                    finish()
                    startActivity(intent)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "your interest "

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openCricket(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout,CricketFragment()).commit()

        supportActionBar?.title="Cricket Profiles"
        menubar.setCheckedItem(R.id.SelectCricket)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.mainFrameLayout)
        when(frag){
            !is CricketFragment -> openCricket()
            else -> super.onBackPressed()
        }

    }
}