package com.ragdoll.photogalleryapp

import android.app.Application
import com.ragdoll.photogalleryapp.di.appModule
import com.ragdoll.photogalleryapp.di.databaseModule
import com.ragdoll.photogalleryapp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            // Use Android logger - Level.ERROR is recommended for production
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR)
            // Provide Android context
            androidContext(this@PhotoGalleryApplication)
            // Load Koin modules
            modules(
                listOf(
                    appModule,
                    networkModule,
                    databaseModule,
                )
            )
        }
    }
}
