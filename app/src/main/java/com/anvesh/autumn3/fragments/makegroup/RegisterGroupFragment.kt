package com.anvesh.autumn3.fragments.makegroup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.anvesh.autumn3.R
import com.anvesh.autumn3.model.Group
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView

class RegisterGroupFragment : Fragment() {

    lateinit var btnSelectPhoto: Button
    lateinit var imgSelectedGroupPhoto: CircleImageView
    lateinit var etGroupName: EditText
    lateinit var etGroupBio: EditText
    lateinit var btnRegisterGroup: Button

    var selectedPhotoUri: Uri? = null
    var croppedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_group, container, false)

        btnSelectPhoto = view.findViewById(R.id.btnSelectPhoto)
        imgSelectedGroupPhoto = view.findViewById(R.id.imgCircleViewSelectedPhoto)
        etGroupName = view.findViewById(R.id.etGroupName)
        etGroupBio = view.findViewById(R.id.etGroupBio)
        btnRegisterGroup = view.findViewById(R.id.btnRegisterGroup)

        btnSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            //selectImage()
            //val uri = CropImage.activity(selectedPhotoUri).setGuidelines(CropImageView.Guidelines.ON)
        }
        imgSelectedGroupPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            //startCropImageActivity(selectedPhotoUri!!)
        }

        btnRegisterGroup.setOnClickListener {
            if(etGroupName.text.toString().isNotEmpty() && etGroupBio.text.toString().isNotEmpty() && etGroupBio.text.toString().length <=250 && croppedPhotoUri != null){
                fetchRegisterGroup()
            }
        }

        return view
    }

    private fun fetchRegisterGroup() {
        val groupName = etGroupName.text.toString()
        val groupBio = etGroupBio.text.toString()

        val groupId = groupName + System.currentTimeMillis().toString()

        uploadGroupPhoto(groupId,groupName,groupBio)

    }

    private fun uploadGroupPhoto(
        groupId: String,
        groupName: String,
        groupBio: String
    ) {
        if (croppedPhotoUri == null) return
        val filename = groupId
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(croppedPhotoUri!!).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener {
                //it contains url to group photo
                registerGroup(groupId,groupName,it.toString(),groupBio)
            }
        }
    }

    private fun registerGroup(groupId: String, groupName: String, groupPhotoUrl: String, groupBio: String) {
        val newGroupRef = FirebaseDatabase.getInstance().getReference("/users").push()

        newGroupRef.setValue(
            Group(
                groupId,
                groupName,
                groupPhotoUrl,
                groupBio
            )
        ).addOnSuccessListener {
            
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //     retrieve selected image into app
            selectedPhotoUri = data.data

            //startCropImageActivity(selectedPhotoUri!!)
            context?.let {
                CropImage.activity(selectedPhotoUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1).start(
                        it, this
                    )
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                croppedPhotoUri = resultUri
                Log.d("selectedPhotoUri", croppedPhotoUri.toString() + " 4")
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        if (croppedPhotoUri != null) {
            btnSelectPhoto.visibility = View.GONE
            Glide.with(activity as Context).load(croppedPhotoUri).into(imgSelectedGroupPhoto)
            // Picasso.get().load(croppedPhotoUri).into(imgCircleViewSelectedPhoto)
            imgSelectedGroupPhoto.visibility = View.VISIBLE
        } else {
            btnSelectPhoto.visibility = View.GONE
                Glide.with(activity as Context).load(selectedPhotoUri).into(imgSelectedGroupPhoto)
            // Picasso.get().load(selectedPhotoUri).into(imgCircleViewSelectedPhoto)
            imgSelectedGroupPhoto.visibility = View.VISIBLE
        }
    }
}