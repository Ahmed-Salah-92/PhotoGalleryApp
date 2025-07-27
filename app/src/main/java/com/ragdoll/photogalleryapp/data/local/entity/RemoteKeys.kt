package com.ragdoll.photogalleryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class that stores pagination keys for each photo.
 * This helps the RemoteMediator know which pages have been loaded before
 * and which page to load next.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val photoId: Int,
    val prevPage: Int?,
    val currentPage: Int,
    val nextPage: Int?
)