package com.ragdoll.photogalleryapp.domain.usecase

import androidx.paging.PagingData
import com.ragdoll.photogalleryapp.domain.model.Photo
import com.ragdoll.photogalleryapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

// 5
class GetPhotosUseCase(private val repository: PhotoRepository) {
    operator fun invoke(): Flow<PagingData<Photo>> = repository.getPhotos()
}