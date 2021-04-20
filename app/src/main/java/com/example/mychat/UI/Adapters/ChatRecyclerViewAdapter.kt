package com.example.mychat.UI.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mychat.R
import com.example.mychat.UI.onfragenentclicklistener
import com.example.mychat.model.User
import com.example.mychat.model.messsage
import com.example.mychat.util


class ChatRecyclerViewAdapter(
    private val values: ArrayList<User>,
   private val msglist: ArrayList<messsage>,
    private val listner:onfragenentclicklistener
) : RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatlayout, parent, false)
        return ViewHolder(view)
    }
    fun updatelist(user: User){
        values.add(user)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=values[position]
        item.photourl?.let {
            Glide.with(holder.itemView.context).load(it).circleCrop().into(holder.profile)
        }
        holder.username.text=item.username
        holder.lastmessage.text= msglist[position].text
        holder.itemView.setOnClickListener {
            listner.onfragementclick(item.userid)
        }
        val time=util.gettime(msglist[position].sentAt)
        holder.time.text=time
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      val username:TextView=itemView.findViewById(R.id.textView2)
      val profile:ImageView=itemView.findViewById(R.id.imageView4)
      val lastmessage:TextView=itemView.findViewById(R.id.textView3)
      val time:TextView=itemView.findViewById(R.id.textView4)
    }
}