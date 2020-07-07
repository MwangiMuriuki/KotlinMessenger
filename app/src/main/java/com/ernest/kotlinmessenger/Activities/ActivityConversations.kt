package com.ernest.kotlinmessenger.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_conversations.*


class ActivityConversations : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    val firebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)
        setSupportActionBar(chatListToolbar)
        supportActionBar?.apply {
            title = "Messages"

//            setDisplayUseLogoEnabled(false)
//            setIcon(R.drawable.avatar)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth!!.currentUser

        if (currentUser!=null){
            val userID: String = currentUser.uid
            firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener {
                if (it.isSuccessful){
                    val documentSnapshot: DocumentSnapshot? = it.result
                    name.text = documentSnapshot!!.getString("username")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.newMessage -> {
                val NewMessage = Intent(applicationContext, SelectUserActivity::class.java)
                startActivity(NewMessage)
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()

                val Register = Intent(applicationContext, ActivityRegister::class.java)
                startActivity(Register)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}