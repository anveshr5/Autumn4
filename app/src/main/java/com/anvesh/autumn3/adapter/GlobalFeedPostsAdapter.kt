package com.anvesh.autumn3.adapter

import android.graphics.Paint
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.FeedPostModel
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class GlobalFeedPostsAdapter(val feedPosts: ArrayList<FeedPostModel>) : RecyclerView.Adapter<GlobalFeedPostsAdapter.GlobalFeedPostViewHolder>() {
    class GlobalFeedPostViewHolder(view : View): RecyclerView.ViewHolder(view){
        val imgCircleViewProfilePhoto: CircleImageView = view.findViewById(R.id.imgCircleViewProfilePhoto)
        val txtUsername: TextView = view.findViewById(R.id.txtUsername)
        val imgFeedPostImage: ImageView = view.findViewById(R.id.imgFeedPostImage)
        val txtFeedPostCaption: TextView = view.findViewById(R.id.txtFeedPostCaption)
        val txtTimePosted: TextView = view.findViewById(R.id.txtTimePosted)
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

        Glide.with(holder.itemView.context).load(feedPost.userImageUrl).into(holder.imgCircleViewProfilePhoto)
        //Picasso.get().load(feedPost.userImageUrl).into(holder.imgCircleViewProfilePhoto)
        holder.txtUsername.text = feedPost.username
        Glide.with(holder.itemView.context).load(feedPost.feedPostImageUrl).into(holder.imgFeedPostImage)
        //Picasso.get().load(feedPost.feedPostImageUrl).into(holder.imgFeedPostImage)
        holder.txtFeedPostCaption.text = feedPost.feedPostCaption

        val time = System.currentTimeMillis() - feedPost.timestamp*1000
        val displayTimeString = when {
            time/8.64e+7>=1 -> {
                "${(time/8.64e+7).toInt()} Days ago"
            }
            time/3.6e+6>1 -> {
                "${(time/3.6e+6).toInt()} Hours ago"
            }
            time/60000>1 -> {
                "${(time/60000).toInt()} Minutes ago"
            }
            else -> {
                "Less than a minute ago"
            }
        }
        holder.txtTimePosted.text = displayTimeString
    }
}