package com.ragdoll.photogalleryapp.data.mapper

import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity
import com.ragdoll.photogalleryapp.data.remote.dto.PhotoResponse
import com.ragdoll.photogalleryapp.domain.model.Photo
import com.ragdoll.photogalleryapp.domain.model.PhotoSource

object PhotoMapper {
    fun mapToDomain(photoEntity: PhotoEntity): Photo {
        return Photo(
            id = photoEntity.id,
            photographer = photoEntity.photographer,
            url = photoEntity.url,
            src = PhotoSource(
                original = photoEntity.originalUrl,
                large = photoEntity.largeUrl,
                medium = photoEntity.mediumUrl,
                small = photoEntity.smallUrl
            ),
            alt = photoEntity.altText
        )
    }

    fun mapToEntity(photoResponse: PhotoResponse): PhotoEntity {
        return PhotoEntity(
            id = photoResponse.id,
            photographer = photoResponse.photographer,
            url = photoResponse.url,
            originalUrl = photoResponse.src.original,
            largeUrl = photoResponse.src.large,
            mediumUrl = photoResponse.src.medium,
            smallUrl = photoResponse.src.small,
            altText = photoResponse.alt
        )
    }
}