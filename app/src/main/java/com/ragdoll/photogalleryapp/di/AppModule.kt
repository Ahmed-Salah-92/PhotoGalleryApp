package com.ragdoll.photogalleryapp.di

import com.ragdoll.photogalleryapp.presentation.common.ConnectivityManagerNetworkMonitor
import com.ragdoll.photogalleryapp.presentation.common.NetworkMonitor
import org.koin.dsl.module

val appModule = module {
    single<NetworkMonitor> {
        ConnectivityManagerNetworkMonitor(get())
    }
}