package com.manish.meetup.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manish.meetup.R
import com.manish.meetup.adapter.UserAdapter
import com.manish.meetup.model.User


class FootballFragment : Fragment() {

    lateinit var FootballRecyclerView: RecyclerView
    lateinit var userList:ArrayList<User>
    lateinit var adapter: UserAdapter

    lateinit var mauth: FirebaseAuth
    lateinit var mDBRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_football, container, false)

        FootballRecyclerView=view.findViewById(R.id.FootballRecyclerView)

        mauth= FirebaseAuth.getInstance()
        mDBRef= FirebaseDatabase.getInstance().getReference()

        userList= ArrayList()
        adapter= UserAdapter(activity as Context,userList)

        FootballRecyclerView.layoutManager= LinearLayoutManager(activity as Context)
        FootballRecyclerView.adapter=adapter

        mDBRef.child("Football").child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)
                    if(mauth.currentUser?.uid!=currentUser?.uid)
                    {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return view
    }


}