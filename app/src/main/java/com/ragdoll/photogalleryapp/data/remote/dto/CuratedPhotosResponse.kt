package com.ragdoll.photogalleryapp.data.remote.dto

data class CuratedPhotosResponse(
    val page: Int,
    val per_page: Int,
    val photos: List<PhotoResponse>,
    val total_results: Int,
    val next_page: String?
)