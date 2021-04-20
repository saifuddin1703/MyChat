package com.example.mychat.UI.bottomnavigationfragments.friendstab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.Adapters.FriendsSearchAdapter
import com.example.mychat.UI.HomepageFragment
import com.example.mychat.UI.onfragenentclicklistener
import com.example.mychat.model.User
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class FriendsFragment(val activity: HomepageFragment) : Fragment(), FriendsSearchAdapter.onclick,onfragenentclicklistener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
lateinit var searchView: SearchView
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val view=inflater.inflate(R.layout.fragment_friends, container, false)
        searchView=view.findViewById(R.id.searchView)
        recyclerView=view.findViewById(R.id.searchlistcontainer)

        val tabLayout:TabLayout= view.findViewById(R.id.tabLayout)
        val container:FrameLayout=view.findViewById(R.id.container)
        fragmentManager?.beginTransaction()?.replace(R.id.container,
            AllfriendsFragment(activity)
        )?.commit()
        val listener=this
        searchView.setOnCloseListener(object :SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                recyclerView.visibility=View.INVISIBLE
                return true
            }

        })
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
             val query=p0
              val list=  ArrayList<User>()
                val userdao=userdao()
                userdao.searchUsingQuery(query).addOnSuccessListener {
                    for (i in it){
                      val users= i.toObject(User::class.java)
                        list.add(users)

                    }
                    if (list.isEmpty()) Toast.makeText(context,"Nothing found",Toast.LENGTH_SHORT).show()
                    else{
                        Toast.makeText(context,"${list.size}",Toast.LENGTH_SHORT).show()
                        recyclerView.visibility=View.VISIBLE
                        recyclerView.adapter= FriendsSearchAdapter(list,listener,activity)
                    }
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
               return true
            }

        })

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                 val title=tab?.text
                if (title==getString(R.string.all_friends)){
                    fragmentManager?.beginTransaction()?.replace(R.id.container,
                        AllfriendsFragment(activity)
                    )?.commit()
                }
                if (title== getString(R.string.requests)){
                    fragmentManager?.beginTransaction()?.replace(R.id.container,
                        FriendRequestFragment()
                    )?.commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        return view
    }

    override fun click(friendid: String) {
         val userdao=userdao()
        val userid=FirebaseAuth.getInstance().currentUser?.uid
         userdao.sendrequest(userid!!,friendid)
    }

    override fun onfragementclick(user: String?) {
    val navController=   this.findNavController()

    }


}