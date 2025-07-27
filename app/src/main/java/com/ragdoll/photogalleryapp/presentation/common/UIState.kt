package com.ragdoll.photogalleryapp.presentation.common

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    object Empty : UIState<Nothing>()
    object Refreshing : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}