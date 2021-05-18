 package com.jfsb.antwort

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.jfsb.antwort.fragments.FriendsFragment
import com.jfsb.antwort.navigation.*
import com.jfsb.antwort.navigation.ClickListener

class MenuMain : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigation_rv: RecyclerView
    lateinit var navigation_header_img: ImageView
    lateinit var navigation_layout: LinearLayout

    private lateinit var adapter: NavigationRVAdapter

    private var items = arrayListOf(
            NavigationItemModel(R.drawable.ic_profile, "Perfil"),
            NavigationItemModel(R.drawable.ic_people_24, "Amigos"),
            NavigationItemModel(R.drawable.ic_music, "---"),
            NavigationItemModel(R.drawable.ic_movie, "---"),
            NavigationItemModel(R.drawable.ic_book, "---"),
            NavigationItemModel(R.drawable.ic_logout, "Salir"),
            NavigationItemModel(R.drawable.ic_social, "Facebook")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ly_menumain)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigation_rv = findViewById(R.id.navigation_rv)
        navigation_header_img = findViewById(R.id.navigation_header_img)
        navigation_layout = findViewById(R.id.navigation_layout)

        val intentExit = Intent(this, LoginActivity::class.java).apply {}
        // Set the toolbar
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        // Setup Recyclerview's Layout
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)
/*
        // Set Header Image
        navigation_header_img.setImageResource(R.drawable.logo)

        // Set background of Drawer
        navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))*/


        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        // # Profile Activity
                        val intent = Intent(this@MenuMain, PerfilMain::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        // # Friends Fragment
                        val friendsFragment = FriendsFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, friendsFragment).commit()
                    }
                    2 -> {
                        // # Music Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Music Fragment")
                        val musicFragment = DemoFragment()
                        musicFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, musicFragment).commit()
                    }
                    3 -> {
                        // # Movies Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Movies Fragment")
                        val moviesFragment = DemoFragment()
                        moviesFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, moviesFragment).commit()
                    }
                    4 -> {
                        // # Books Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Books Fragment")
                        val booksFragment = DemoFragment()
                        booksFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, booksFragment).commit()
                    }
                    5 -> {
                        mAuth.signOut()
                        startActivity(intentExit)
                        finish()
                    }
                    6 -> {
                        // # Open URL in browser
                        val uri: Uri = Uri.parse("https://fb.com/antwort")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                        finish()
                    }
                }
                // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                if (position != 6 && position != 4) {
                    updateAdapter(position)
                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))
        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
        updateAdapter(1)

        // Set 'Friends' as the default fragment when the app starts
        val friendsFragment = FriendsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id, friendsFragment).commit()


        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.activity_main_toolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        
        // Set Header Image
        navigation_header_img.setImageResource(R.drawable.logo)

        // Set background of Drawer
        navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Checking for fragment count on back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
            }
        }
    }
}