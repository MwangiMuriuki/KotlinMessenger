package com.ernest.kotlinmessenger.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ernest.kotlinmessenger.Adapters.UsersAdapter
import com.ernest.kotlinmessenger.ModelClasses.ModelClassUserDetails
import com.ernest.kotlinmessenger.R
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

//        selectUsersRecyclerView.apply {
//            adapter = UsersAdapter(myUsers)
//        }

        val myUsers = listOf<ModelClassUserDetails>()

//        val adapter = GroupAdapter<GroupieViewHolder>()
//        adapter.add(UserItems())
//        adapter.add(UserItems())
//        adapter.add(UserItems())
//        adapter.add(UserItems())
//        adapter.add(UserItems())
//        adapter.add(UserItems())
//        selectUsersRecyclerView.adapter = adapter


//        fetchUsers()

        var list = mutableListOf<ModelClassUserDetails>()
        val adapter = UsersAdapter(applicationContext, list)
        selectUsersRecyclerView.adapter = adapter

        getData(list, adapter)

    }

    private fun getData(
        list: MutableList<ModelClassUserDetails>,
        adapter: UsersAdapter
    ) {
        val usersRef = FirebaseFirestore.getInstance().collection("Users")

       usersRef.get().addOnSuccessListener { snapshot ->
           for (document in snapshot){

               list.add(document.toObject(ModelClassUserDetails::class.java))

           }
           adapter.notifyDataSetChanged()
       }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


