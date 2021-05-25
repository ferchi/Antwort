package com.jfsb.antwort.post

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

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

    fun openProfile(mDatabase:DatabaseReference, child:String, user:String, origen:Context, destino:AppCompatActivity, miperfil:Boolean) {

        mDatabase.child(child).child(user).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("username").value.toString()
                val name = dataSnapshot.child("name").value.toString()
                val imgProfile = dataSnapshot.child("imgProfile").value.toString()
                val imgBanner = dataSnapshot.child("imgBanner").value.toString()
                val uid = dataSnapshot.child("uid").value.toString()
                val userType = dataSnapshot.child("usertype").value.toString()


                val intent = Intent(origen, destino::class.java).apply {
                    putExtra("username",username)
                    putExtra("name",name)
                    putExtra("imgProfile",imgProfile)
                    putExtra("imgBanner",imgBanner)
                    putExtra("uid",uid)
                    putExtra("miperfil",miperfil)
                    putExtra("usertype",userType)
                }
                origen.startActivity(intent)
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}