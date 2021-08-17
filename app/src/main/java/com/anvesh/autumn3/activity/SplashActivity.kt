package com.anvesh.autumn3.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.anvesh.autumn3.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val intent = if (currentUser != null) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, LoginRegisterActivity::class.java)
        }
        Handler().postDelayed({
            startActivity(intent)
            finish()
        },2000)

    }
}