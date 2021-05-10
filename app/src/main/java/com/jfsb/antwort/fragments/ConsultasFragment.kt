package com.jfsb.antwort.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwort.R
import com.jfsb.antwort.post.Post
import com.jfsb.antwort.post.PostAdapter
import kotlinx.android.synthetic.main.ly_fragment_consultas.*
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jfsb.antwort.post.CreateActivity


class ConsultasFragment : Fragment() {

    lateinit var rev: RecyclerView
    lateinit var fab: FloatingActionButton
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.ly_fragment_consultas, container, false)
        rev = view.findViewById(R.id.rv)
        fab = view.findViewById(R.id.floatingActionButton)

        db.collection("post").addSnapshotListener{value, error ->
            val posts = value!!.toObjects(Post::class.java)

            posts.forEachIndexed{ index, post ->
                post.uid = value.documents[index].id
            }

//           posts.add(2,Post("Texto donde se explica la duda que se tiene", Date(),"Username"))

            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostAdapter(this@ConsultasFragment,posts)
            }
        }

        fab.setOnClickListener{
            val intent = Intent (requireContext(), CreateActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return view
    }
}