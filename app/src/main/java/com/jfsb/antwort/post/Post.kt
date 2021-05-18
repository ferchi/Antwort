package com.jfsb.antwort.post;
import com.google.firebase.database.Exclude
import java.util.*;

class Post(val post:String? = null, val date: Date? = null, val userName: String? = null, val userId: String? = null, val likes: ArrayList<String>? = arrayListOf()){
    @Exclude
    @set:Exclude
    @get:Exclude
    var uid: String? = null

    constructor(): this(null,null,null,null,null)
}