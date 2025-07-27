package com.ragdoll.photogalleryapp.di

import androidx.room.Room
import com.ragdoll.photogalleryapp.data.local.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Database module that provides database related dependencies
 */
val databaseModule = module {
    // Room database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "photo_gallery_database"
        ).fallbackToDestructiveMigration()
         .build()
    }

    // DAOs
    // Example: single { get<AppDatabase>().photoDao() }
    single { get<AppDatabase>().photoDao() }
}
