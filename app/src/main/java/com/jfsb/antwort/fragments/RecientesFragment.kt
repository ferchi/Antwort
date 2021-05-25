package com.jfsb.antwort.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Preconditions
import com.jfsb.antwort.R
import com.jfsb.antwort.post.Post
import com.jfsb.antwort.post.PostAdapter
import kotlinx.android.synthetic.main.ly_perfilmain.*
import java.util.*


class RecientesFragment : Fragment() {

    lateinit var rev: RecyclerView
    lateinit var fab: FloatingActionButton
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userDB_ref = FirebaseDatabase.getInstance().reference

    var postsFriends: MutableList<Post> = mutableListOf()
    var friends: MutableList<String> = mutableListOf()
    var deleteFriends: MutableList <Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.ly_fragment_recientes, container, false)
        rev = view.findViewById(R.id.rv_recientes)

        setPosts()

        return view
    }

    fun setPosts(){
        db.collection("post").addSnapshotListener{value, error ->
            val posts = value!!.toObjects(Post::class.java)


            //postsFriends.clear()
            getFriends()

            posts.forEachIndexed{ index, post ->
                //if(value.documents[index].data?.values?.contains() == true)
                post.uid = value.documents[index].id
                if(!friends.contains(post.userId)){
                    deleteFriends.add(post)
                }
            }
            deleteFriends.forEach{ post ->
                posts.remove(post)
            }

            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostAdapter(this@RecientesFragment,posts)
            }
        }
    }

    fun getFriends(){
        userDB_ref.child("Users").child(auth.currentUser.uid).child("friends").get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach{
                friends.add(it.value.toString())
            }
        }
    }

}