package com.ernest.kotlinmessenger.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ActivityCheckLogin : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userLoggedIn = mAuth.currentUser

        if (userLoggedIn != null) {
            val Login = Intent(applicationContext, ActivityConversations::class.java)
            startActivity(Login)
        } else {
            val MainPage = Intent(applicationContext, ActivityRegister::class.java)
            startActivity(MainPage)
        }

        finish()

    }
}