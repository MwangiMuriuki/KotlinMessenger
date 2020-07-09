package com.ernest.kotlinmessenger.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ernest.kotlinmessenger.Adapters.AdapterMessages
import com.ernest.kotlinmessenger.ModelClasses.MessageData
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.local.LruGarbageCollector.Results
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_messages.*
import java.util.*


class ActivityMessages : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    val firestoreDB = Firebase.firestore
    var databaseReference = FirebaseDatabase.getInstance()
    var mRootRef = FirebaseDatabase.getInstance().getReference()
    var currentUserID: String? = null
    var fetchedMessageList: MessageData? = null


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
        currentUserID = mAuth!!.currentUser!!.uid

        var dbReference = databaseReference.getReference("Chats")

        val bundle: Bundle? = intent.extras
        val userName: String? = intent.getStringExtra("username")
        var userProfPic: String? = intent.getStringExtra("profilePic")
        var otherUserID: String? = intent.getStringExtra("otherUserID")

        supportActionBar?.title = userName

        val sentMessageslist = mutableListOf<MessageData>()
        val adapter = AdapterMessages(applicationContext, sentMessageslist)
        messagesRecyclerView.adapter = adapter

        mRootRef.child("Chats").child(currentUserID!!).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(otherUserID!!)){
                    val chatMap:HashMap<String,Any> = HashMap<String,Any>()
                    chatMap["timestamp"] = ServerValue.TIMESTAMP

                    val chatUserMap:HashMap<String,Any> = HashMap<String,Any>()
                    chatUserMap["Chat/$currentUserID/$otherUserID"] = chatMap.toString()
                    chatUserMap["Chat/$otherUserID/$currentUserID"] = chatMap.toString()

                    mRootRef.updateChildren(chatUserMap, DatabaseReference.CompletionListener { error, ref ->
                        if (error != null){
                            Log.e("Chat Error", error.message.decapitalize())
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        loadMessages(sentMessageslist, adapter, otherUserID)

        sendMessageBtn.setOnClickListener {
            val enteredMessage: String? = editTextEnterMessage.text.toString()
            if (enteredMessage!!.isEmpty()){
                Toast.makeText(baseContext, "Please Enter Message", Toast.LENGTH_SHORT).show()
            }else{
                sendMessageMthd(enteredMessage, currentUserID!!, otherUserID, dbReference)
            }
        }
    }

    private fun loadMessages(
        sentMessageslist: MutableList<MessageData>,
        adapter: AdapterMessages,
        otherUserID: String?
    ) {

        mRootRef.child("Messages").child(currentUserID!!).child(otherUserID!!).addChildEventListener(
            object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val messages: MessageData? = snapshot.getValue(MessageData::class.java)
                    sentMessageslist.add(messages!!)
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
            }
        )

    }

    private fun sendMessageMthd(
        enteredMessage: String,
        currentUserID: String,
        otherUserID: String?,
        dbReference: DatabaseReference
    ) {
        val chatUserRef: String = "Messages/$otherUserID/$currentUserID"
        val currentUserRef: String = "Messages/$currentUserID/$otherUserID"

        val messageKey: DatabaseReference = mRootRef.child("Messages").child(currentUserRef).child(chatUserRef).push()
        val keyPushId = messageKey.key

        val messagesMap:HashMap<String,Any> = HashMap<String,Any>()
        messagesMap["message"] = enteredMessage
        messagesMap["seen"] = false
        messagesMap["type"] = "text"
        messagesMap["timestamp"] = ServerValue.TIMESTAMP
        messagesMap["sender"] = currentUserID

        val messagesChatMap:HashMap<String,Any> = HashMap<String,Any>()
        messagesChatMap["$currentUserRef/$keyPushId"] = messagesMap
        messagesChatMap["$chatUserRef/$keyPushId"] = messagesMap

        mRootRef.updateChildren(messagesChatMap, DatabaseReference.CompletionListener { error, ref ->
            if (error == null){
                editTextEnterMessage.text.clear()
                Toast.makeText(baseContext, "Message sent", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(baseContext, "Error Sending message", Toast.LENGTH_SHORT).show()
                Log.e("Message Error", error.message.decapitalize())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}