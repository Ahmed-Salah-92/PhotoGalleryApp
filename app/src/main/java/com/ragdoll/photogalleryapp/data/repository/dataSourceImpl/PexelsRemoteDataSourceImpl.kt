package com.ragdoll.photogalleryapp.data.repository.dataSourceImpl

import com.ragdoll.photogalleryapp.data.api.PexelsAPIService
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsRemoteDataSource

// 6
class PexelsRemoteDataSourceImpl(
    private val pexelsAPIService: PexelsAPIService,
) : PexelsRemoteDataSource {

    override suspend fun fetchPhotos(page: Int, perPage: Int) =
        pexelsAPIService.fetchPhotos(page, perPage)
}