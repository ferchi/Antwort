package com.jfsb.antwort.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jfsb.antwort.R
import com.jfsb.antwort.social.FriendAdapter
import com.jfsb.antwort.social.FriendCard
import kotlinx.android.synthetic.main.ly_fragment_friends.*

class FriendsFragment: Fragment() {

    lateinit var rev: RecyclerView
    private val db = FirebaseDatabase.getInstance()
    private val db_ref = db.reference
    private val auth = FirebaseAuth.getInstance()
    private val friends: MutableList<FriendCard> = mutableListOf()
    private lateinit var friend:FriendCard

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.ly_fragment_friends, container, false)
        rev = view.findViewById(R.id.rv_friends)

            db_ref.child("Users").child(auth.currentUser.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val children = dataSnapshot.child("friends")!!.children

                    children.forEach {
                        val uid = it.value
                        db_ref.child("Users").child(uid.toString()).addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(datatwo: DataSnapshot) {
                                val imageProfile = datatwo.child("imgProfile").value.toString()
                                val username = datatwo.child("username").value.toString()
                                val name = datatwo.child("name").value.toString()

                                friend = FriendCard(imageProfile, username, name, uid.toString())

                                addFriend(2)
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

    fun addFriend(limit:Int){
        Log.d("friend",friends.size.toString())
        Log.d("limit",limit.toString())

        friends.add(friend)

        if(friends.size == limit){
            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = FriendAdapter(this@FriendsFragment,friends)
            }
        }
    }
}