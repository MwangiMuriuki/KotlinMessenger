package com.ernest.kotlinmessenger.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ernest.kotlinmessenger.Adapters.UsersAdapter
import com.ernest.kotlinmessenger.ModelClasses.ModelClassUserDetails
import com.ernest.kotlinmessenger.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_select_user.*


class SelectUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)
        setSupportActionBar(selectUserToolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayUseLogoEnabled(true)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)

        var list = mutableListOf<ModelClassUserDetails>()
        val adapter = UsersAdapter(applicationContext, list)
        selectUsersRecyclerView.adapter = adapter

        getData(list, adapter)

    }

    private fun getData(
        list: MutableList<ModelClassUserDetails>,
        adapter: UsersAdapter
    ) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
        usersRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val userList = it.getValue(ModelClassUserDetails::class.java)
                    list.add(userList!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


