package com.anvesh.autumn3.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.ChatLogActivity
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.fragments.ChatSectionFragment
import com.anvesh.autumn3.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NewMessageAdapter(
    val context: Context,
    val userList: List<User?>
) : RecyclerView.Adapter<NewMessageAdapter.NewMessageViewHolder>() {
    class NewMessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgProfilePhoto: CircleImageView = view.findViewById(R.id.imgProfilePhoto)
        val txtUsername: TextView = view.findViewById(R.id.txtUsername)
        val rlNewMessage: RelativeLayout = view.findViewById(R.id.rlNewMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMessageViewHolder {
        val userView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_select_user_layout, parent,false)

        return NewMessageViewHolder(userView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: NewMessageViewHolder, position: Int) {
        val newUser = userList[position]
        holder.txtUsername.text = newUser?.username
        Picasso.get().load(newUser?.profileImageUrl).into(holder.imgProfilePhoto)

        holder.rlNewMessage.setOnClickListener {
            if (newUser?.uid != MainActivity.currentUser?.uid) {
                val intent = Intent(context as Activity, ChatLogActivity::class.java)
                intent.putExtra("ToUser", newUser)
                context.startActivity(intent)
                context.finish()
            }
        }
    }
}