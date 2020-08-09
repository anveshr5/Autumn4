package com.anvesh.autumn3.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.adapter.GlobalFeedPostsAdapter
import com.anvesh.autumn3.model.FeedPostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class GlobalFeedFragment : Fragment() {

    lateinit var recyclerGlobalFeed : RecyclerView

    val feedPosts = arrayListOf<FeedPostModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_global_feed, container, false)

        recyclerGlobalFeed = view.findViewById(R.id.recyclerGlobalFeed)

        listenForGlobalPosts()

        return view
    }

    private fun listenForGlobalPosts() {
        val globalPostsRef = FirebaseDatabase.getInstance().getReference("/global-posts")

        globalPostsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val feedPost = it.getValue(FeedPostModel::class.java)
                    if (feedPost != null) {
                        feedPosts.add(feedPost)
                    }
                }
                Log.d("globalPosts", feedPosts.toString())
                recyclerGlobalFeed.adapter = GlobalFeedPostsAdapter(feedPosts)
                recyclerGlobalFeed.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}