package com.ragdoll.photogalleryapp

import android.app.Application
import com.ragdoll.photogalleryapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PhotoGalleryApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PhotoGalleryApp)
            modules(appModule)
        }
    }
}
