package com.ragdoll.photogalleryapp.presentation.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import com.ragdoll.photogalleryapp.domain.usecase.FetchPexelsPhotosUseCase
import com.ragdoll.photogalleryapp.domain.usecase.GetPexelsPhotosUseCase
// 5 paging
class PhotosViewModelFactory(
    private val app: Application,
    private val fetchPexelsPhotosUseCase: FetchPexelsPhotosUseCase,
    private val getPexelsPhotosUseCase: GetPexelsPhotosUseCase,
    private val connectivityManager: ConnectivityManager,
    private val repository: PexelsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotosViewModel(
            app,
            fetchPexelsPhotosUseCase,
            getPexelsPhotosUseCase,
            connectivityManager,
            repository
        ) as T
    }
}