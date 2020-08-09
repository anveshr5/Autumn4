package com.anvesh.autumn3.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.anvesh.autumn3.R
import com.anvesh.autumn3.activity.MainActivity
import com.anvesh.autumn3.model.FeedPostModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class AddPhotoToFeedFragment : Fragment() {

    lateinit var btnClickToAddPhoto: Button
    lateinit var imgSelectedImageToAdd: ImageView
    lateinit var etPostCaption: EditText
    lateinit var btnAddPostToFeed: Button

    lateinit var progressLayout: RelativeLayout

    var selectedPhotoToAddUri: Uri? = null

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
            uploadPhotoToFirebase()
        }



        return view
    }

    private fun addPhotoToFeed(postImageUrl: String) {
        val userUid = MainActivity.currentUser?.uid
        val username = MainActivity.currentUser?.username
        val userImageUrl = MainActivity.currentUser?.profileImageUrl
        val postCaption = etPostCaption.text.toString().trimEnd()

        val addPhotoToFeedRef = FirebaseDatabase.getInstance().getReference("/global-posts").push()

        if (addPhotoToFeedRef.key != null && userUid != null && username != null && userImageUrl != null && postCaption != "" && postCaption.length <= 250) {
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
                activity?.supportFragmentManager!!.beginTransaction().replace(R.id.frameLayout, GlobalFeedFragment()).commit()
                activity?.bottomNavigationBar!!.setItemSelected(R.id.globalFeed,true)
            }
        } else if (userUid == null || username == null || userImageUrl == null) {
            Toast.makeText(
                activity as Context,
                "An error occurred, please try again later",
                Toast.LENGTH_SHORT
            ).show()
        } else if (postCaption == ""){
            showError(etPostCaption,"Must have a caption")
        } else if (postCaption.length > 250){
            showError(etPostCaption,"Caption can not exceed 250 characters")
        }
    }

    private fun uploadPhotoToFirebase() {
        if (selectedPhotoToAddUri == null) return
        val filename = UUID.randomUUID().toString()
        val imageRef = FirebaseStorage.getInstance().getReference("/global-posts/$filename")
        imageRef.putFile(selectedPhotoToAddUri!!).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                addPhotoToFeed(it.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoToAddUri = data.data

            Picasso.get().load(selectedPhotoToAddUri).into(imgSelectedImageToAdd)

            btnClickToAddPhoto.visibility = View.GONE
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