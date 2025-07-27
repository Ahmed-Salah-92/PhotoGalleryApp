package com.ragdoll.photogalleryapp.presentation.viewmodel

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ragdoll.photogalleryapp.domain.repository.PhotoRepository
import com.ragdoll.photogalleryapp.domain.usecase.GetPhotosUseCase
import com.ragdoll.photogalleryapp.presentation.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 4 paging
class PhotosViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val connectivityManager: ConnectivityManager,
    private val repository: PhotoRepository,
) : ViewModel() {

    val photos = repository.getPhotos().cachedIn(viewModelScope)


    private val _uiState = MutableStateFlow<UIState<Boolean>>(UIState.Loading)
    val uiState: StateFlow<UIState<Boolean>> get() = _uiState


    private val _isOnline = MutableStateFlow(checkInitialNetwork())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isOnline.value = true
        }

        override fun onLost(network: Network) {
            _isOnline.value = false
        }
    }

    init {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            try {
                if(_isOnline.value) {
                    _uiState.value = UIState.Success(true)
                } else {
                    _uiState.value = UIState.Error("No internet connection")
                }

            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun checkInitialNetwork(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onCleared() {
        super.onCleared()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}