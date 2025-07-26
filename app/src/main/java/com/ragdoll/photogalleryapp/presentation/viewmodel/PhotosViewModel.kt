package com.ragdoll.photogalleryapp.presentation.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ragdoll.photogalleryapp.R
import com.ragdoll.photogalleryapp.data.model.APIResponse
import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.data.util.Resource
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import com.ragdoll.photogalleryapp.domain.usecase.FetchPexelsPhotosUseCase
import com.ragdoll.photogalleryapp.domain.usecase.GetPexelsPhotosUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// 4 paging
class PhotosViewModel(
    private val app: Application,
    private val fetchPexelsPhotosUseCase: FetchPexelsPhotosUseCase,
    private val getPexelsPhotosUseCase: GetPexelsPhotosUseCase,
    private val connectivityManager: ConnectivityManager,
    private val repository: PexelsRepository,
) : AndroidViewModel(app) {

    val pagedPhotos: Flow<PagingData<Photo>> = repository.getPagedPhotos()
        .cachedIn(viewModelScope)
    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean> get() = _isNetworkAvailable

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isNetworkAvailable.postValue(true)
        }

        override fun onLost(network: Network) {
            _isNetworkAvailable.postValue(false)
        }
    }

    init {
        // initialize network availability check
        _isNetworkAvailable.value = checkInitialNetwork()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun checkInitialNetwork(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private val _photos = MutableLiveData<Resource<APIResponse>>()
    val photos: LiveData<Resource<APIResponse>> get() = _photos

    fun fetchPhotos(page: Int, perPage: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            _photos.postValue(Resource.Loading)
            try {
                if (checkInitialNetwork()) {
                    val apiResponse = fetchPexelsPhotosUseCase.execute(page, perPage)
                    _photos.postValue(apiResponse)
                } else {
                    _photos.postValue(Resource.Error(app.getString(R.string.no_internet_connection)))
                }
            } catch (e: IOException) {
                _photos.postValue(Resource.Error(buildString {
                    append(app.getString(R.string.network_error))
                    append(e.message.toString())
                }))
            } catch (e: HttpException) {
                _photos.postValue(Resource.Error(buildString {
                    append(app.getString(R.string.server_error))
                    append(e.message.toString())
                }))
            } catch (e: Exception) {
                _photos.postValue(Resource.Error(buildString {
                    append(app.getString(R.string.unexpected_error))
                    append(e.message.toString())
                }))
            }

        }

    fun getPhotos(): Flow<List<Photo>> = getPexelsPhotosUseCase.execute()

    fun refreshPhotos(page: Int = 1, perPage: Int) =
        fetchPhotos(page, perPage)

    override fun onCleared() {
        super.onCleared()
        // Unregister the network callback to avoid memory leaks
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}