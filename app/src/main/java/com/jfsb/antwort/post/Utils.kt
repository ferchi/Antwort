package com.jfsb.antwort.post

import android.app.AlertDialog
import android.content.Context

object Utils {
    fun showError(context: Context, message: String){
        AlertDialog.Builder(context).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("Aceptar",null)
        }.show()
    }
}