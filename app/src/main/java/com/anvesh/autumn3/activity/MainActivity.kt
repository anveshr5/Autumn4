package com.anvesh.autumn3.activity

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anvesh.autumn3.R
import com.anvesh.autumn3.fragments.AutumnChatFragment
import com.anvesh.autumn3.fragments.ChatSectionFragment
import com.anvesh.autumn3.fragments.MyProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    lateinit var frameLayout: FrameLayout
    lateinit var bottomNavigationView: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Autumn"

        frameLayout = findViewById(R.id.frameLayout)
        bottomNavigationView = findViewById(R.id.bottomNavigationBar)
        openChatSection()

        navigationBarListener()
    }

    private fun navigationBarListener() {
        bottomNavigationView.setOnItemSelectedListener {
            when(it){
                R.id.chat -> openChatSection()

                R.id.autumn -> openAutumn()

                R.id.myProfile -> openMyProfile()
            }
        }
    }

    private fun openMyProfile() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,MyProfileFragment()).commit()
    }

    private fun openAutumn() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,AutumnChatFragment()).commit()
    }

    private fun openChatSection() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            ChatSectionFragment()
        ).commit()
        bottomNavigationView.setItemSelected(R.id.chat, true)
        supportActionBar?.title = "Messenger"
    }
}

