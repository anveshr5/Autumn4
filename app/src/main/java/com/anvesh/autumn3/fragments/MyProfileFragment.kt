package com.anvesh.autumn3.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.activity.RegisterActivity
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.*

class MyProfileFragment : Fragment() {

    lateinit var profilePhoto: CircleImageView
    lateinit var txtUsername: TextView
    lateinit var btnLogOut: Button

    val userProfile: User? = MainActivity.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        profilePhoto = view.findViewById(R.id.imgCircleViewProfilePhoto)
        txtUsername = view.findViewById(R.id.txtUsername)
        btnLogOut = view.findViewById(R.id.btnLogOut)

        setUpProfileDetails()


        logout()

        return view
    }

    private fun setUpProfileDetails() {
        txtUsername.text = userProfile?.username
            Picasso.get().load(userProfile?.profileImageUrl).error(R.drawable.profile_photo).into(profilePhoto)
    }

    private fun logout() {
        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity as Context, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}