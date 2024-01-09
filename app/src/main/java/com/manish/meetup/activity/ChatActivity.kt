package com.manish.meetup.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manish.meetup.R
import com.manish.meetup.adapter.MessageAdapter
import com.manish.meetup.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sentButton:ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var mDBRef:DatabaseReference

    var senderRoom:String?=null
    var receiverRoom:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView=findViewById(R.id.chatRecyclerView)
        messageBox=findViewById(R.id.etMessageBox)
        sentButton=findViewById(R.id.imgSent)
        toolbar=findViewById(R.id.chatToolbar)


        val name=intent.getStringExtra("name")
        val receiver_uid=intent.getStringExtra("uid")
        val sender_uid=FirebaseAuth.getInstance().currentUser?.uid

        mDBRef=FirebaseDatabase.getInstance().getReference()

        senderRoom=receiver_uid+sender_uid
        receiverRoom=sender_uid+receiver_uid

        setUpToolbar(name)

        messageList=ArrayList()
        messageAdapter= MessageAdapter(this,messageList)
        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        //Displaying message in the recyclerView
        mDBRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                // nothing to do here now.
                }

            })


        sentButton.setOnClickListener{

            val message=messageBox.text.toString()
            val messageObject=Message(message,sender_uid)

            mDBRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDBRef.child("chats").child(receiverRoom!!).child("messages")
                        .push().setValue(messageObject)
                }
            messageBox.setText("")
        }

    }
    private fun setUpToolbar(name:String?){
        setSupportActionBar(toolbar)
        supportActionBar?.title = name
    }
}