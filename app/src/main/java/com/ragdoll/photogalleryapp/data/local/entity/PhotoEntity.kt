package com.ragdoll.photogalleryapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "photographer") val photographer: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "original_url") val originalUrl: String,
    @ColumnInfo(name = "large_url") val largeUrl: String,
    @ColumnInfo(name = "medium_url") val mediumUrl: String,
    @ColumnInfo(name = "small_url") val smallUrl: String,
    @ColumnInfo(name = "alt_text") val altText: String?
)