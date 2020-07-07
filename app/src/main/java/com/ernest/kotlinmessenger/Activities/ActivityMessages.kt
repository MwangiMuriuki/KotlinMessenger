package com.ernest.kotlinmessenger.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_messages.*
import java.util.HashMap

class ActivityMessages : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    val firestoreDB = Firebase.firestore
    var databaseReference = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        setSupportActionBar(messagesToolbar)
        supportActionBar?.apply {
            setDisplayUseLogoEnabled(true)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)
        mAuth = FirebaseAuth.getInstance()
        var dbReference = databaseReference.getReference("Chats")

        val currentUserID = mAuth!!.currentUser!!.uid

        val bundle: Bundle? = intent.extras
        val userName: String? = intent.getStringExtra("username")
        var userProfPic: String? = intent.getStringExtra("profilePic")
        var otherUserID: String? = intent.getStringExtra("otherUserID")

        supportActionBar?.title = userName

        sendMessageBtn.setOnClickListener {
            var enteredMessage: String? = editTextEnterMessage.text.toString()
            if (enteredMessage!!.isEmpty()){
                Toast.makeText(baseContext, "Please Enter Message", Toast.LENGTH_SHORT).show()
            }else{
                sendMessageMthd(enteredMessage, currentUserID, otherUserID, dbReference)
            }
        }
    }

    private fun sendMessageMthd(
        enteredMessage: String,
        currentUserID: String,
        otherUserID: String?,
        dbReference: DatabaseReference
    ) {

        val chatUserRef: String = "Messages/" + otherUserID + "/" + currentUserID
        val currentUserRef: String = "Messages/" + currentUserID + "/" + otherUserID

        var user_message_key = FirebaseFirestore.getInstance().collection("Chats").document(currentUserRef).collection(chatUserRef).document(chatUserRef)
        var push_id = user_message_key.id

        var message_key = FirebaseDatabase.getInstance().getReference("Chats").child("messages")
            .child(currentUserRef)
            .child(chatUserRef)
            .push()
        var key_push_id = message_key.key

        val messagesMap:HashMap<String,String> = HashMap<String,String>()
        messagesMap.put("message", enteredMessage)
        messagesMap.put("type", "text")

        val messagesChatMap:HashMap<String,String> = HashMap<String,String>()
        messagesMap.put(currentUserRef + "/" + key_push_id, messagesMap.toString())
        messagesMap.put(chatUserRef + "/" + key_push_id, messagesMap.toString())

        dbReference.updateChildren(messagesChatMap as Map<String, Any>, DatabaseReference.CompletionListener { error, ref ->
            if (error == null){
                Toast.makeText(baseContext, "Message sent", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(baseContext, "Error Sending message", Toast.LENGTH_SHORT).show()
                Log.e("Message Error", error.toString())
            }
        })

//        firestoreDB.collection("Messages")
//            .document(currentUserRef)
//            .collection("receiver")
//            .document(chatUserRef)
//            .set(messagesChatMap)
//            .addOnCompleteListener {
//                if (it.isSuccessful){
//                    Toast.makeText(baseContext, "Message sent", Toast.LENGTH_SHORT).show()
//                }else{
//                    it.exception.toString()
//                    Toast.makeText(baseContext, "Error Sending message", Toast.LENGTH_SHORT).show()
//                    Log.e("Message Error", it.exception.toString())
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(baseContext, "Ooops!!!", Toast.LENGTH_SHORT).show()
//            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}