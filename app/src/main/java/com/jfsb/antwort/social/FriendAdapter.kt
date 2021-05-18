package com.jfsb.antwort.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.antwort.R
import com.jfsb.antwort.post.Post
import com.jfsb.antwort.post.PostAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_friend.view.*
import kotlinx.android.synthetic.main.card_post.view.*
import kotlinx.android.synthetic.main.ly_perfilmain.*

class FriendAdapter (private val fragment: Fragment, private val dataset: List<FriendCard>):RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    class ViewHolder (val layout: View) : RecyclerView.ViewHolder(layout)

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
    }

}