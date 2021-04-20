package com.example.mychat.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class User(
    var username: String ="",
    val userid:String="",
    var photourl:String?=null,
    val friends:ArrayList<String>?=null,
    val requestrecieved:ArrayList<FriendRequest>?=null,
    val requestsent:ArrayList<FriendRequest>?=null,
     val usertoken:String?="")