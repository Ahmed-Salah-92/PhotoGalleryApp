package com.ragdoll.photogalleryapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ragdoll.photogalleryapp.data.model.APIResponse
import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.data.paging.PhotoPagingSource
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsLocalDataSource
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsRemoteDataSource
import com.ragdoll.photogalleryapp.data.util.Resource
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response

// 7    3 paging
class PexelsRepositoryImpl(
    private val pexelsRemoteDataSource: PexelsRemoteDataSource,
    private val pexelsLocalDataSource: PexelsLocalDataSource
) : PexelsRepository {

    private fun responseToResource(apiResponse: Response<APIResponse>): Resource<APIResponse> {
        if (apiResponse.isSuccessful)
            apiResponse.body()?.let { result ->
                return Resource.Success(result)
            }
        return Resource.Error(apiResponse.message())
    }


    override suspend fun fetchPhotos(page: Int, perPage: Int): Resource<APIResponse> {
        val response = pexelsRemoteDataSource.fetchPhotos(page, perPage)
        if (response.isSuccessful) {
            response.body()?.photos?.let { pexelsLocalDataSource.upsertPhoto(it) }
            val localPhotos = pexelsLocalDataSource.getPhoto().first()
            return Resource.Success(
                APIResponse(
                    page = page,
                    perPage = perPage,
                    photos = localPhotos,
                    totalResults = response.body()?.totalResults ?: 0,
                    prevPage = "${page - 1}", // If changing this type to Int ==> (page - 1)
                    nextPage = "${page + 1}" // If changing this type to Int ==> (page + 1)
                )
            )
        }
        return responseToResource(response)
    }

    override fun getPhotos(): Flow<List<Photo>> = flow {
        emitAll(pexelsLocalDataSource.getPhoto())
    }

    override fun getPagedPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = { PhotoPagingSource(this) }
        ).flow
    }

}