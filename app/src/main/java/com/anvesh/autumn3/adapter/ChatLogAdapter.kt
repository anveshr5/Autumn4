package com.anvesh.autumn3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth

class ChatLogAdapter(val message: String, val toUser: User, val fromUser: User): RecyclerView.Adapter<ChatLogAdapter.ChatLogViewHolder>() {
    class ChatLogViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatLogViewHolder {
        return if (fromUser.uid == FirebaseAuth.getInstance().currentUser?.uid) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chatrow_self, parent,false)

            ChatLogViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chatrow_friend, parent,false)

            ChatLogViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ChatLogViewHolder, position: Int) {

    }
}