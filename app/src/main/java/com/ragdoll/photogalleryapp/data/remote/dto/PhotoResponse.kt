package com.ragdoll.photogalleryapp.data.remote.dto

data class PhotoResponse(
    val id: Int,
    val photographer: String,
    val url: String,
    val src: PhotoSourceDto,
    val alt: String?
)

data class PhotoSourceDto(
    val original: String,
    val large: String,
    val medium: String,
    val small: String
)
