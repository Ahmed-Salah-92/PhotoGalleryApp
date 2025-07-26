package com.ragdoll.photogalleryapp.data.repository.dataSource

import com.ragdoll.photogalleryapp.data.model.APIResponse
import retrofit2.Response

// 3
interface PexelsRemoteDataSource {
    suspend fun fetchPhotos(page: Int, perPage: Int): Response<APIResponse>
}