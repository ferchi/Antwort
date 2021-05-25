package com.jfsb.antwort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.ly_registro.*

class RegistroActivity : AppCompatActivity() {

    //Declarar variables que almacenarán el texto de los EditText
    var txtusername:String = ""
    var txtemail:String = ""
    var txtpassword:String = ""
    var txtname:String = ""
    var txtusertype:String = ""

    //Declarar el objeto que será la instancia de la base de datos
    lateinit var oAuth:FirebaseAuth
    lateinit var userDB_ref:DatabaseReference
    lateinit var userDB:FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ly_registro)

        //Instanciar la base de datos
        oAuth = FirebaseAuth.getInstance()
        userDB = FirebaseDatabase.getInstance()
        userDB_ref = userDB.getReference("Users")

        setSupportActionBar(toolbar_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "REGISTRO"

        usertypeIn_sw.setOnCheckedChangeListener{_,isChecked ->
            if (isChecked){
                txtusertype = "maestro"
            }
            else{
                txtusertype = "estudiante"
            }
        }

        btn_registro.setOnClickListener {

            txtusername = usernameIn.text.toString()
            txtemail = emailIn.text.toString()
            txtpassword = passwordIn.text.toString()
            txtname = nameIn.text.toString()

            if(txtusername.isNotEmpty()&&txtemail.isNotEmpty()&&txtpassword.isNotEmpty()){
                if (txtpassword.length >= 6) {
                    oAuth.createUserWithEmailAndPassword(txtemail,txtpassword).addOnCompleteListener(this){ task ->
                        if (task.isSuccessful) {
                            val id = oAuth.currentUser.uid

                            //Mapeo de los datos del usuario
                            var map:HashMap<String, String> = HashMap()

                            map["uid"] = id
                            map["name"] = txtname
                            map["username"] = txtusername
                            map["email"] = txtemail
                            map["password"] = txtpassword
                            map["imgProfile"] = ""
                            map["imgBanner"] = ""
                            map["usertype"] = txtusertype

                            userDB_ref.child(id).setValue(map)


                            val profile = UserProfileChangeRequest.Builder()
                                .setDisplayName(txtusername)
                                .build()

                            task.addOnSuccessListener {
                                it.user!!.updateProfile(profile)
                            }

                            Toast.makeText(applicationContext,"Registro completo",Toast.LENGTH_SHORT).show()
                            val intentRegistry = Intent(this, LoginActivity::class.java).apply {}
                            startActivity(intentRegistry)
                            finish()

                        } else {
                            Toast.makeText(applicationContext,"No se pudo registrar este usuario "+task.result,Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                else {
                    Toast.makeText(applicationContext,"La contraseña debe ser de mínimo seis caracteres",Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(applicationContext,"Favor de llenar todos los datos",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
    }

}