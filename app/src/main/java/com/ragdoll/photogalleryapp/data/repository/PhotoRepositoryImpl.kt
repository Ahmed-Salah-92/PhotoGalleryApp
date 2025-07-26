package com.ragdoll.photogalleryapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ragdoll.photogalleryapp.data.local.database.AppDatabase
import com.ragdoll.photogalleryapp.data.local.mediator.PhotoRemoteMediator
import com.ragdoll.photogalleryapp.data.mapper.PhotoMapper
import com.ragdoll.photogalleryapp.data.remote.api.PexelsApi
import com.ragdoll.photogalleryapp.domain.model.Photo
import com.ragdoll.photogalleryapp.domain.repository.PhotoRepository
import com.ragdoll.photogalleryapp.presentation.common.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepositoryImpl(
    private val api: PexelsApi,
    private val db: AppDatabase,
    private val networkMonitor: NetworkMonitor,
) : PhotoRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = PhotoRemoteMediator(api, db, networkMonitor),
            pagingSourceFactory = { db.photoDao().getPagingSource() }
        ).flow.map { pagingData ->
                pagingData.map {
                    PhotoMapper.mapToDomain(it)
                }
            }
    }

    override suspend fun getPhotoById(id: Int): Photo? {
        return db.photoDao().getPhotoById(id)?.let {
            PhotoMapper.mapToDomain(it)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 15
    }
}