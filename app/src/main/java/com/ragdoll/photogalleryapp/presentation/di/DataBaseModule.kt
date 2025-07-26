/*
package com.ragdoll.photogalleryapp.presentation.di

import android.app.Application
import androidx.room.Room
import com.ragdoll.photogalleryapp.data.db.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application): PhotoDatabase = Room
        .databaseBuilder(app, PhotoDatabase::class.java, "photos_db")
        .fallbackToDestructiveMigration(false)
        .build()

    @Singleton
    @Provides
    fun providePhotoDao(photoDatabase: PhotoDatabase) =
        photoDatabase.getPhotoDAO()
}*/
