package com.jfsb.antwort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Registro : AppCompatActivity() {

    //Declarar variables de los componentes
    lateinit var usernameIn: EditText
    lateinit var emailIn: EditText
    lateinit var passwordIn: EditText
    lateinit var btnRegistro: Button

    //Declarar variables que almacenaran el texto de los EditText
    var txtname:String = ""
    var txtemail:String = ""
    var txtpassword:String = ""

    //Declarar el objeto que será la instancia de la base de datos
    lateinit var oAuth:FirebaseAuth
    lateinit var uDatabase:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_registro)

        //Instanciar la base de datos
        oAuth = FirebaseAuth.getInstance()
        uDatabase = FirebaseDatabase.getInstance().reference

        //Instanciar los componentes del activity0
        usernameIn = findViewById<EditText>(R.id.usernameIn)
        emailIn = findViewById<EditText>(R.id.emailIn)
        passwordIn = findViewById<EditText>(R.id.passwordIn)
        btnRegistro = findViewById<Button>(R.id.btn_registro)

        btnRegistro.setOnClickListener {

            txtname = usernameIn.text.toString()
            txtemail = emailIn.text.toString()
            txtpassword = passwordIn.text.toString()

            if(txtname.isNotEmpty()&&txtemail.isNotEmpty()&&txtpassword.isNotEmpty()){
                if (txtpassword.length >= 6) {
                    registrarUsuario()
                }
                else {
                    Toast.makeText(applicationContext,"El password debe ser de mínimo seis caracteres",Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(applicationContext,"Favor de llenar todos los datos",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario() {
        oAuth.createUserWithEmailAndPassword(txtemail,txtpassword).addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {
                var map:HashMap<String, String> = HashMap<String, String>()
                map["name"] = txtname
                map["email"] = txtemail
                map["password"] = txtpassword

                val id = oAuth.currentUser.uid

                Toast.makeText(applicationContext,"Registro completo",Toast.LENGTH_SHORT).show()
                val intentRegistry = Intent(this, MainActivity::class.java).apply {}
                startActivity(intentRegistry)

                uDatabase.child("Users").child(id).setValue(map)


            } else {
                Toast.makeText(applicationContext,"No se pudo registrar este usuario 2",Toast.LENGTH_SHORT).show()
            }

        }
    }


}