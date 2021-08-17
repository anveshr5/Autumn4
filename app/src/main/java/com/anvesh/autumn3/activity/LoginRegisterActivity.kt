package com.anvesh.autumn3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anvesh.autumn3.R
import com.anvesh.autumn3.fragments.loginRegister.LoginFragment
import com.anvesh.autumn3.fragments.loginRegister.RegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginRegisterActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        setUpToolbar()

        setUpLoginFragment()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nagivateLogin->setUpLoginFragment()

                R.id.navigateRegister -> setUpRegisterLayout()
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun setUpToolbar() {

    }

    private fun setUpLoginFragment() {
        supportActionBar?.title = "Welcome back! Login to continue"
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,LoginFragment()).commit()
        //bottomNavigationView.selectedItemId = R.id.nagivateLogin
    }

    private fun setUpRegisterLayout() {
        supportActionBar?.title = "Welcome!, Register Yourself!"
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, RegisterFragment()).commit()
    }
}