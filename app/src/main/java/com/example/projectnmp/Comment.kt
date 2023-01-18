package com.example.projectnmp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Comment (val comment_id: Number, val content :String, val first_name:String,
               val last_name:String?, val publish_date:String): Parcelable