package com.example.mychat.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mychat.DAOs.chatdao
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.Adapters.AllmessagesAdapter
import com.example.mychat.model.User
import com.example.mychat.model.messsage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class ChatFragement : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
 private lateinit var chats: RecyclerView
    private lateinit var send:FloatingActionButton
    private lateinit var messagebox:EditText
    private lateinit var username:TextView
    private lateinit var profilepic:ImageView
    private lateinit var adapter:AllmessagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_chat_fragement, container, false)

        chats=view.findViewById(R.id.messages)
        send=view.findViewById(R.id.floatingActionButton)
        messagebox=view.findViewById(R.id.textInputEditText)
        username=view.findViewById(R.id.textView7)
        profilepic=view.findViewById(R.id.imageView5)
        val chatdao=chatdao()
         val friend=ChatFragementArgs.fromBundle(requireArguments()).userid
       val userdao= userdao()
        userdao.getuserbyid(friend!!).addOnSuccessListener {
            val friendo=it.toObject(User::class.java)
            friendo?.photourl?.let {
                Glide.with(requireContext()).load(it).circleCrop().into(profilepic)

            }
            username.text=friendo?.username
        }

        val userid=FirebaseAuth.getInstance().currentUser?.uid
        val messsagelist=ArrayList<messsage>()
        chatdao.initiatechat(userid!!,friend)
        val options= chatdao.getallmessages(userid!!, friend)
        adapter= AllmessagesAdapter(options)

        chats.adapter=adapter
        send.setOnClickListener{
            val messagetext=messagebox.text.toString()

            Toast.makeText(requireContext(), messagetext, Toast.LENGTH_LONG).show()
            if (!messagetext?.isEmpty()!!){
                val msg:messsage= messsage(
                    userid!!,
                    friend,
                    System.currentTimeMillis(),
                    messagetext
                )
                chatdao.sendmessage(userid, friend.trim(), msg).addOnSuccessListener {

                    chats.scrollToPosition((adapter.itemCount-1))
                    Toast.makeText(requireContext(), "${adapter.itemCount}", Toast.LENGTH_SHORT).show()
                }
                messagebox.setText("")



            }
        }

        return view
    }
     override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
     override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}