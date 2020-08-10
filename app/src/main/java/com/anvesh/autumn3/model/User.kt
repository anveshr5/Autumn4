package com.anvesh.autumn3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String,val username: String, val profileImageUrl : String,val userBio: String) : Parcelable {
    constructor(): this("","","","")
}