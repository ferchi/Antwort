package com.jfsb.antwort.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jfsb.antwort.R
import org.w3c.dom.Text

class DemoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_demo, container, false)

        val fragmentName = arguments?.getString("fragmentName")

       // rootView.fragment_name.text = fragmentName

        rootView.findViewById<TextView>(R.id.fragment_name).text = fragmentName

        return rootView
    }
}