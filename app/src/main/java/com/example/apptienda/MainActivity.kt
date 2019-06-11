package com.example.apptienda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var currentLoadedFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        goToLoginActivity()
    }

    private fun goToLoginActivity(){
        startActivityForResult(
            Intent(this, LoginActivity::class.java),
            RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY.value)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
        setupNavViewListener()
    }

    private fun setupNavViewListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            val transaction = supportFragmentManager.beginTransaction()
            when (menuItem.itemId) {

                R.id.shoppingHistory -> {
                    val shoppingFragment = supportFragmentManager.findFragmentByTag("shoppingHistoryFrag")
                    if (shoppingFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, shoppingFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, ShoppingHistoryFragment(), "shoppingHistoryFrag")
                    }
                    supportActionBar!!.title = getString(R.string.action_bar_shopping_history_title)
                }

                R.id.signOut -> {
                    onSignOut()
                }
            }

            transaction.commit()
            true
        }
    }

    // Called when navDrawer is opened (used to display the menu)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

   fun onSignOut() {
        goToLoginActivity()
    }
}
