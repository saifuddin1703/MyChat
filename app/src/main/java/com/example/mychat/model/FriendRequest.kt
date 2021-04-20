package com.example.mychat.model

data class FriendRequest (
    val createdBy:String="",
    val sentTo:String="",
    val createdAt:Long=0L
)