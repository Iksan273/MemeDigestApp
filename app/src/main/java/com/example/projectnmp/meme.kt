package com.example.projectnmp
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class meme(var meme_id:Int, var meme_url:String,var top_text:String,var bottom_text:String,
                var num_like:Int,var user_id:Int, var liked: Int, var jum_comment: Int): Parcelable
