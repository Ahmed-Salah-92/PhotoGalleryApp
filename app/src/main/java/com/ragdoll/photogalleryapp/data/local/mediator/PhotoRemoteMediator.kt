package com.ragdoll.photogalleryapp.data.local.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ragdoll.photogalleryapp.data.local.database.AppDatabase
import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity
import com.ragdoll.photogalleryapp.data.local.entity.RemoteKeys
import com.ragdoll.photogalleryapp.data.mapper.PhotoMapper
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

    private val TAG = "PhotoRemoteMediator"
    val photoDao = db.photoDao()
    val remoteKeysDao = db.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val isOnline = networkMonitor.isOnline.first()
        Log.d(TAG, "initialize: isOnline = $isOnline")
        return if (isOnline)
            InitializeAction.LAUNCH_INITIAL_REFRESH.also {
                Log.d(TAG, "initialize: LAUNCH_INITIAL_REFRESH")
            }
        else
            InitializeAction.SKIP_INITIAL_REFRESH.also {
                Log.d(TAG, "initialize: SKIP_INITIAL_REFRESH - No internet connection")
            }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        Log.d(TAG, "load: loadType = $loadType, state pageSize = ${state.config.pageSize}")

        // Check network connectivity first
        val isOnline = networkMonitor.isOnline.first()
        Log.d(TAG, "load: isOnline = $isOnline")

        if (!isOnline) {
            Log.w(TAG, "load: No internet connection, returning error")
            return MediatorResult.Error(Exception("No internet connection"))
        }

        try {
            // Calculate the page number based on the load type
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d(TAG, "load: REFRESH, starting from page 1")
                    1
                }

                LoadType.PREPEND -> {
                    // Get the remote key of the first item in the database
                    val remoteKeys = getRemoteKeyForFirstItem(state)

                    // If remoteKeys is null or prevPage is null, there's no more data to load at the beginning
                    val prevPage = remoteKeys?.prevPage
                    if (prevPage == null) {
                        Log.d(TAG, "load: PREPEND, no more data at beginning, remoteKeys=$remoteKeys")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    Log.d(TAG, "load: PREPEND, loading page $prevPage")
                    prevPage
                }

                LoadType.APPEND -> {
                    Log.d(TAG, "load: APPEND request initiated")

                    // Get the remote key of the last item in the database
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    Log.d(TAG, "load: APPEND got remoteKeys: $remoteKeys")

                    if (remoteKeys == null) {
                        // If there are no keys yet (empty DB), start with page 1
                        // This handles the initial empty database case
                        val nextPageFromEmpty = 1
                        Log.d(TAG, "load: APPEND no remoteKeys found, using default page=$nextPageFromEmpty")
                        return loadAndSaveData(nextPageFromEmpty, loadType, state)
                    }

                    val nextPage = remoteKeys.nextPage
                    if (nextPage == null) {
                        Log.d(TAG, "load: APPEND nextPage is null, we've reached the end")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    Log.d(TAG, "load: APPEND, loading page $nextPage")
                    nextPage
                }
            }

            return loadAndSaveData(page, loadType, state)

        } catch (e: Exception) {
            Log.e(TAG, "load: Error loading data", e)
            Log.e(TAG, "load: Error message: ${e.message}")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun loadAndSaveData(page: Int, loadType: LoadType, state: PagingState<Int, PhotoEntity>): MediatorResult {
        try {
            // Fetch data from the API with proper page size
            val apiPageSize = state.config.pageSize.coerceAtMost(NETWORK_PAGE_SIZE)
            Log.d(TAG, "loadAndSaveData: Making API call to getCuratedPhotos(page=$page, perPage=$apiPageSize)")

            val response = api.getCuratedPhotos(page = page, perPage = apiPageSize)
            Log.d(TAG, "loadAndSaveData: API response received, photos count: ${response.photos.size}")

            if (response.photos.isNotEmpty()) {
                Log.d(TAG, "loadAndSaveData: First photo in response: id=${response.photos.first().id}")
                Log.d(TAG, "loadAndSaveData: Last photo in response: id=${response.photos.last().id}")
            }

            // Calculate whether we've reached the end of pagination
            val endOfPaginationReached = response.photos.isEmpty()

            // Calculate next and previous page numbers
            val nextPage = if (endOfPaginationReached) null else page + 1
            val prevPage = if (page > 1) page - 1 else null

            // Handle database transaction
            Log.d(TAG, "loadAndSaveData: Starting database transaction")
            db.withTransaction {
                // If refreshing, clear the existing data
                if (loadType == LoadType.REFRESH) {
                    Log.d(TAG, "loadAndSaveData: REFRESH mode, clearing existing data")
                    remoteKeysDao.clearRemoteKeys()
                    photoDao.clearAll()
                }

                // Convert API responses to entities
                val photoEntities = response.photos.map { PhotoMapper.mapToEntity(it) }
                Log.d(TAG, "loadAndSaveData: Converted ${photoEntities.size} API responses to entities")

                // Create and insert remote keys
                val keys = photoEntities.map { photo ->
                    RemoteKeys(
                        photoId = photo.id,
                        prevPage = prevPage,
                        currentPage = page,
                        nextPage = nextPage
                    )
                }

                // Insert keys and photos
                if (keys.isNotEmpty()) {
                    Log.d(TAG, "loadAndSaveData: Inserting ${keys.size} remote keys into database")
                    remoteKeysDao.insertAll(keys)
                }

                if (photoEntities.isNotEmpty()) {
                    Log.d(TAG, "loadAndSaveData: Inserting ${photoEntities.size} photos into database")
                    photoDao.insertALll(photoEntities)
                    Log.d(TAG, "loadAndSaveData: Successfully inserted photos into database")
                } else {
                    Log.w(TAG, "loadAndSaveData: No photos to insert into database")
                }
            }

            Log.d(TAG, "loadAndSaveData: End of pagination reached: $endOfPaginationReached")
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e(TAG, "loadAndSaveData: Error loading data", e)
            Log.e(TAG, "loadAndSaveData: Error message: ${e.message}")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        // Get the first page with data
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photo ->
                remoteKeysDao.getRemoteKeysById(photo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        // Get the last page with data
        val lastPage = state.pages.lastOrNull { it.data.isNotEmpty() }

        // Get the last item from that page
        val lastItem = lastPage?.data?.lastOrNull()

        // Return the remote keys associated with that item
        return lastItem?.let { photo ->
            remoteKeysDao.getRemoteKeysById(photo.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeysById(id)
            }
        }
    }
}