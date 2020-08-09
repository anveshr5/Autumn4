package com.anvesh.autumn3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtRegister: TextView

    lateinit var progressLayout: RelativeLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Welcome back! Login to continue"

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if(email.isNotEmpty() && password.length>= 6){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "signInWithEmail:success")

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        progressLayout.visibility = View.GONE
                        Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                progressLayout.visibility = View.GONE
                if(email.isEmpty()){
                    showError(etEmail,"Invalid Email")
                }
                if (password.length >= 6){
                    showError(etPassword,"Incorrect Password")
                }
            }
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showError(input: EditText, error: String) {
        input.error = error
        input.requestFocus()
    }

}