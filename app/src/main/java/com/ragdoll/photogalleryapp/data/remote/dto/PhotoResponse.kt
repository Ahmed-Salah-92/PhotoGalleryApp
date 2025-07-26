package com.ragdoll.photogalleryapp.data.remote.dto

import com.google.gson.annotations.SerializedName
data class CuratedPhotosResponse(
    @SerializedName("photos") val photos: List<PhotoResponse>,
)
data class PhotoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("photographer") val photographer: String,
    @SerializedName("url") val url: String,
    @SerializedName("src") val src: PhotoSourceResponse,
    @SerializedName("alt") val alt: String?
)

data class PhotoSourceResponse(
    @SerializedName("original") val original: String,
    @SerializedName("large2x") val large: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("small") val small: String
)