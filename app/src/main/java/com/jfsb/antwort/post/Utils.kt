package com.jfsb.antwort.post

import android.app.AlertDialog
import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.security.auth.callback.Callback

object Utils {
    fun showError(context: Context, message: String){
        AlertDialog.Builder(context).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("Aceptar",null)
        }.show()
    }
    fun consultar_view(mDatabase:DatabaseReference, child:String, user:String, attribute:String, view:TextView) {

        mDatabase.child(child).child(user).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dato = dataSnapshot.child(attribute).value.toString()
                view.text = dato
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}