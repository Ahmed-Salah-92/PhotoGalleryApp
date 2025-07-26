package com.ragdoll.photogalleryapp.presentation.di

import com.ragdoll.photogalleryapp.presentation.adapter.PhotoAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {
    @Singleton
    @Provides
    fun providePhotoAdapter(): PhotoAdapter = PhotoAdapter()
}