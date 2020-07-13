package com.ernest.kotlinmessenger.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ernest.kotlinmessenger.Activities.ActivityMessages
import com.ernest.kotlinmessenger.ModelClasses.ConvData
import com.ernest.kotlinmessenger.ModelClasses.MessageData
import com.ernest.kotlinmessenger.ModelClasses.ModelClassUserDetails
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ConversationsAdapter (val context: Context, private val conversationDataList: List<ConvData>) : RecyclerView.Adapter<ConvViewHolder>(){
    var mRootRef = FirebaseDatabase.getInstance().getReference()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ConvViewHolder(layoutInflater, parent)
    }

    override fun getItemCount(): Int = conversationDataList.size

    override fun onBindViewHolder(holder: ConvViewHolder, position: Int) {
        var convData: ConvData? = conversationDataList[position]

        val mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
        val loggedInUser: String? = mAuth?.currentUser?.uid
        val sender: String? = convData?.chatUserName
        val imageUri: Uri? = Uri.parse(convData?.chatUserPic)
        val chatUserID: String? = convData?.chatUserID

        holder.senderName?.text = sender
        holder.senderPicture?.let { Glide.with(context).load(imageUri).into(it) }
        holder.layout?.setOnClickListener {
            val intent = Intent(context, ActivityMessages::class.java).apply {
                putExtra("username", convData?.chatUserName)
                putExtra("profilePic", convData?.chatUserPic)
                putExtra("otherUserID", convData?.chatUserID)
            }

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            context.startActivity(intent)
        }
    }
}

class ConvViewHolder (inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.conversation_list_single_item, parent, false)){
    var senderName: TextView? = null
    var lastText: TextView? = null
    var senderPicture: CircleImageView? = null
    var layout: LinearLayout? = null

    init {
        senderName = itemView.findViewById(R.id.senderUserName)
        lastText = itemView.findViewById(R.id.lastReceived)
        senderPicture = itemView.findViewById(R.id.senderPic)
        layout = itemView.findViewById(R.id.mainLayoutConversationList)
    }

}

