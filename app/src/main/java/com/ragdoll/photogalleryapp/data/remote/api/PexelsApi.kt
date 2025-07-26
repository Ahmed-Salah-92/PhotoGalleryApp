package com.ragdoll.photogalleryapp.data.remote.api

import com.ragdoll.photogalleryapp.data.remote.dto.CuratedPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApi {
    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int,
    ): CuratedPhotosResponse

    companion object {
        const val BASE_URL = "https://api.pexels.com/"
    }
}