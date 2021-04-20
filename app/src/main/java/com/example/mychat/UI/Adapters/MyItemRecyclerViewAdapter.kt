package com.example.mychat.UI.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mychat.DAOs.userdao
import com.example.mychat.R
import com.example.mychat.UI.onfragenentclicklistener


import com.example.mychat.model.User
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MyItemRecyclerViewAdapter(
    private val values: List<String>,
    private val listener:onfragenentclicklistener
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_allfriends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userdao= userdao()

            val id=values[position]
         userdao.getuserbyid(id).addOnSuccessListener {
            val item=it.toObject(User::class.java)
              item?.photourl?.let {
                 Glide.with(holder.itemView.context).load(it).circleCrop().into(holder.profileimage)
             }

             holder.username.text = item?.username

             holder.button.setOnClickListener {

                 listener.onfragementclick(item?.userid)
             }
         }


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileimage: ImageView = view.findViewById(R.id.imageView4)
        val username: TextView = view.findViewById(R.id.textView2)
        val button: MaterialButton = view.findViewById(R.id.button)
    }
}