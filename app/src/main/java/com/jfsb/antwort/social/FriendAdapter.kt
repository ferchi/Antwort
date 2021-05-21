package com.jfsb.antwort.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jfsb.antwort.PerfilMain
import com.jfsb.antwort.R
import com.jfsb.antwort.post.Utils.openProfile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_friend.view.*

class FriendAdapter (private val dataset: List<FriendCard>):RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    class ViewHolder (val layout: View) : RecyclerView.ViewHolder(layout)
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.card_friend,parent,false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend =  dataset[position]
        holder.layout.nameCard.text = friend.fullName
        holder.layout.usernameCard.text = friend.userName
        try {
            Picasso.get().load(friend.imageUri).into(holder.layout.imageCard)
        }catch (e:Exception){
            Picasso.get().load(R.drawable.woman).into(holder.layout.imageCard)
        }

        holder.layout.imageCard.setOnClickListener{
            openProfile(FirebaseDatabase.getInstance().reference,"Users",friend.userId.toString(),it.context, PerfilMain(),false)
        }

        holder.layout.menu_card_icon.setOnClickListener{
            val popupMenu = PopupMenu(it.context,it)
            popupMenu.inflate(R.menu.menu_card_friend)
            popupMenu.show()
        }
    }


}