package com.example.mychat.DAOs

import com.example.mychat.model.FriendRequest
import com.example.mychat.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class userdao {
   private val db= FirebaseFirestore.getInstance()
   private val usercollection= db.collection("users")

    fun adduser(user:User){
      usercollection.document(user.userid).get().addOnCompleteListener {
          val task= it.result
          task?.let {
              if (!it.exists())
                  usercollection.document(user.userid).set(user)
          }

     }
    }

    //method to send request
    fun sendrequest(userid:String,friendid:String){
        GlobalScope.launch {
            val user=  getuserbyid(userid).await().toObject(User::class.java)
            val friend=  getuserbyid(friendid).await().toObject(User::class.java)
             val request=FriendRequest(userid,friendid,System.currentTimeMillis())
            user?.requestsent?.add(request)
            friend?.requestrecieved?.add(request)
            usercollection.document(userid).set(user!!)
            usercollection.document(friendid).set(friend!!)
        }
    }


    // method to accept the friend requests
    fun acceptrequest(request: FriendRequest){
        GlobalScope.launch {
            // getting the user who recieved the request
          val user=  getuserbyid(request.sentTo).await().toObject(User::class.java)
            // getting the user who sent the request i.e friend
          val friend=  getuserbyid(request.createdBy).await().toObject(User::class.java)
            //removing the request from the request list of user who received the request
            user?.requestrecieved?.remove(request)
            //removing the request from the request list of user who sent the request
            friend?.requestsent?.remove(request)
            // adding the friend to the user's friend list
            val userfriendlist=user?.friends
            // adding the user to the friend's friend list
            val friendsfriendlist=friend?.friends
           userfriendlist?.add(request.createdBy)
            friendsfriendlist?.add(request.sentTo)

            usercollection.document(request.sentTo).set(user!!)
            usercollection.document(request.createdBy).set(friend!!)

        }

    }

    fun deletereceivedrequest(friendRequest: FriendRequest){
        GlobalScope.launch {
            // getting the user who received the request
            val user=  getuserbyid(friendRequest.sentTo).await().toObject(User::class.java)
            user?.requestrecieved?.remove(friendRequest)
            usercollection.document(friendRequest.sentTo).set(user!!)
        }
    }
    fun isUserExists(userid: String): Task<DocumentSnapshot> {
        var ans=false
        return usercollection.document(userid).get()
        //return ans
    }

    fun getuserbyid(userid:String): Task<DocumentSnapshot> {
        return usercollection.document(userid).get()
    }

    // method for searching a user with his/her name in the database
    fun searchUsingQuery(query:String?): Task<QuerySnapshot> {
       query?.get(0)?.toUpperCase()
        val firestorequery= usercollection.whereEqualTo("username",query)
      return  firestorequery.get()
    }
// method to update the details of the user in the database
    fun updateuser(user:User){
        usercollection.document(user.userid).set(user)
    }

}