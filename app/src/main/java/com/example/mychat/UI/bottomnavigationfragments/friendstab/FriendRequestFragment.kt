package com.example.mychat.UI.bottomnavigationfragments.friendstab

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.Adapters.FriendRequestRecyclerViewAdapter
import com.example.mychat.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


/**
 * A fragment representing a list of Items.
 */
class FriendRequestFragment : Fragment() {

   // private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context,"all requests",Toast.LENGTH_SHORT).show()

        val view = inflater.inflate(R.layout.fragment_friend_request_list, container, false)
           recyclerView=view.findViewById(R.id.list)
         val userid=FirebaseAuth.getInstance().currentUser?.uid
        val userdao= userdao()


            userdao.getuserbyid(userid!!).addOnSuccessListener {
                val user=    it.toObject(User::class.java)
                val requests=user?.requestrecieved
                Toast.makeText(context,"${requests?.size}",Toast.LENGTH_SHORT).show()
                recyclerView.adapter=FriendRequestRecyclerViewAdapter(requests!!)
            }




        return view
    }


}