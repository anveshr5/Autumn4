package com.anvesh.autumn3.fragments.loginRegister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterFragment : Fragment() {

    lateinit var btnSelectPhoto: Button
    lateinit var imgCircleViewSelectedPhoto: CircleImageView
    lateinit var etUsername: EditText
    lateinit var etUserBio: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegister: Button

    lateinit var progressLayout: RelativeLayout

    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        btnSelectPhoto = view.findViewById(R.id.btnSelectPhoto)
        imgCircleViewSelectedPhoto = view.findViewById(R.id.imgCircleViewSelectedPhoto)
        etUsername = view.findViewById(R.id.etUsername)
        etUserBio = view.findViewById(R.id.etUserBio)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegister = view.findViewById(R.id.btnRegister)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE

        btnRegister.setOnClickListener {
            performRegister()
        }

        btnSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            //selectImage()
            //val uri = CropImage.activity(selectedPhotoUri).setGuidelines(CropImageView.Guidelines.ON)
        }
        imgCircleViewSelectedPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            //startCropImageActivity(selectedPhotoUri!!)
        }

        return view
    }

    private fun performRegister() {
        val username = etUsername.text.toString().trimEnd()
        val email = etEmail.text.toString().trimEnd()
        val password = etPassword.text.toString().trimEnd()
        val userBio = etUserBio.text.toString().trimEnd()

        if (username.isNotEmpty() && email.isNotEmpty() && password.length >= 6 && croppedPhotoUri != null && userBio.isNotEmpty() && userBio.length <= 250) {
            progressLayout.visibility = View.VISIBLE
            Toast.makeText(
                activity as Context,
                "Registering...",
                Toast.LENGTH_LONG
            ).show()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterActivity", "createUserWithEmail:success")
                        uploadImageToFirebase()
                        val user = FirebaseAuth.getInstance().currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(
                            "RegisterActivity",
                            "createUserWithEmail:failure",
                            task.exception
                        )
                        showError(etEmail, "Invalid Email/Email already in use")
                        Toast.makeText(
                            activity as Context, "Authentication failed.",
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
                    activity as Context,
                    "Please select a profile photo to register",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (userBio == "") {
                showError(etUserBio, "Bio must not be null")
            }
            if (userBio.length > 250) {
                showError(etUsername, "Bio can not be greater than 250 characters")
            }
        }
    }

    private fun showError(input: EditText, error: String) {
        input.error = error
        input.requestFocus()
    }

    var selectedPhotoUri: Uri? = null
    var croppedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //     retrieve selected image into app
            selectedPhotoUri = data.data
            //startCropImageActivity(selectedPhotoUri!!)
            context?.let {
                CropImage.activity(selectedPhotoUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1).start(it, this)
            }
        }

        Log.d(
            "selectedPhotoUri",
            requestCode.toString() + "   " + CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE.toString()
        )
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            Log.d("selectedPhotoUri", selectedPhotoUri.toString() + " 3")
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                croppedPhotoUri = resultUri
                Log.d("selectedPhotoUri", croppedPhotoUri.toString() + " 4")
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("selectedPhotoUri", error.toString() + " 5")

            }
        }

        if (croppedPhotoUri != null) {
            btnSelectPhoto.visibility = View.GONE
            Glide.with(activity as Context).load(croppedPhotoUri).into(imgCircleViewSelectedPhoto)
            // Picasso.get().load(croppedPhotoUri).into(imgCircleViewSelectedPhoto)
            imgCircleViewSelectedPhoto.visibility = View.VISIBLE
        } else {
            btnSelectPhoto.visibility = View.GONE
            Glide.with(activity as Context).load(selectedPhotoUri).into(imgCircleViewSelectedPhoto)
            // Picasso.get().load(selectedPhotoUri).into(imgCircleViewSelectedPhoto)
            imgCircleViewSelectedPhoto.visibility = View.VISIBLE
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("Anvesh", CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE.toString() + " $ " + Activity.RESULT_OK.toString() + " $ " )
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                selectedPhotoUri = result.uri
                val inputStream: InputStream?
                try {
                    inputStream = activity!!.contentResolver.openInputStream(selectedPhotoUri!!)
                    val photo: Bitmap = BitmapFactory.decodeStream(inputStream)
                    imgCircleViewSelectedPhoto.setImageBitmap(photo)
                    btnSelectPhoto.visibility = View.GONE
                    imgCircleViewSelectedPhoto.visibility = View.VISIBLE
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(
                    activity,
                    "Cropping failed: " + result.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(activity, "" + requestCode, Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun uploadImageToFirebase() {
        if (croppedPhotoUri == null) return
        val filename = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(croppedPhotoUri!!).addOnSuccessListener {
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

        val user = User(uid, etUsername.text.toString(), profileImageUrl, etUserBio.text.toString())

        ref.setValue(user).addOnSuccessListener {
            val intent = Intent(activity as Context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    fun selectImage() {
        val intent = CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .getIntent(context!!)
        startActivityForResult(intent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE)
    }

    private fun startCropImageActivity(imageUri: Uri) {
        context?.let { CropImage.activity(imageUri).setOutputUri(croppedPhotoUri).start(it, this) }
    }
}