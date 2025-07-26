/*
package com.ragdoll.photogalleryapp.presentation.di

import com.ragdoll.photogalleryapp.data.db.PhotoDAO
import com.ragdoll.photogalleryapp.data.repository.dataSource.PexelsLocalDataSource
import com.ragdoll.photogalleryapp.data.repository.dataSourceImpl.PexelsLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    @Singleton
    @Provides
    fun provideLocalDataSource(photoDoa: PhotoDAO) : PexelsLocalDataSource =
        PexelsLocalDataSourceImpl(photoDoa)
}*/
