package com.jfsb.antwort

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.jfsb.antwort.perfilView.CircleImageViewBehavior
import de.hdodenhof.circleimageview.CircleImageView

class PerfilMain : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var collapsingtoolbar:CollapsingToolbarLayout
    lateinit var appbar:AppBarLayout
    lateinit var imageView: CircleImageView

    var colorCode:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ly_perfilmain)

        toolbar = findViewById(R.id.toolBarLayout)
        appbar = findViewById(R.id.appbarLayout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // your image view here
         imageView = findViewById(R.id.imageUser);

        val params = imageView.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = CircleImageViewBehavior()

    }
}