/*
package com.ragdoll.photogalleryapp.presentation.di

import com.ragdoll.photogalleryapp.data.api.PexelsAPIService
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsRemoteDataSource
import com.ragdoll.photogalleryapp.data.repository.dataSourceImpl.PexelsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun providePexelsRemoteDataSource(pexelsAPIService: PexelsAPIService): PexelsRemoteDataSource =
        PexelsRemoteDataSourceImpl(pexelsAPIService)
}*/
