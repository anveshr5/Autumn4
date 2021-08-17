package com.anvesh.autumn3.fragments.mainActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.adapter.MyPostsAdapter
import com.anvesh.autumn3.model.FeedPostModel
import com.anvesh.autumn3.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MyProfileFragment : Fragment() {

    lateinit var profilePhoto: CircleImageView
    lateinit var txtUsername: TextView
    lateinit var txtUserBio: TextView

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: StaggeredGridLayoutManager
    lateinit var myPostsAdapter: MyPostsAdapter

    val myFeedPosts: ArrayList<FeedPostModel> = arrayListOf()

    val userProfile: User? = MainActivity.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        profilePhoto = view.findViewById(R.id.imgCircleViewProfilePhoto)
        txtUsername = view.findViewById(R.id.txtUsername)
        txtUserBio = view.findViewById(R.id.txtUserBio)
        recyclerView = view.findViewById(R.id.recyclerviewMyPosts)

        layoutManager =// GridLayoutManager(activity as Context,3)
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        getMyPosts()

        recyclerView.layoutManager = layoutManager

        //btnLogOut = view.findViewById(R.id.btnLogOut)

        setUpProfileDetails()


        //logout()

        return view
    }

    private fun getMyPosts() {
        val myPostRef = FirebaseDatabase.getInstance().getReference("/posts/${userProfile?.uid}")

        myPostRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val myPost = it.getValue(FeedPostModel::class.java)
                    if (myPost != null) {
                        myFeedPosts.add(myPost)
                    }
                    myFeedPosts.sortByDescending { it.timestamp }
                    setUpRecycler()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun setUpRecycler() {
        myPostsAdapter = MyPostsAdapter(myFeedPosts)

        recyclerView.adapter = myPostsAdapter
    }

    private fun setUpProfileDetails() {
        txtUsername.text = userProfile?.username
        Glide.with(activity as Context).load(userProfile!!.profileImageUrl).error(R.drawable.profile_photo).into(profilePhoto)
            // Picasso.get().load(userProfile?.profileImageUrl).error(R.drawable.profile_photo)
            //.into(profilePhoto)
        txtUserBio.text = userProfile?.userBio

    }
}