package com.ragdoll.photogalleryapp.domain.usecase

import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import kotlinx.coroutines.flow.Flow

// 5
class GetPexelsPhotosUseCase(private val pexelsRepository: PexelsRepository) {

    fun execute(): Flow<List<Photo>> = pexelsRepository.getPhotos()
}