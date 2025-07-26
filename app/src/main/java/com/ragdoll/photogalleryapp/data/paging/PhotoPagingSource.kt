package com.ragdoll.photogalleryapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.data.util.Resource
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository

// 1 paging
class PhotoPagingSource(
    private val repository: PexelsRepository,
    private val query: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        val response = repository.fetchPhotos(page, params.loadSize)
        return try {
            if (response is Resource.Success) {
                val photos = response.data?.photos ?: emptyList()
                LoadResult.Page(
                    data = photos,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (photos.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to Load photos"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        TODO("Not yet implemented")
    }

}