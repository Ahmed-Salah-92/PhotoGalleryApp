package com.ragdoll.photogalleryapp.di

import android.content.Context
import android.net.ConnectivityManager
import com.ragdoll.photogalleryapp.data.repository.PhotoRepositoryImpl
import com.ragdoll.photogalleryapp.domain.repository.PhotoRepository
import com.ragdoll.photogalleryapp.domain.usecase.GetPhotosUseCase
import com.ragdoll.photogalleryapp.presentation.common.ConnectivityManagerNetworkMonitor
import com.ragdoll.photogalleryapp.presentation.common.NetworkMonitor
import com.ragdoll.photogalleryapp.presentation.viewmodel.PhotosViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * Main application module that defines all dependencies for the app
 */
val appModule = module {
    // ViewModels
    // Example: viewModel { YourViewModel(get()) }
    viewModel {
        PhotosViewModel(
            get(),
            get(),
            get(),
        )
    }
//    viewModelFactory { PhotosViewModelFactory(get(), get(), get()) }

    // Use Cases
    // Example: single { YourUseCase(get()) }
    single {
        GetPhotosUseCase(get())
    }

    // Repositories
    // Example: single { YourRepository(get()) }
    single {
        PhotoRepositoryImpl(
            get(),
            get(),
            get(),
        ) as PhotoRepository
    }

    // Data Sources
    // Example: single { YourRemoteDataSource(get()) }

    // API Services
    // Example: single { createApiService<YourApiService>(get()) }


    // Database and DAOs
    // Example: single { get<AppDatabase>().yourDao() }

    // Utilities and other dependencies
    // Example: single { YourUtility() }

    // NetworkMonitor dependency
    single<NetworkMonitor> {
        ConnectivityManagerNetworkMonitor(androidContext())
    }

    // System services
    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
