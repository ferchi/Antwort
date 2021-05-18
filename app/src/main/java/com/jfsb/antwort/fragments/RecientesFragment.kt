package com.jfsb.antwort.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwort.R
import com.jfsb.antwort.post.Post
import com.jfsb.antwort.post.PostAdapter


class RecientesFragment : Fragment() {

    lateinit var rev: RecyclerView
    lateinit var fab: FloatingActionButton
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.ly_fragment_recientes, container, false)
        rev = view.findViewById(R.id.rv_recientes)

        db.collection("post").addSnapshotListener{value, error ->
            val posts = value!!.toObjects(Post::class.java)

            posts.forEachIndexed{ index, post ->
                post.uid = value.documents[index].id

            }

            //posts.add(2,Post("Texto donde se explica la duda que se tiene", Date(),"Username"))

            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostAdapter(this@RecientesFragment,posts)
            }
        }
        return view
    }
}