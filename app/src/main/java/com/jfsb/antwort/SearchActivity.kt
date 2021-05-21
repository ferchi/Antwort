package com.jfsb.antwort

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.jfsb.antwort.social.FriendAdapter
import com.jfsb.antwort.social.FriendCard
import com.jfsb.antwort.toolbar.ToolbarSearch
import kotlinx.android.synthetic.main.action_bar_toolbar.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private val friends: MutableList<FriendCard> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        ToolbarSearch().show(this,"Buscar",true)
        val layoutManager = LinearLayoutManager(this)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        rv_search.layoutManager = layoutManager

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_context,menu)
        val item: MenuItem? = menu?.findItem(R.id.search_item)
        val searchView:SearchView = MenuItemCompat.getActionView(item) as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)){
                    searchFriends(query)
                }else{

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!TextUtils.isEmpty(newText)){
                    searchFriends(newText)
                }else{

                }
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_item){
            Toast.makeText(this,"Si jala  opcion 1",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchFriends(searchQuery:String?){
        val ref:DatabaseReference = FirebaseDatabase.getInstance().reference
        friends.clear()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val children = snapshot!!.child("Users").children

                children.forEach{

                    val name = it.child("name").value.toString()
                    val username = it.child("username").value.toString()
                    val img = it.child("imgProfile").value.toString()
                    val id = it.child("uid").value.toString()

                    val modelFriendCard : FriendCard? = FriendCard(img,username,name,id)

                    if(modelFriendCard?.fullName?.toLowerCase()?.contains(searchQuery?.toLowerCase().toString()) == true
                        || modelFriendCard?.userName?.toLowerCase()?.contains(searchQuery?.toLowerCase().toString()) == true){

                        friends.add(modelFriendCard)
                    }

                    val friendAdapter = FriendAdapter(friends)
                    rv_search.adapter = friendAdapter
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}