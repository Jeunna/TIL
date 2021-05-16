package com.example.firstapp.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class ContentList(
    @Json(name="Contents")
    val contentList: List<ContentModel> = emptyList()
    //arrayListOf()
) : ResponseData()

@Parcelize //Serializable
data class ContentModel(
    val title: String = "",
    val contentLink: String = "",
    val thumbnail: String = "",
    var details: String = "",
    val isAvailable: Boolean = false
) : Parcelable {}

@Parcelize
data class MarsProperty (
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double) : Parcelable {
    val isRental
        get() = type == "rent"
}

data class Account(
    val auth_token: String = "",
    val email: String = "",
    val user: String = ""
): ResponseData()

data class Details (
    val text: String = ""
): ResponseData()