package com.ragdoll.photogalleryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: Int,
    val photographer: String,
    val url: String,
    val originalUrl: String,
    val largeUrl: String,
    val mediumUrl: String,
    val smallUrl: String,
    val altText: String?
)