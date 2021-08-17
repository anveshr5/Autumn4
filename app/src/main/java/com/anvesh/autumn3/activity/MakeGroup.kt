package com.anvesh.autumn3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anvesh.autumn3.R
import com.anvesh.autumn3.fragments.makegroup.RegisterGroupFragment

class MakeGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_group)

        startRegisterGroup()
    }

    private fun startRegisterGroup() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,RegisterGroupFragment()).commit()
    }
}