package com.anvesh.autumn3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.FeedPostModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GlobalFeedPostsAdapter(val feedPosts: ArrayList<FeedPostModel>) : RecyclerView.Adapter<GlobalFeedPostsAdapter.GlobalFeedPostViewHolder>() {
    class GlobalFeedPostViewHolder(view : View): RecyclerView.ViewHolder(view){
        val imgCircleViewProfilePhoto: CircleImageView = view.findViewById(R.id.imgCircleViewProfilePhoto)
        val txtUsername: TextView = view.findViewById(R.id.txtUsername)
        val imgFeedPostImage: ImageView = view.findViewById(R.id.imgFeedPostImage)
        val txtFeedPostCaption: TextView = view.findViewById(R.id.txtFeedPostCaption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalFeedPostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_feed_post_layout,parent,false)

        return GlobalFeedPostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedPosts.size
    }

    override fun onBindViewHolder(holder: GlobalFeedPostViewHolder, position: Int) {
        val feedPost = feedPosts[position]

        Picasso.get().load(feedPost.userImageUrl).into(holder.imgCircleViewProfilePhoto)
        holder.txtUsername.text = feedPost.username
        Picasso.get().load(feedPost.feedPostImageUrl).into(holder.imgFeedPostImage)
        holder.txtFeedPostCaption.text = feedPost.feedPostCaption

    }
}