package com.anvesh.autumn3.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class RegisterActivity : AppCompatActivity() {

    lateinit var btnSelectPhoto: Button
    lateinit var imgCircleViewSelectedPhoto: CircleImageView
    lateinit var etUsername: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegister: Button
    lateinit var txtLogin: TextView

    lateinit var progressLayout: RelativeLayout

    lateinit var auth: FirebaseAuth

    // var rotation = -1

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "Welcome!, Register Yourself!"

        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
        imgCircleViewSelectedPhoto = findViewById(R.id.imgCircleViewSelectedPhoto)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        txtLogin = findViewById(R.id.txtLogin)

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            Toast.makeText(
                this@RegisterActivity,
                "Registering...",
                Toast.LENGTH_LONG
            ).show()
            performRegister()
        }

        btnSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        imgCircleViewSelectedPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        txtLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        val username = etUsername.text.toString()
        val email = etEmail.text.toString().trimEnd()
        val password = etPassword.text.toString()

        if (username.isNotEmpty() && email.isNotEmpty() && password.length >= 6 && selectedPhotoUri != null) {
            val str = "Username = $username\nEmail = $email\nPassword = $password"
            Log.d("RegisterActivity", str)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@RegisterActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterActivity", "createUserWithEmail:success")
                        uploadImageToFirebase()
                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(
                            "RegisterActivity",
                            "createUserWithEmail:failure",
                            task.exception
                        )
                        showError(etEmail, "Invalid Email/Email already in use")
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressLayout.visibility = View.GONE
                    }
                }
        } else {
            progressLayout.visibility = View.GONE
            if (username.isEmpty()) {
                showError(etUsername, "Username can not be Blank")
            }
            if (email.isEmpty()) {
                showError(etEmail, "Invalid Email")
            }
            if (password.length < 6) {
                showError(etPassword, "Password must be longer than 6 characters")
            }
            if (selectedPhotoUri == null) {
                Toast.makeText(
                    this,
                    "Please select a profile photo to register",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showError(input: EditText, error: String) {
        input.error = error
        input.requestFocus()
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //retrieve selected image into app
            selectedPhotoUri = data.data

            btnSelectPhoto.visibility = View.GONE
            Picasso.get().load(selectedPhotoUri).into(imgCircleViewSelectedPhoto)
            imgCircleViewSelectedPhoto.visibility = View.VISIBLE
        }
    }

    private fun uploadImageToFirebase() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d("RegistryActivity", "Photo upload")

            ref.downloadUrl.addOnSuccessListener {
                //it contains url to profile photo
                saveUserToFirebaseDatabase(it.toString())
            }
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, etUsername.text.toString(), profileImageUrl)

        ref.setValue(user).addOnSuccessListener {
            Log.d("Added", "${user.uid}, ${user.username}, ${user.profileImageUrl}: User Created")

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}