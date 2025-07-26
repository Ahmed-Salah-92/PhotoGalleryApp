package com.ragdoll.photogalleryapp.presentation.di

import com.ragdoll.photogalleryapp.data.repository.PexelsRepositoryImpl
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsLocalDataSource
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsRemoteDataSource
import com.ragdoll.photogalleryapp.domain.repository.PexelsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provivedPexelsRepository(
        pexelsRemoteDataSource: PexelsRemoteDataSource,
        pexelsLocalDataSource: PexelsLocalDataSource
    ): PexelsRepository =
        PexelsRepositoryImpl(pexelsRemoteDataSource, pexelsLocalDataSource)
}