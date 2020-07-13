package com.ernest.kotlinmessenger.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ernest.kotlinmessenger.Adapters.ConvViewHolder
import com.ernest.kotlinmessenger.Adapters.ConversationsAdapter
import com.ernest.kotlinmessenger.ModelClasses.ConvData
import com.ernest.kotlinmessenger.ModelClasses.ModelClassUserDetails
import com.ernest.kotlinmessenger.ModelClasses.UserItems
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_conversations.*


class ActivityConversations : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    val firebaseFirestore = Firebase.firestore
    val userReference = Firebase.database.reference
    var mRootRef = FirebaseDatabase.getInstance().getReference()



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
        val userID: String? = currentUser?.uid

        var mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(userID!!)
        mConvDatabase.keepSynced(true);
        var mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
        var mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(userID)
        mUsersDatabase.keepSynced(true)

        var list = mutableListOf<ConvData>()
        val adapter = ConversationsAdapter(applicationContext, list)
        conversationRecyclerView.adapter = adapter

        getData(list, adapter, currentUser)
    }

    private fun getData(
        list: MutableList<ConvData>,
        adapter: ConversationsAdapter,
        currentUser: FirebaseUser?
    ) {
        mRootRef.child("Messages").child(currentUser?.uid!!)
            .addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val conversations = snapshot.getValue(ConvData::class.java)
                var taskObject = snapshot.key
                val task = ConvData()

                getUserDetails(taskObject, task)

            }

                private fun getUserDetails(taskObject: String?, task: ConvData) {
                    mRootRef.child("Users").child(taskObject!!).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(ModelClassUserDetails::class.java)
                        val user = userData?.username
                        val userDP = userData?.dp

                        with(task){
                            chatUserID = taskObject
                            chatUserName = user!!
                            chatUserPic = userDP!!
                        }

                        val keys = snapshot.key
                        Log.d("Keys", keys!!)
                        list.add(task)

                        adapter.notifyDataSetChanged()

                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
                }

                override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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

