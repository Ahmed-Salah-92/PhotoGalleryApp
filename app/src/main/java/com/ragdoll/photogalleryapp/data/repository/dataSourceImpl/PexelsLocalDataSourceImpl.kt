package com.ragdoll.photogalleryapp.data.repository.dataSourceImpl

import com.ragdoll.photogalleryapp.data.db.PhotoDAO
import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsLocalDataSource
import kotlinx.coroutines.flow.Flow

class PexelsLocalDataSourceImpl(private val photoDAO: PhotoDAO) : PexelsLocalDataSource {
    override suspend fun upsertPhoto(photo: List<Photo>) =
        photoDAO.upsertPhoto(photo)

    override fun getPhoto(): Flow<List<Photo>> =
        photoDAO.getPhoto()
}