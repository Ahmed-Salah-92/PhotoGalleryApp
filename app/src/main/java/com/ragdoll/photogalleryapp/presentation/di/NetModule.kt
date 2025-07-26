package com.ragdoll.photogalleryapp.presentation.di

import com.ragdoll.photogalleryapp.data.api.PexelsAPIService
import com.ragdoll.photogalleryapp.data.api.PexelsAPIService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providePexelsAPIService(retrofit: Retrofit): PexelsAPIService =
        retrofit.create(PexelsAPIService::class.java)
}