package com.anvesh.autumn3.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.anvesh.autumn3.R
import com.anvesh.autumn3.fragments.mainActivity.*
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout
    private lateinit var bottomNavigationView: ChipNavigationBar

    companion object {
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Autumn"

        fetchCurrentUser()

        frameLayout = findViewById(R.id.frameLayout)
        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        openChatSection()

        navigationBarListener()
    }

    private fun navigationBarListener() {
        bottomNavigationView.setOnItemSelectedListener {
            when (it) {
                R.id.chat -> openChatSection()

                R.id.globalFeed -> openGlobalFeed()

                R.id.addPhotoToFeed-> openAddPhotoToFeed()

                R.id.autumn -> openAutumn()

                R.id.myProfile -> openMyProfile()
            }
        }
    }

    private fun openMyProfile() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
            MyProfileFragment()
        )
            .commit()
    }

    private fun openAutumn() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
            AutumnChatFragment()
        )
            .commit()
    }

    private fun openAddPhotoToFeed() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
            AddPhotoToFeedFragment()
        )
            .commit()
    }

    private fun openGlobalFeed() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
            GlobalFeedFragment()
        )
            .commit()
    }

    private fun openChatSection() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            ChatSectionFragment()
        ).commit()
        bottomNavigationView.setItemSelected(R.id.chat, true)
        supportActionBar?.title = "Messenger"
    }

    private fun fetchCurrentUser() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frameLayout)){
            !is ChatSectionFragment -> openChatSection()

        else -> ActivityCompat.finishAffinity(this@MainActivity)
        }
    }
}

