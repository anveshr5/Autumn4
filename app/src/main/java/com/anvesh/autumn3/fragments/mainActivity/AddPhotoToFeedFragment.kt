package com.anvesh.autumn3.fragments.mainActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.model.FeedPostModel
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class AddPhotoToFeedFragment : Fragment() {

    lateinit var btnClickToAddPhoto: Button
    lateinit var imgSelectedImageToAdd: ImageView
    lateinit var etPostCaption: EditText
    lateinit var btnAddPostToFeed: Button

    lateinit var progressLayout: RelativeLayout

    var selectedPhotoToAddUri: Uri? = null
    var croppedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_photo_to_feed, container, false)

        btnClickToAddPhoto = view.findViewById(R.id.btnClickToAddPhoto)
        imgSelectedImageToAdd = view.findViewById(R.id.imgSelectedImageToAdd)
        etPostCaption = view.findViewById(R.id.etPostCaption)
        btnAddPostToFeed = view.findViewById(R.id.btnAddPostToFeed)
        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.GONE
        btnAddPostToFeed.visibility = View.GONE


        btnClickToAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        imgSelectedImageToAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btnAddPostToFeed.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            if (etPostCaption.text.toString().trimEnd() != "" &&
                etPostCaption.text.toString().trimEnd().length <= 250
            ) {
                uploadPhotoToFirebase()
            } else {
                progressLayout.visibility = View.GONE
                if (etPostCaption.text.toString().trimEnd() == "") {
                    showError(etPostCaption, "Must have a caption")
                } else if (etPostCaption.text.toString().trimEnd().length > 250) {
                    showError(etPostCaption, "Caption can not exceed 250 characters")
                }
            }
        }

        return view
    }

    private fun addPhotoToFeed(postImageUrl: String) {
        val userUid = MainActivity.currentUser?.uid
        val username = MainActivity.currentUser?.username
        val userImageUrl = MainActivity.currentUser?.profileImageUrl
        val postCaption = etPostCaption.text.toString().trimEnd()

        val addPhotoToFeedRef = FirebaseDatabase.getInstance().getReference("/global-posts").push()

        val selfRef =
            FirebaseDatabase.getInstance().getReference("/posts/${MainActivity.currentUser?.uid}").push()

        if (addPhotoToFeedRef.key != null && userUid != null && username != null && userImageUrl != null) {
            addPhotoToFeedRef.setValue(
                FeedPostModel(
                    addPhotoToFeedRef.key!!,
                    userUid,
                    username,
                    userImageUrl,
                    postImageUrl,
                    postCaption,
                    System.currentTimeMillis() / 1000
                )
            ).addOnSuccessListener {
                activity?.supportFragmentManager!!.beginTransaction()
                    .replace(R.id.frameLayout,
                        GlobalFeedFragment()
                    ).commit()
                activity?.bottomNavigationBar!!.setItemSelected(R.id.globalFeed, true)
            }
            selfRef.setValue(
                FeedPostModel(
                    addPhotoToFeedRef.key!!,
                    userUid,
                    username,
                    userImageUrl,
                    postImageUrl,
                    postCaption,
                    System.currentTimeMillis() / 1000
                )
            ).addOnSuccessListener {
                //Add on success listener
            }
        } else {
            progressLayout.visibility = View.GONE
            if (userUid == null || username == null || userImageUrl == null) {
                Toast.makeText(
                    activity as Context,
                    "An error occurred, please try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun uploadPhotoToFirebase() {
        if (croppedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val imageRef = FirebaseStorage.getInstance().getReference("/global-posts/$filename")
        imageRef.putFile(croppedPhotoUri!!).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                addPhotoToFeed(it.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //     retrieve selected image into app
            selectedPhotoToAddUri = data.data

            //startCropImageActivity(selectedPhotoUri!!)
            context?.let {
                CropImage.activity(selectedPhotoToAddUri).setGuidelines(CropImageView.Guidelines.ON).setAllowRotation(true)
                    .start(
                        it, this
                    )
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                croppedPhotoUri = resultUri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(activity as Context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }

        if (croppedPhotoUri != null) {
            btnClickToAddPhoto.visibility = View.GONE
            Glide.with(activity as Context).load(croppedPhotoUri).into(imgSelectedImageToAdd)
            imgSelectedImageToAdd.visibility = View.VISIBLE
            etPostCaption.visibility = View.VISIBLE
            btnAddPostToFeed.visibility = View.VISIBLE
        } else {
            btnClickToAddPhoto.visibility = View.GONE
            Glide.with(activity as Context).load(selectedPhotoToAddUri).into(imgSelectedImageToAdd)
            imgSelectedImageToAdd.visibility = View.VISIBLE
            etPostCaption.visibility = View.VISIBLE
            btnAddPostToFeed.visibility = View.VISIBLE
        }
    }

    private fun showError(input: EditText, error: String) {
        input.error = error
        input.requestFocus()
    }
}