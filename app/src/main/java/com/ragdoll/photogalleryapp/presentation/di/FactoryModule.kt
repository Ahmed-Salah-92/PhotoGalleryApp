/*
package com.ragdoll.photogalleryapp.presentation.di

import android.app.Application
import android.net.ConnectivityManager
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import com.ragdoll.photogalleryapp.domain.usecase.FetchPexelsPhotosUseCase
import com.ragdoll.photogalleryapp.domain.usecase.GetPexelsPhotosUseCase
import com.ragdoll.photogalleryapp.presentation.viewmodel.PhotosViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
// 6 paging
@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {
    @Singleton
    @Provides
    fun providePhotosViewModelFactory(
        application: Application,
        fetchPexelsPhotosUseCase: FetchPexelsPhotosUseCase,
        getPexelsPhotosUseCase: GetPexelsPhotosUseCase,
        connectivityManager: ConnectivityManager,
        repository: PexelsRepository,
    ): PhotosViewModelFactory = PhotosViewModelFactory(
        application,
        fetchPexelsPhotosUseCase,
        getPexelsPhotosUseCase,
        connectivityManager,
        repository
    )
}*/
