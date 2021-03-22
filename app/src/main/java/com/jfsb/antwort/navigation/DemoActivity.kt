package com.jfsb.antwort.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jfsb.antwort.R


class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        val activityName = intent.getStringExtra("activityName")
        val activity_name: TextView = findViewById(R.id.activity_name)

        activity_name.text = activityName
    }
}