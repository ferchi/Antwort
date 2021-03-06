package com.jfsb.antwort

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jfsb.antwort.fragments.ConsultasFragment
import com.jfsb.antwort.fragments.RecientesFragment
import com.jfsb.antwort.fragments.RespuestasFragment
import com.jfsb.antwort.fragments.adapters.ViewPagerAdapter
import com.jfsb.antwort.perfilView.CircleImageViewBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_friend.view.*
import kotlinx.android.synthetic.main.ly_perfilmain.*
import java.io.ByteArrayOutputStream

class PerfilMainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var appbar: AppBarLayout

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userDB_ref: DatabaseReference

    val TAKE_IMG_CODE = 1046
    lateinit var vista: View
    lateinit var storageChild: String
    lateinit var databaseChild: String

    lateinit var nameS: String
    lateinit var usernameS: String
    lateinit var imgProfileS: String
    lateinit var imgBannerS: String
    var miPerfil: Boolean = false
    lateinit var uidS: String
    lateinit var userType: String

    var clickFollow : Boolean = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ly_perfilmain)

        nameS = intent.getStringExtra("name").toString()
        usernameS = intent.getStringExtra("username").toString()
        uidS = intent.getStringExtra("uid").toString()
        imgProfileS = intent.getStringExtra("imgProfile").toString()
        imgBannerS = intent.getStringExtra("imgBanner").toString()
        miPerfil = intent.getBooleanExtra("miperfil", false)
        userType = intent.getStringExtra("usertype").toString()





        //Instancia de los elementos dentro del layout
        toolbar = findViewById(R.id.toolBarLayout)
        appbar = findViewById(R.id.appbarLayout)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        userDB_ref = FirebaseDatabase.getInstance().reference

        fullname_tv.text = nameS
        username_tv.text = usernameS

        //Implementacion de las propiedades del FAB a un CircleImage
        val params = imageUser.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = CircleImageViewBehavior()

        //Metodo para habilitar el uso de Tabs dentro del Layout
        setUpTabs()

        loadImg()
        isFriend()

        if (!miPerfil) {
            username_tv.setOnClickListener {
                if(!clickFollow){
                    Log.d("seguir", "a??adir")
                    addFriend()
                    clickFollow = !clickFollow
                } else {
                    Log.d("seguir", "eliminar")
                    removeFriend()
                    clickFollow = !clickFollow
                }
            }
        }
        else{
            imageUser.setOnLongClickListener {
                changeImg(it)
            }
            imageBanner.setOnLongClickListener {
                changeImg(it)
            }
        }

        if(userType == "maestro"){
            Picasso.get().load(R.drawable.ic_teacher_24).into(usertypeImg)
        }
        else{
            Picasso.get().load(R.drawable.ic_student_24).into(usertypeImg)
        }
    }

    override fun onBackPressed() {
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.drawable.ic_book -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RecientesFragment(), "Recientes")
        adapter.addFragment(ConsultasFragment(), "Consultas")
        adapter.addFragment(RespuestasFragment(), "Respuestas")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_new_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_hearing_24)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_question_24)
    }

    fun loadImg() {
        userDB_ref.child("Users").child(uidS).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val urlImg = dataSnapshot.child("imgProfile").value.toString()
                val urlImgB = dataSnapshot.child("imgBanner").value.toString()
                try {
                    Picasso.get().load(urlImg).into(imageUser)
                    Picasso.get().load(urlImgB).into(imageBanner)
                } catch (e: Exception) {
                    Picasso.get().load(R.drawable.woman).into(imageUser)
                    Picasso.get().load(R.drawable.campo).into(imageBanner)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun changeImg(view: View): Boolean {
        when (view.id) {
            imageUser.id -> {
                vista = imageUser
                databaseChild = "imgProfile"
                storageChild = "profileImages"
            }
            imageBanner.id -> {
                vista = imageBanner
                databaseChild = "imgBanner"
                storageChild = "bannerImages"
            }
        }

        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, TAKE_IMG_CODE)
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMG_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
                    if (vista.id == imageUser.id) {
                        imageUser.setImageBitmap(bitmap)
                    } else {
                        imageBanner.setImageBitmap(bitmap)
                    }
                    handleUpload(bitmap)
                }
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uid: String = uidS
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child(storageChild)
            .child("$uid.jpeg")

        ref.putBytes(baos.toByteArray())
            .addOnSuccessListener {
                getDownloadUrl(ref)
            }
            .addOnFailureListener() {
                Log.e("Errorimg", "onFailure", it.cause)
            }
    }

    private fun getDownloadUrl(ref: StorageReference) {
        ref.downloadUrl.addOnSuccessListener {
            setUserProfileUrl(it)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user: FirebaseUser = FirebaseAuth.getInstance().currentUser

        val request: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setPhotoUri(uri)
            .build()

        user.updateProfile(request)
            .addOnSuccessListener {
                userDB_ref.child("Users").child(uidS).child(databaseChild).setValue(uri.toString())
                loadImg()
                Toast.makeText(this, "Actualizaci??n exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Actualizaci??n fallida", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addFriend() {
        userDB_ref.child("Users").child(mAuth.currentUser.uid).child("friends").child(uidS).setValue(uidS)
    }
    private fun removeFriend() {
        userDB_ref.child("Users").child(mAuth.currentUser.uid).child("friends").child(uidS).removeValue()
    }

    private fun isFriend(){
        userDB_ref.child("Users").child(mAuth.currentUser.uid).child("friends").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.hasChild(uidS)){
                    username_tv.background = resources.getDrawable(R.drawable.rounded_corners_or)
                    clickFollow = true
                }else{
                    username_tv.background = resources.getDrawable(R.drawable.rounded_corners_bl)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}