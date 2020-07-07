package com.ernest.kotlinmessenger.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ernest.kotlinmessenger.Activities.ActivityConversations
import com.ernest.kotlinmessenger.Activities.ActivityMessages
import com.ernest.kotlinmessenger.ModelClasses.ModelClassUserDetails
import com.ernest.kotlinmessenger.R

class UsersAdapter (val context: Context, private val list: List<ModelClassUserDetails?>) : RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val modelClassUserDetails: ModelClassUserDetails? = list[position]

        holder.bind(modelClassUserDetails)
//        holder.usersName?.text = modelClassUserDetails?.username
        val imageUri: Uri? = Uri.parse(modelClassUserDetails?.dp)
        holder.userProfPic?.let { Glide.with(context).load(imageUri).into(it) }

        holder.mainLayout?.setOnClickListener {
            val intent = Intent(context, ActivityMessages::class.java).apply {
                putExtra("username", modelClassUserDetails?.username)
                putExtra("profilePic", modelClassUserDetails?.dp)
                putExtra("otherUserID", modelClassUserDetails?.userID)
            }

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            context.startActivity(intent)
        }
    }

}

class MyViewHolder (inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.select_users_single_item, parent, false)) {

     var userProfPic: ImageView? = null
     var usersName: TextView? = null
     var mainLayout: LinearLayout? = null

    init {
        userProfPic = itemView.findViewById(R.id.userPic)
        usersName = itemView.findViewById(R.id.userNameTV)
        mainLayout = itemView.findViewById(R.id.mainLayout)
    }

    fun bind(userDetailsModel: ModelClassUserDetails?) {
//        userProfPic?.setImageURI() = userDetailsModel.profilePic
        usersName?.text = userDetailsModel?.username

    }

}


