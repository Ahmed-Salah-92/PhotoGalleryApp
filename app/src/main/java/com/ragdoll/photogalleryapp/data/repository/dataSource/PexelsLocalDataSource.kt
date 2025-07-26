package com.ragdoll.photogalleryapp.data.repository.dataSource

import com.ragdoll.photogalleryapp.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface PexelsLocalDataSource {
    suspend fun upsertPhoto(photo: List<Photo>)
    fun getPhoto(): Flow<List<Photo>>
}