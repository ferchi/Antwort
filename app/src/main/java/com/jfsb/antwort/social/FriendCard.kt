package com.jfsb.antwort.social

import com.google.firebase.database.Exclude

class FriendCard(val imageUri:String? = null, val userName: String? = null, val fullName: String? = null,val userId: String? = null) {
    @Exclude
    @set:Exclude
    @get:Exclude
    var uid: String? = null

    constructor(): this(null,null,null,null)
}