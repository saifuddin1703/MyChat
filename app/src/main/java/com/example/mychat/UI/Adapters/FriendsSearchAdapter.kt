package com.example.mychat.UI.Adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.HomepageFragment
import com.example.mychat.UI.onfragenentclicklistener
import com.example.mychat.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth


class FriendsSearchAdapter(
    private val values: List<User>,
    val listener: onclick,
    private val activity: onfragenentclicklistener
) : RecyclerView.Adapter<FriendsSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_friends_search_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        item.photourl?.let {
            Glide.with(holder.itemView.context).load(it).circleCrop().into(holder.imageView)
        }
        holder.textView.text=item.username
         val userdao=userdao()
       // holder.button.setBackgroundColor(R.color.mytheme)

          val user=  userdao.getuserbyid(FirebaseAuth.getInstance().currentUser?.uid!!).addOnSuccessListener {
              val user=it.toObject(User::class.java)
              if (user?.friends?.contains(item.userid)!!){
                  holder.button.text="message"
              }else if (user.userid==item.userid){
                  holder.button.visibility=View.INVISIBLE
              }
              holder.button.setOnClickListener {
                  if (holder.button.text=="message"){
                      activity.onfragementclick(user.userid)
                  }
                  holder.button.backgroundTintList= ColorStateList.valueOf(R.color.grey)
                  holder.button.text="sent"
                  holder.button.isClickable=false
                  listener.click(item.userid)
              }
          }


    }

    override fun getItemCount(): Int = values.size


    interface onclick{
        fun click(friendid:String)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       val imageView:ImageView=view.findViewById(R.id.imageView4)
       val textView:TextView=view.findViewById(R.id.textView2)
       val button:MaterialButton=view.findViewById(R.id.button)
}}