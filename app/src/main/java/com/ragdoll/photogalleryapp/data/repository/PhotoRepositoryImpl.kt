package com.ragdoll.photogalleryapp.data.repository

import android.util.Log
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
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class PhotoRepositoryImpl(
    private val api: PexelsApi,
    private val db: AppDatabase,
    private val networkMonitor: NetworkMonitor,
) : PhotoRepository {
    
    private val TAG = "PhotoRepository"
    
    // Expose network state from the repository
    override val networkState: Flow<Boolean> = networkMonitor.isOnline
        .onEach { isOnline -> Log.d(TAG, "Network state changed: isOnline = $isOnline") }
    
    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotos(): Flow<PagingData<Photo>> {
        Log.d(TAG, "getPhotos: Creating paging flow")
        // Create a mediator that's aware of network status
        val remoteMediator = PhotoRemoteMediator(api, db, networkMonitor)
        
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE / 2,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE * 2
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { 
                Log.d(TAG, "Creating paging source from database")
                db.photoDao().getPagingSource() 
            }
        ).flow.map { pagingData ->
            Log.d(TAG, "Received paging data, mapping to domain models")
            pagingData.map { photoEntity ->
                PhotoMapper.mapToDomain(photoEntity).also { photo ->
                    Log.v(TAG, "Mapped photo: id=${photo.id}, photographer=${photo.photographer}")
                }
            }
        }
        .onEach { pagingData ->
            Log.d(TAG, "Emitting PagingData to UI")
        }
        .conflate() // Use conflate to ensure we only process the latest data
    }

    override suspend fun getPhotoById(id: Int): Photo? {
        Log.d(TAG, "getPhotoById: Fetching photo with id=$id")
        return db.photoDao().getPhotoById(id)?.let {
            Log.d(TAG, "getPhotoById: Found photo in database, mapping to domain model")
            PhotoMapper.mapToDomain(it)
        } ?: run {
            Log.w(TAG, "getPhotoById: Photo with id=$id not found in database")
            null
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}