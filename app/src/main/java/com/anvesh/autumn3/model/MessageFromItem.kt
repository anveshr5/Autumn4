package com.anvesh.autumn3.model

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.provider.Settings
import com.anvesh.autumn3.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recyclerview_chatrow_friend.view.*
import kotlinx.android.synthetic.main.recyclerview_chatrow_self.view.*
import kotlinx.android.synthetic.main.recyclerview_chatrow_self.view.imgProfilePhoto
import kotlinx.android.synthetic.main.recyclerview_chatrow_self.view.txtmessage

class MessageFromItem(val context: Context,val text: String, val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.recyclerview_chatrow_friend
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtmessage.text = text

        Picasso.get().load(user.profileImageUrl).error(R.drawable.profile_photo).into(viewHolder.itemView.imgProfilePhoto)

        viewHolder.itemView.rlMessageFriend.setOnLongClickListener {
            var clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip = ClipData.newPlainText("Message",text)

            clipboard.setPrimaryClip(clip)

            return@setOnLongClickListener true
        }
    }
}