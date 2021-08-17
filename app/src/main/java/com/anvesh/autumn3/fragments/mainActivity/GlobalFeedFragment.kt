package com.anvesh.autumn3.fragments.mainActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.adapter.GlobalFeedPostsAdapter
import com.anvesh.autumn3.model.FeedPostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GlobalFeedFragment : Fragment() {

    lateinit var recyclerGlobalFeed : RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var txtLoadingMessage: TextView

    val feedPosts = arrayListOf<FeedPostModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_global_feed, container, false)

        recyclerGlobalFeed = view.findViewById(R.id.recyclerGlobalFeed)
        progressLayout = view.findViewById(R.id.progressLayout)
        txtLoadingMessage = view.findViewById(R.id.txtLoadingMessage)
        progressLayout.visibility = View.VISIBLE

        listenForGlobalPosts()

        return view
    }

    private fun listenForGlobalPosts() {
        val globalPostsRef = FirebaseDatabase.getInstance().getReference("/global-posts")

        globalPostsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { it ->
                    val feedPost = it.getValue(FeedPostModel::class.java)
                    if (feedPost != null) {
                        feedPosts.add(feedPost)
                    }
                }
                feedPosts.sortByDescending { it.timestamp }
                setUpGlobalPosts()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun setUpGlobalPosts() {
        if (feedPosts.isNotEmpty()) {
            recyclerGlobalFeed.adapter = GlobalFeedPostsAdapter(feedPosts)
            recyclerGlobalFeed.adapter?.notifyDataSetChanged()
            progressLayout.visibility = View.GONE
        } else {
            txtLoadingMessage.text = getString(R.string.no_posts_to_display)
            //Add No internet connection
        }
    }
}