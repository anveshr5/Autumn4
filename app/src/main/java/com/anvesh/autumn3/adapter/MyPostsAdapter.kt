package com.anvesh.autumn3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.FeedPostModel
import com.bumptech.glide.Glide

class MyPostsAdapter(val myFeedPosts: ArrayList<FeedPostModel>) : RecyclerView.Adapter<MyPostsAdapter.MyPostsViewHolder>() {
    class MyPostsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val myPostImage:ImageView = view.findViewById(R.id.imgMyPostPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_my_posts_layout,parent,false)

        return MyPostsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myFeedPosts.size
    }

    override fun onBindViewHolder(holder: MyPostsViewHolder, position: Int) {
        val myPost = myFeedPosts[position]

        Glide.with(holder.itemView.context).load(myPost.feedPostImageUrl).into(holder.myPostImage)
    }
}