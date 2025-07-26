package com.ragdoll.photogalleryapp.data.local.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ragdoll.photogalleryapp.data.local.database.AppDatabase
import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity
import com.ragdoll.photogalleryapp.data.mapper.PhotoMapper.toEntity
import com.ragdoll.photogalleryapp.data.remote.api.PexelsApi
import com.ragdoll.photogalleryapp.data.repository.PhotoRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.ragdoll.photogalleryapp.presentation.common.NetworkMonitor
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val api: PexelsApi,
    val db: AppDatabase,
    val networkMonitor: NetworkMonitor
) : RemoteMediator<Int, PhotoEntity>() {

    val photoDao = db.photoDao()

    override suspend fun initialize(): InitializeAction {
        return if (networkMonitor.isOnline.first())
            InitializeAction.LAUNCH_INITIAL_REFRESH
        else
            InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        return try {
            // Calculate the page number based on the load type
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.let { lastItem ->
                        (lastItem.id / NETWORK_PAGE_SIZE) + 1
                    }
                }
            }

            // Fetch the curated photos from the API
            val response = api.getCuratedPhotos(page = page, NETWORK_PAGE_SIZE)

            // Update database with the fetched photos
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    photoDao.clearAll()
                }
                // Insert the fetched photos into the database
                photoDao.insertALll(response.photos.map{ it.toEntity()})
            }

            MediatorResult.Success(
                endOfPaginationReached = response.photos.size < NETWORK_PAGE_SIZE
            )

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}