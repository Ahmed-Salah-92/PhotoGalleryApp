package com.ragdoll.photogalleryapp.domain.model

data class Photo(
    val id: Int,
    val photographer: String,
    val url: String,
    val src: PhotoSource,
    val alt: String?
)

data class PhotoSource(
    val original: String,
    val large: String,
    val medium: String,
    val small: String
)