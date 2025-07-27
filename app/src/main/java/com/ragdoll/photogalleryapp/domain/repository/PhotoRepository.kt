package com.ragdoll.photogalleryapp.domain.repository

import androidx.paging.PagingData
import com.ragdoll.photogalleryapp.domain.model.Photo
import kotlinx.coroutines.flow.Flow

// 4          2 paging
interface PhotoRepository {
    fun getPhotos(): Flow<PagingData<Photo>>
    suspend fun getPhotoById(id: Int): Photo?
    val networkState: Flow<Boolean>
}