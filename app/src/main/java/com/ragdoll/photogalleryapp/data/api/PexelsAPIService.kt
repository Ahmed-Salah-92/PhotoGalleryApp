package com.ragdoll.photogalleryapp.data.api

import com.ragdoll.photogalleryapp.data.model.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 2
interface PexelsAPIService {
     companion object {
         const val BASE_URL = "https://api.pexels.com"
        const val API_KEY = "VZhgOp4Gp1hNjMzdKsQbXEpPNbrLtpYZQJeIEm586XCV1KpzHOcYaCx7"
    }

    @GET("/v1/curated")
    suspend fun fetchPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") apiKey: String = API_KEY
    ): Response<APIResponse>
}
