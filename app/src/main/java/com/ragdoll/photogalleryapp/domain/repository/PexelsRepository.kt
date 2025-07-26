package com.ragdoll.photogalleryapp.domain.repository

import androidx.paging.PagingData
import com.ragdoll.photogalleryapp.data.model.APIResponse
import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.data.util.Resource
import kotlinx.coroutines.flow.Flow

// 4          2 paging
interface PexelsRepository {
    suspend fun fetchPhotos(page: Int, perPage: Int): Resource<APIResponse>
    fun getPhotos(): Flow<List<Photo>>
    fun getPagedPhotos(): Flow<PagingData<Photo>>
}