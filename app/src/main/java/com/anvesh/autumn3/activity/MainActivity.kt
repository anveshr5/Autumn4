package com.anvesh.autumn3.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anvesh.autumn3.R
import com.anvesh.autumn3.fragments.AutumnChatFragment
import com.anvesh.autumn3.fragments.ChatSectionFragment
import com.anvesh.autumn3.fragments.MyProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var myProfile: ImageView
    lateinit var autumn: ImageView
    lateinit var chatSection: ImageView

    lateinit var frameLayout: FrameLayout

    //lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Autumn"

        //toolbar = findViewById(R.id.toolbar)
        myProfile = findViewById(R.id.myProfile)
        autumn = findViewById(R.id.autumn)
        chatSection = findViewById(R.id.chatSection)
        frameLayout = findViewById(R.id.frameLayout)

        //setupToolbar(toolbar)
        openChatSection()

        myProfile.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                MyProfileFragment()
            ).commit()
        }
        autumn.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout,
                AutumnChatFragment()
            ).commit()
        }
        chatSection.setOnClickListener {
            openChatSection()
        }
    }

    private fun openChatSection() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            ChatSectionFragment()
        ).commit()
    }

    /*private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Autumn"
    }*/
}

