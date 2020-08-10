package com.anvesh.autumn3.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.ChatLogActivity
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.fragments.ChatSectionFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_latest_message_row.view.*

class LatestMessageRow(val context: Context,val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {

    var user: User? = null

    override fun getLayout(): Int {
        return R.layout.recyclerview_latest_message_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int){


        val friendId = if (chatMessage.fromId == MainActivity.currentUser?.uid){
            chatMessage.toId
        } else {
            chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$friendId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java) ?: return

                viewHolder.itemView.txtUsername.text = user!!.username
                if (chatMessage.toId == MainActivity.currentUser?.uid) {
                    viewHolder.itemView.newMessageNotif.visibility = View.VISIBLE
                    viewHolder.itemView.txtLatestMessage.text = chatMessage.text
                } else {
                    viewHolder.itemView.newMessageNotif.visibility = View.GONE
                    val lastMessage = "You: " + chatMessage.text
                    viewHolder.itemView.txtLatestMessage.text = lastMessage
                }
                Picasso.get().load(user!!.profileImageUrl).error(R.drawable.profile_photo).into(viewHolder.itemView.imgProfilePhoto)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        viewHolder.itemView.llLatestMessageRow.setOnClickListener {
            val intent = Intent(context as Activity, ChatLogActivity::class.java)
            intent.putExtra("ToUser",user)
            context.startActivity(intent)
        }
    }
}