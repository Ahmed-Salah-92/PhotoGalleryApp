package com.ragdoll.photogalleryapp.data.mapper

import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity
import com.ragdoll.photogalleryapp.data.remote.dto.PhotoResponse
import com.ragdoll.photogalleryapp.domain.model.Photo
import com.ragdoll.photogalleryapp.domain.model.PhotoSource

object PhotoMapper {
    fun mapToDomain(photoResponse: PhotoEntity): Photo {
        return Photo(
            id = photoResponse.id,
            photographer = photoResponse.photographer,
            url = photoResponse.url,
            src = PhotoSource(
                original = photoResponse.originalUrl,
                large = photoResponse.largeUrl,
                medium = photoResponse.smallUrl,
                small = photoResponse.smallUrl
            ),
            alt = photoResponse.altText
        )
    }

    fun PhotoResponse.toEntity(): PhotoEntity {
        return PhotoEntity(
            id = this.id,
            photographer = this.photographer,
            url = url,
            originalUrl = this.src.original,
            largeUrl = this.src.large,
            mediumUrl = this.src.medium,
            smallUrl = this.src.small,
            altText = this.alt
        )
    }
}