package com.example.mychat.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat.R
import com.example.mychat.model.messsage
import com.example.mychat.util
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class AllmessagesAdapter(options: FirestoreRecyclerOptions<messsage>):FirestoreRecyclerAdapter<messsage,AllmessagesAdapter.viewholder>(
    options
) {

    inner class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val messagesend:TextView=itemView.findViewById(R.id.textView8)
        val messagerecieved:TextView=itemView.findViewById(R.id.textView5)
        val messagesendtime:TextView=itemView.findViewById(R.id.textView9)
        val messagerecievedtime:TextView=itemView.findViewById(R.id.textView10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.message_layout,parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int, model: messsage) {
        val userid=FirebaseAuth.getInstance().currentUser?.uid
        val time= util.gettime(model.sentAt)

        val item=model
        if (item.sentby==userid){
            holder.messagesendtime.visibility=View.VISIBLE
            holder.messagesend.visibility=View.VISIBLE
            holder.messagesend.text=item.text+"\n"
            holder.messagesendtime.text=time
            holder.messagerecievedtime.visibility=View.INVISIBLE
            holder.messagerecieved.visibility=View.INVISIBLE
        }else{
            holder.messagerecieved.visibility=View.VISIBLE
            holder.messagerecievedtime.visibility=View.VISIBLE
            holder.messagerecieved.text=item.text+"\n"
            holder.messagerecievedtime.text=time
            holder.messagesendtime.visibility=View.INVISIBLE
            holder.messagesend.visibility=View.INVISIBLE
        }
    }
}