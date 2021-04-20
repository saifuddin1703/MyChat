package com.example.mychat.UI.bottomnavigationfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mychat.DAOs.chatdao
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.Adapters.ChatRecyclerViewAdapter
import com.example.mychat.UI.HomepageFragment
import com.example.mychat.model.User
import com.example.mychat.model.messsage
import com.google.firebase.auth.FirebaseAuth


class AllChatsFragment(val activity: HomepageFragment) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_chats_list, container, false)
        val userid=FirebaseAuth.getInstance().currentUser?.uid
       val chatdao=chatdao()
        val list=view.findViewById<RecyclerView>(R.id.list)

        chatdao.getallchats(userid!!).get().addOnSuccessListener {
            val userdao=userdao()
            val users=ArrayList<User>()
            val msglist=ArrayList<messsage>()

            for (document in it){
               val friendid= document.id
                Toast.makeText(requireContext(),"$friendid",Toast.LENGTH_SHORT).show()
                userdao.getuserbyid(friendid).addOnSuccessListener {
                  val user=  it.toObject(User::class.java)
                    Toast.makeText(requireContext(),"${user?.username}",Toast.LENGTH_SHORT).show()

                   chatdao.getlatestmessage(userid,friendid).addOnSuccessListener {
                       it.documents.get(0).toObject(messsage::class.java)?.let { it1 ->
                           msglist.add(
                               it1
                           )
                       }
                       users.add(user!!)
                       val adapter=ChatRecyclerViewAdapter(users,msglist!!,activity)
                       list.adapter=adapter
                   }


                }
            }
            Toast.makeText(requireContext(),"${users.size}",Toast.LENGTH_SHORT).show()

        }
        return view
    }


}