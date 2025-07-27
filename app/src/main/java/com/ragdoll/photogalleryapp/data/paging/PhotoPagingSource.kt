package com.ragdoll.photogalleryapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ragdoll.photogalleryapp.data.mapper.PhotoMapper
import com.ragdoll.photogalleryapp.data.remote.api.PexelsApi
import com.ragdoll.photogalleryapp.domain.model.Photo
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(
    private val api: PexelsApi
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX
            val response = api.getCuratedPhotos(page = page, perPage = params.loadSize)

            val photos = response.photos.map { photoResponse ->
                PhotoMapper.mapToEntity(photoResponse).let { entity ->
                    PhotoMapper.mapToDomain(entity)
                }
            }

            LoadResult.Page(
                data = photos,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
