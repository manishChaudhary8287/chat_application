package com.manish.meetup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.manish.meetup.R
import com.manish.meetup.model.Message

class MessageAdapter(val context: Context,val messageList:ArrayList<Message>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val ITEM_RECEIVE=1
        val ITEM_SENT=2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==1){
            // inflate receive
            val view:View= LayoutInflater.from(context).inflate(R.layout.receive_message,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            // inflate sent
            val view:View=LayoutInflater.from(context).inflate(R.layout.sent_message,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage=messageList[position]

        if(holder.javaClass==SentViewHolder::class.java){
            // Sent viewHolder works here.
            val viewHolder=holder as SentViewHolder
            holder.sentMessage.text=currentMessage.message
        }
        else{
            // Receive ViewHolder works here.
            val viewHolder=holder as ReceiveViewHolder
            holder.receiveMessage.text=currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else
        {
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.sentMessage)
    }

    class ReceiveViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val receiveMessage=itemView.findViewById<TextView>(R.id.receiveMessage)
    }
}