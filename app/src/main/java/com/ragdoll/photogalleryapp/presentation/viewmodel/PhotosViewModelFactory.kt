package com.ragdoll.photogalleryapp.presentation.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ragdoll.photogalleryapp.domain.repository.PhotoRepository
import com.ragdoll.photogalleryapp.domain.usecase.GetPhotosUseCase
// 5 paging
class PhotosViewModelFactory(
    private val app: Application,
   // private val fetchPexelsPhotosUseCase: FetchPexelsPhotosUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val connectivityManager: ConnectivityManager,
    private val repository: PhotoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotosViewModel(
            app,
           // fetchPexelsPhotosUseCase,
            getPhotosUseCase,
            connectivityManager,
            repository
        ) as T
    }
}