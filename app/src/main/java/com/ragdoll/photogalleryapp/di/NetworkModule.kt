package com.ragdoll.photogalleryapp.di

import com.ragdoll.photogalleryapp.BuildConfig
import com.ragdoll.photogalleryapp.data.remote.api.PexelsApi
import com.ragdoll.photogalleryapp.data.remote.api.PexelsApi.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network module that provides networking related dependencies
 */
val networkModule = module {
    // OkHttpClient with logging
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        // Create a retry interceptor
        val retryInterceptor = Interceptor { chain ->
            val request = chain.request()
            var response = chain.proceed(request)
            var tryCount = 0
            val maxRetry = 2 // Maximum number of retries

            while (!response.isSuccessful && tryCount < maxRetry) {
                tryCount++
                // Wait before retrying (exponential backoff)
                Thread.sleep(1000L * tryCount)
                response.close()
                response = chain.proceed(request)
            }

            response
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(retryInterceptor)
            .addInterceptor { chain ->
                // Add API key to requests if needed
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", BuildConfig.PEXELS_API_KEY)
                    .method(originalRequest.method, originalRequest.body)

                chain.proceed(requestBuilder.build())
            }
            .connectTimeout(10, TimeUnit.SECONDS) // Reduced from 30 to 10 seconds
            .readTimeout(10, TimeUnit.SECONDS)    // Reduced from 30 to 10 seconds
            .writeTimeout(10, TimeUnit.SECONDS)   // Added write timeout
            .retryOnConnectionFailure(true)       // Enable retries on connection failures
            .build()
    }

    // Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Replace with your base URL
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Services
    // Example: single { get<Retrofit>().create(YourApiService::class.java) }
    single { get<Retrofit>().create(PexelsApi::class.java) }
}
