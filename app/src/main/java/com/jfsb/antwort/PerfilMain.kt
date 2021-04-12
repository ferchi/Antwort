package com.jfsb.antwort

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.jfsb.antwort.fragments.ConsultasFragment
import com.jfsb.antwort.fragments.RecientesFragment
import com.jfsb.antwort.fragments.RespuestasFragment
import com.jfsb.antwort.fragments.adapters.ViewPagerAdapter
import com.jfsb.antwort.perfilView.CircleImageViewBehavior
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.ly_perfilmain.*

class PerfilMain : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var collapsingtoolbar:CollapsingToolbarLayout
    lateinit var appbar:AppBarLayout
    lateinit var imageView: CircleImageView

    var colorCode:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ly_perfilmain)

        //Instancia de los elementos dentro del layout
        toolbar = findViewById(R.id.toolBarLayout)
        appbar = findViewById(R.id.appbarLayout)
        imageView = findViewById(R.id.imageUser);

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Implementacion de las propiedades del FAB a un CircleImage
        val params = imageView.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = CircleImageViewBehavior()

        //Metodo para habilitar el uso de Tabs dentro del Layout
        setUpTabs()

    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RecientesFragment(),"Recientes")
        adapter.addFragment(ConsultasFragment(),"Consultas")
        adapter.addFragment(RespuestasFragment(),"Respuestas")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_new_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_hearing_24)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_question_24)
    }
}