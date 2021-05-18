package com.jfsb.antwort.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwort.post.Utils
import com.jfsb.antwort.R
import kotlinx.android.synthetic.main.activity_create.*
import java.util.*

class CreateActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        publicar_btn.setOnClickListener{
            val postString = postText.text.toString()
            val date = Date()
            val userName = auth.currentUser!!.displayName
            val userId = auth.currentUser!!.uid

            Log.d("prueba",date.toString())
            Log.d("prueba",userName)

            val post = Post(postString, date, userName ,userId)

            db.collection("post").add(post)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener{
                    Utils.showError(this, it.message.toString())
                }
        }
    }
}