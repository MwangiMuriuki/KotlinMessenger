package com.ernest.kotlinmessenger.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ernest.kotlinmessenger.ModelClasses.MessageData
import com.ernest.kotlinmessenger.R

class AdapterMessages (val context: Context, private val messageDataList: List<MessageData?>) : RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return CustomViewHolder(layoutInflater, parent)
    }

    override fun getItemCount(): Int = messageDataList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val messageData: MessageData? = messageDataList[position]

        holder.message?.text = messageData?.message

//        val imageUri: Uri? = Uri.parse(messageData?.userDisplayPic)
//        holder.userDP?.let { Glide.with(context).load(imageUri).into(it) }
    }
}

class CustomViewHolder (inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(
    R.layout.sent_message_layout, parent, false)){

    var message: TextView? = null
    var userDP: ImageView? = null

    init {
        message = itemView.findViewById(R.id.messageTextView)
        userDP = itemView.findViewById(R.id.userDisplayPicture)
    }

}
