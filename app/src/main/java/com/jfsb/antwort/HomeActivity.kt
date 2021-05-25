package com.jfsb.antwort

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwort.databinding.ActivityHomeBinding
import com.jfsb.antwort.post.CreateActivity
import com.jfsb.antwort.post.Post
import com.jfsb.antwort.post.PostAdapter
import com.jfsb.antwort.post.PostAdapter_Act
import com.jfsb.antwort.social.FriendAdapter
import com.jfsb.antwort.social.FriendCard
import com.jfsb.antwort.toolbar.ToolbarSearch
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_search.*

class HomeActivity : AppCompatActivity() {
    private val posts: MutableList<Post> = mutableListOf()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        ToolbarSearch().show(this,"Buscar",true)
        val layoutManager = LinearLayoutManager(this)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        rv_home.layoutManager = layoutManager


        fab_home.setOnClickListener { view ->
            Snackbar.make(view, "Escribe tu consulta", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val intent = Intent (this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_context,menu)
        val item: MenuItem? = menu?.findItem(R.id.search_item)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)){
                    searchPosts(query)
                }else{

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!TextUtils.isEmpty(newText)){
                    searchPosts(newText)
                }else{

                }
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun searchPosts(searchQuery:String?){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
        posts.clear()

        db.collection("post").addSnapshotListener{value, error ->
            val posts = value!!.toObjects(Post::class.java)
            val postsAux: MutableList<Post> = mutableListOf()

            posts.forEachIndexed{ index, post ->
                post.uid = value.documents[index].id

                val modelPost: Post? = Post(post.post, post.date, post.userName, post.userId, post.likes)
                if(modelPost?.userName?.toLowerCase()?.contains(searchQuery?.toLowerCase().toString()) == true
                    || modelPost?.post?.toLowerCase()?.contains(searchQuery?.toLowerCase().toString()) == true){

                    postsAux.add(modelPost)
                }
            }

            //posts.add(2,Post("Texto donde se explica la duda que se tiene", Date(),"Username"))

            rv_home.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostAdapter_Act(this@HomeActivity,postsAux)
            }
        }


    }

}