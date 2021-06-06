package com.internshala.bookapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
//import android.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.internshala.bookapplication.fragment.ProfileFragment
import com.internshala.bookapplication.R
import com.internshala.bookapplication.fragment.AboutFragment
import com.internshala.bookapplication.fragment.DashboardFragment
import com.internshala.bookapplication.fragment.FavouriteFragment


class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout

    var previousmenuitem : MenuItem?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      drawerLayout=findViewById(R.id.drawerlayout)
        coordinatorLayout=findViewById(R.id.coordinatelayout)
        toolbar=findViewById(R.id.toolbar)
        navigationView=findViewById(R.id.navigationview)
        frameLayout=findViewById(R.id.frame)

        setupToolbar()

        openDashboard()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousmenuitem!=null){
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it

            when(it.itemId){
                R.id.dashboard -> {
                    Toast.makeText(this@MainActivity,"clicked dashboard",Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame,
                        DashboardFragment()
                    )
                    .commit()

                    supportActionBar?.title="Dashboard"
                    drawerLayout.closeDrawers()
                }
                R.id.favourite -> {
                    Toast.makeText(this@MainActivity,"clicked favourite",Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.frame,
                        FavouriteFragment()
                    )
                    .commit()

                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    Toast.makeText(this@MainActivity,"clicked profile",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.about -> {
                    Toast.makeText(this@MainActivity,"clicked about",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutFragment()
                        )
                        .commit()

                    supportActionBar?.title="About"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="The Book App"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment =
            DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else-> super.onBackPressed()
        }
    }


}
