package com.ernest.kotlinmessenger.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.ernest.kotlinmessenger.ModelClasses.MessageData
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth

class AdapterMessages (val context: Context, private val messageDataList: List<MessageData?>) : RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return CustomViewHolder(layoutInflater, parent)
    }

    override fun getItemCount(): Int = messageDataList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val messageData: MessageData? = messageDataList[position]

        val mAuth:FirebaseAuth? = FirebaseAuth.getInstance()
        var loggedInUser: String? = mAuth?.currentUser?.uid
        var sender: String? = messageData?.sender
        val layoutParams: RelativeLayout.LayoutParams = holder.message?.layoutParams as RelativeLayout.LayoutParams

        holder.message?.text = messageData?.message

        if (loggedInUser.equals(sender)){
            holder.message?.setBackgroundResource(R.drawable.sent_message_bubble_background)
            holder.message?.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            holder.message?.layoutParams = layoutParams
        }else{
            holder.message?.setBackgroundResource(R.drawable.received_message_bubble_background)
            holder.message?.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
            holder.message?.layoutParams = layoutParams
        }

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
