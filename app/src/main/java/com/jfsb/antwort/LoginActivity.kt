package com.jfsb.antwort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //Declarar variables de los componentes
    private lateinit var tv_registro: TextView
    private lateinit var btn_iniciar: Button
    private lateinit var et_usernameLog: EditText
    private lateinit var et_passwordLog: EditText

    private lateinit var txt_username:String
    private lateinit var txt_password:String

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        tv_registro = findViewById(R.id.tv_registro)
        btn_iniciar = findViewById(R.id.btn_iniciar)
        et_usernameLog = findViewById(R.id.et_usernameLog)
        et_passwordLog = findViewById(R.id.et_passwordLog)


        tv_registro.setOnClickListener {
            val intentRegistry = Intent(this, Registro::class.java).apply {}
            startActivity(intentRegistry)
        }

        btn_iniciar.setOnClickListener {

            txt_username = et_usernameLog.text.toString()
            txt_password = et_passwordLog.text.toString()

            if (txt_username.isNotEmpty() && txt_password.isNotEmpty()){
                logUser()
            }
            else{
                Toast.makeText(this,"Favor de ingresar todos los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logUser(){
        mAuth.signInWithEmailAndPassword(txt_username,txt_password).addOnCompleteListener(){ task ->
            if(task.isSuccessful){
                val intentProfile = Intent(this, MenuMain::class.java).apply {}
                startActivity(intentProfile)
                finish()
            } else{
                Toast.makeText(this,"Verificar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}