package com.example.apptienda.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.apptienda.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


import com.example.apptienda.Adapters.UserAdapter
import com.example.apptienda.db.models.User
import java.lang.Exception
import com.example.apptienda.*
import com.example.apptienda.R
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUser()
        setListOnClickListeners()
    }

    private fun loadUser(){
        val usrsTotal = AppDatabase.getDatabase(context!!).userDao()

        GlobalScope.launch(Dispatchers.IO) {
            val usrs = usrsTotal.getAllUser()
            launch(Dispatchers.Main){
                val itemsAdapter = UserAdapter(context!!, ArrayList(usrs))
                userListView.adapter = itemsAdapter
            }
        }
    }

    private fun setListOnClickListeners(){
        val usrAction = AppDatabase.getDatabase(context!!).userDao()
        var name = ""
        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = (userListView.adapter).getItem(position) as User
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Confirm Deletion")
            builder.setMessage("Are you sure you want to delete this user?")
            builder.setPositiveButton("Delete"){_, _->
                GlobalScope.launch(Dispatchers.IO){
                    name = (selectedUser.name.toString())
                    usrAction.deleteUser(selectedUser)
                }
                Toast.makeText(context!!, "User : " + name + " shall now be a memory that no longer exists", Toast.LENGTH_SHORT).show()
            }
            builder.setNeutralButton("Cancel"){_, _ ->}

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        //reload() deberia actualizar la lista
    }

    private fun reload(){
        //var fragment: Fragment? = getFragmentManager()?.findFragmentByTag("userFragment")
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        GlobalScope.launch(Dispatchers.IO) {
            ft?.replace(R.id.contentFrameLayout, UserFragment(), "userFragment")
            ft?.commit()
        }
    }

}
