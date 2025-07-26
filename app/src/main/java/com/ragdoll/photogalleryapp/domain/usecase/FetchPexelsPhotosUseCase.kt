package com.ragdoll.photogalleryapp.domain.usecase

import com.ragdoll.photogalleryapp.data.model.APIResponse
import com.ragdoll.photogalleryapp.data.util.Resource
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository

// 5
class FetchPexelsPhotosUseCase(private val pexelsRepository: PexelsRepository) {

    suspend fun execute(page: Int, perPage: Int): Resource<APIResponse> =
        pexelsRepository.fetchPhotos(page, perPage)

}