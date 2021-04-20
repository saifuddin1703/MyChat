package com.example.mychat.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mychat.DAOs.chatdao
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.bottomnavigationfragments.AllChatsFragment
import com.example.mychat.UI.bottomnavigationfragments.ProfileFragement
import com.example.mychat.UI.bottomnavigationfragments.friendstab.FriendsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomepageFragment : Fragment(),onfragenentclicklistener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      val uid=FirebaseAuth.getInstance().currentUser?.uid
        val userdao=userdao()
        val view= inflater.inflate(R.layout.fragment_homepage, container, false)
       val bottomNavigationItemView:BottomNavigationView=view.findViewById(R.id.bottomNavigationView2)
        fragmentManager?.beginTransaction()?.replace(
            R.id.homepagecontainer,
            AllChatsFragment(this)
        )?.commit()
        bottomNavigationItemView.setOnNavigationItemSelectedListener { item ->
            val title = item.title
            if (title == "Chats") {
                fragmentManager?.beginTransaction()?.replace(
                    R.id.homepagecontainer,
                    AllChatsFragment(this)
                )?.commit()
                Toast.makeText(context,title,Toast.LENGTH_SHORT).show()
            }
            if (title == "Friends") {
                fragmentManager?.beginTransaction()?.replace(
                    R.id.homepagecontainer,
                    FriendsFragment(this)
                )?.commit()
                Toast.makeText(context,title,Toast.LENGTH_SHORT).show()
            }
            if (title=="Profile")
                fragmentManager?.beginTransaction()?.replace(R.id.homepagecontainer,
                    ProfileFragement()
                )?.commit()
            true
        }
        return view
    }

    override fun onfragementclick(user: String?) {
      val action=  HomepageFragmentDirections.actionHomepageFragmentToChatFragement(user)
        val navController=this.findNavController()
        if (action != null) {
            navController.navigate(action)
        }
        val chatdao=chatdao()
        val userid=FirebaseAuth.getInstance().currentUser?.uid
     //   chatdao.initiatechat(userid!!,user.userid)
    }


}