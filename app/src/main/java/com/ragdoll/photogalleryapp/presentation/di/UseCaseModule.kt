package com.ragdoll.photogalleryapp.presentation.di

import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import com.ragdoll.photogalleryapp.domain.usecase.FetchPexelsPhotosUseCase
import com.ragdoll.photogalleryapp.domain.usecase.GetPexelsPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideFetchPhotosUseCase(pexelsRepository: PexelsRepository): FetchPexelsPhotosUseCase =
        FetchPexelsPhotosUseCase(pexelsRepository)

    @Singleton
    @Provides
    fun provideGetPhotosUseCase(pexelsRepository: PexelsRepository): GetPexelsPhotosUseCase =
        GetPexelsPhotosUseCase(pexelsRepository)
}