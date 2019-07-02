package com.example.apptienda

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.apptienda.Fragments.ProductsFragment
import com.example.apptienda.Fragments.ShoppingHistoryFragment
import com.example.apptienda.Fragments.UserFragment
import com.example.apptienda.db.AppDatabase
import com.example.apptienda.db.models.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var currentLoadedFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        checkAdminExist()
        goToLoginActivity()

    }

    private fun initializeHome() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrameLayout, ProductsFragment(), "homeFrag")
        transaction.commit()
        navView.menu.getItem(0).isChecked = true
        supportActionBar!!.title = getString(R.string.action_bar_products_title)
    }



    private fun checkAdminExist(){
        GlobalScope.launch(Dispatchers.IO) {
            if (AppDatabase.getDatabase(applicationContext).userDao().getAllUser().count() == 0) {
                createAdmin()
                if (AppDatabase.getDatabase(applicationContext).userDao().getAllUser()[0].email.toString() != "admin@a.cl"
                ) {
                    createAdmin()
                }
            }
        }
    }

    private fun createAdmin () {
        val admin = createAdminObject()
        val complaintDao = AppDatabase.getDatabase(this).userDao()
        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            try {
                complaintDao.insert(admin)
                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Admin user created", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.d("ERROR", "Error storing complaint ${e.message}")
                    Toast.makeText(applicationContext, "Error storing complaint ${e.message}" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createAdminObject(): User {
        val name = "admin"
        val lastName = "b"
        val address = "calle del admin"
        val email = "admin@a.cl"
        val password = "admin"
        val role = true
        val deleted = false
        return User(name, lastName, address, role, email, password, deleted)
    }

    private fun goToLoginActivity(){
        if (currentLoadedFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(currentLoadedFragment!!).commit()
        }
        startActivityForResult(
            Intent(this, LoginActivity::class.java),
            RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY.value)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white))

        }
        setupNavViewListener()
    }

    private fun setupNavViewListener() {
        navView.setItemIconTintList(null)
        navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            val transaction = supportFragmentManager.beginTransaction()
            when (menuItem.itemId) {


                R.id.products -> {
                    val productsFragment = supportFragmentManager.findFragmentByTag("productsFragment")
                    if (productsFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, productsFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, ProductsFragment(), "productsFragment")
                    }
                    supportActionBar!!.title = getString(R.string.action_bar_products_title)
                }


                R.id.shoppingHistory -> {


                    val shoppingFragment = supportFragmentManager.findFragmentByTag("shoppingHistoryFrag")
                    if (shoppingFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, shoppingFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout,
                            ShoppingHistoryFragment(), "shoppingHistoryFrag")
                    }
                    if (LoginActivity.admin == true) {
                        supportActionBar!!.title = "All Orders"
                    } else {
                        supportActionBar!!.title = getString(R.string.action_bar_shopping_history_title)
                    }
                }

                R.id.deleteUser -> {
                    val  userFragment = supportFragmentManager.findFragmentByTag("userFragment")
                    if (userFragment != null){
                        transaction.replace(R.id.contentFrameLayout, userFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, UserFragment(), "userFragment")
                    }
                    supportActionBar!!.title = getString(R.string.action_bar_users)

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

    private fun hideShoppingHistoryNavDrawer() {
        if (LoginActivity.admin == true) {
           // navView.menu.findItem(R.id.shoppingHistory).isVisible = false
            navView.menu.findItem(R.id.shoppingHistory).title = "All Orders"
            navView.menu.findItem(R.id.deleteUser).isVisible = true
        } else {
            navView.menu.findItem(R.id.shoppingHistory).title = getString(R.string.action_bar_shopping_history_title)
            //navView.menu.findItem(R.id.shoppingHistory).isVisible = true
            navView.menu.findItem(R.id.deleteUser).isVisible = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY.value -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        hideShoppingHistoryNavDrawer()
                        setEmail(data.extras!!)
                        initializeHome()

                    }
                }
            }
        }
    }

    private fun setEmail(bundle: Bundle){
        val userEmail = bundle.getString("EMAIL")!!
        val userPassword = bundle.getString("PASSWORD")!!
        loggedAsTextView.text = "Logged as: " + userEmail
    }

   fun onSignOut() {
        goToLoginActivity()

    }
}
