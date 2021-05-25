package com.jfsb.antwort.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jfsb.antwort.R
import com.jfsb.antwort.social.FriendAdapter
import com.jfsb.antwort.social.FriendCard

class FriendsFragment: Fragment() {

    lateinit var rev: RecyclerView
    private val db = FirebaseDatabase.getInstance()
    private val db_ref = db.reference
    private val auth = FirebaseAuth.getInstance()
    private val friends: MutableList<FriendCard> = mutableListOf()
    private lateinit var friend:FriendCard
    private var friensCount = 0

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.ly_fragment_friends, container, false)
        rev = view.findViewById(R.id.rv_friends)

            db_ref.child("Users").child(auth.currentUser.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val children = dataSnapshot.child("friends")!!.children
                    friensCount = dataSnapshot.child("friends")!!.children.count()

                    friends.clear()

                    children.forEach {
                        val uid = it.value
                        db_ref.child("Users").child(uid.toString()).addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(datatwo: DataSnapshot) {
                                val imageProfile = datatwo.child("imgProfile").value.toString()
                                val username = datatwo.child("username").value.toString()
                                val name = datatwo.child("name").value.toString()
                                val id = datatwo.child("uid").value.toString()
                                val userType = datatwo.child("usertype").value.toString()

                                friend = FriendCard(imageProfile, username, name, id, userType)

                                listFriend()
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        return view
    }

    fun listFriend(limit:Int = friensCount){
        friends.add(friend)

        if(friends.size == limit){
            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = FriendAdapter(friends)
            }
        }
    }
}