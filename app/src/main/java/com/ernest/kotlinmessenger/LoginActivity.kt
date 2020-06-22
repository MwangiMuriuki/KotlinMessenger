package com.ernest.kotlinmessenger

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance();
    val firestoreDB = Firebase.firestore
    val TAG: String = "My Activity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            val loginEmail = loginEmailField.text.toString()
            val loginPassword = loginPasswordField.text.toString()

            mthdLogin(loginEmail, loginPassword)
        }
    }

    private fun mthdLogin(loginEmail: String, loginPassword: String) {

        mAuth!!.signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, ChatList::class.java)
        startActivity(intent)
    }
}