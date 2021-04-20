package com.example.mychat.UI.bottomnavigationfragments.friendstab

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.Adapters.MyItemRecyclerViewAdapter
import com.example.mychat.UI.HomepageFragment
import com.example.mychat.model.User
import com.google.firebase.auth.FirebaseAuth


/**
 * A fragment representing a list of Items.
 */
class AllfriendsFragment(private val activity: HomepageFragment): Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_allfriends_list, container, false)
         Toast.makeText(context,"all friends",Toast.LENGTH_SHORT).show()
        recyclerView=view.findViewById(R.id.list)
      val userid=FirebaseAuth.getInstance().currentUser?.uid
        val userdao=userdao()
        userdao.getuserbyid(userid!!).addOnSuccessListener {
            val user=it.toObject(User::class.java)
            val friends=user?.friends
           recyclerView.adapter= MyItemRecyclerViewAdapter(friends!!,activity)
        }
        return view
    }


}