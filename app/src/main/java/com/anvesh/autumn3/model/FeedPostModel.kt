package com.anvesh.autumn3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.security.Timestamp

@Parcelize
class FeedPostModel(val postId: String, val userUid: String,val username: String, val userImageUrl: String, val feedPostImageUrl: String, val feedPostCaption: String,val timestamp: Long): Parcelable {
    constructor(): this("","","","","","",-1)
}