package com.anvesh.autumn3.fragments.loginRegister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button

    lateinit var progressLayout: RelativeLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if(email.isNotEmpty() && password.length>= 6){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginActivity", "signInWithEmail:success")

                        val intent = Intent(activity as Context, MainActivity::class.java)
                        startActivity(intent)
                        AppCompatActivity().finish()
                    } else {
                        progressLayout.visibility = View.GONE
                        Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(activity as Context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                progressLayout.visibility = View.GONE
                if(email.isEmpty()){
                    showError(etEmail,"Invalid Email")
                }
                if (password.length <= 6){
                    showError(etPassword,"Incorrect Password")
                }
            }
        }

        return view
    }

    private fun showError(input: EditText, error: String) {
        input.error = error
        input.requestFocus()
    }

}