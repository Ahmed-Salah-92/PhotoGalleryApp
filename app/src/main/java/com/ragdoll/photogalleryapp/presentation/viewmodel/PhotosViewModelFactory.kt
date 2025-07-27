package com.ragdoll.photogalleryapp.presentation.viewmodel

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ragdoll.photogalleryapp.domain.repository.PhotoRepository
import com.ragdoll.photogalleryapp.domain.usecase.GetPhotosUseCase

// 5 paging
class PhotosViewModelFactory(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val connectivityManager: ConnectivityManager,
    private val repository: PhotoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotosViewModel(
            getPhotosUseCase,
            connectivityManager,
            repository,
        ) as T
    }
}
