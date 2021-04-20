package com.example.mychat.UI.Adapters

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
import com.example.mychat.model.FriendRequest
import com.example.mychat.model.User
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class FriendRequestRecyclerViewAdapter(
    private val values: List<FriendRequest>
) : RecyclerView.Adapter<FriendRequestRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_friendrequest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val userdao= userdao()

          userdao.getuserbyid(item.createdBy).addOnSuccessListener {
              val tobefriend= it.toObject(User::class.java)
              tobefriend?.photourl?.let {
                  Glide.with(holder.itemView.context).load(it).circleCrop().into(holder.imageView)
              }
              holder.textView.text=tobefriend?.username
          }


       holder.button.setOnClickListener {
           userdao.acceptrequest(item)
           holder.button.text="message"
           holder.dbutton.visibility=View.INVISIBLE
       }
        holder.dbutton.setOnClickListener {
            userdao.deletereceivedrequest(item)

        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView =view.findViewById(R.id.imageView4)
        val textView:TextView=view.findViewById(R.id.textView2)
        val button: MaterialButton =view.findViewById(R.id.button)
        val dbutton: MaterialButton =view.findViewById(R.id.declinebutton)

    }
}