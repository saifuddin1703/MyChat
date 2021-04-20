package com.example.mychat.DAOs

import com.example.mychat.model.messsage
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class chatdao {
   private val db= FirebaseFirestore.getInstance()
   private val usercollection= db.collection("users")

    fun getallchats(userid: String): CollectionReference {
       return usercollection.document(userid).collection("chats")
    }

    fun getlatestmessage(userid: String, friendid: String): Task<QuerySnapshot> {
       return usercollection.document(userid).collection("chats")         //user's chats collection
            .document(friendid).collection("messages")                //user's message collection of friendid document
            .orderBy("sentAt",Query.Direction.DESCENDING).get()
    }

    fun getallmessages(userid: String, friendid: String): FirestoreRecyclerOptions<messsage> {
        val query=usercollection.document(userid).collection("chats")         //user's chats collection
            .document(friendid).collection("messages")                //user's message collection of friendid document
            .orderBy("sentAt",Query.Direction.ASCENDING)

        return FirestoreRecyclerOptions.Builder<messsage>()
            .setQuery(query, messsage::class.java)
                .build()
    }
    fun sendmessage(sender:String,friend:String,messsage: messsage): Task<Void> {
        // going in the chats collection of sender and the document of id of the friend ,
        // and in the document in the messages collection of the document we adding the message object


        // going in the chats collection of friend and the document of id of the sender ,
        // and in the document in the messages collection of the document we adding the message object
        usercollection.document(friend)
            .collection("chats")                //friend's chats collection
            .document(sender)
            .collection("messages")                      //friend message collection of senderid document
            .document().set(messsage)


       return usercollection.document(sender).collection("chats")         //sender chats collection
               .document(friend)
               .collection("messages")                //sender message collection of friendid document
               .document().set(messsage)



    }

    fun initiatechat(userid: String,freindid:String){
         val data=HashMap<String,String>()
        data.put("fata","fa")
       // usercollection.document(userid).collection("chats").document().set(data)
                usercollection.document(userid).collection("chats").document(freindid).get().addOnSuccessListener {
                    if(!it.exists()){
                        usercollection.document(userid).collection("chats").document(freindid).set(data)
                    }
                }

        usercollection.document(freindid).collection("chats").document(userid).get().addOnSuccessListener {
            if(!it.exists()){
                usercollection.document(userid).collection("chats").document(freindid).set(data)
            }
        }

    }

}