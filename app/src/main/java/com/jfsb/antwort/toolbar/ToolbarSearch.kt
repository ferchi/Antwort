package com.jfsb.antwort.toolbar

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.action_bar_toolbar.*

class ToolbarSearch {
    fun show(activities:AppCompatActivity, title:String, upButton:Boolean){
        activities.setSupportActionBar(activities.toolbarSearch)
        activities.supportActionBar?.title = title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }
}