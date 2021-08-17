package com.anvesh.autumn3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Group(val GroupUid: String,val groupName: String, val GroupImageUrl : String,val GroupBio: String) :
    Parcelable {
    constructor(): this("","","","")
}