package com.ragdoll.photogalleryapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ragdoll.photogalleryapp.domain.usecase.GetPhotosUseCase
import com.ragdoll.photogalleryapp.presentation.common.NetworkMonitor
import com.ragdoll.photogalleryapp.presentation.common.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<Boolean>>(UIState.Loading)
    val uiState: StateFlow<UIState<Boolean>> get() = _uiState

    val photos = getPhotosUseCase().cachedIn(viewModelScope)

    val isOnline = networkMonitor.isOnline

    init {
        viewModelScope.launch {
            isOnline.collect { isConnected ->
                _uiState.value =
                    if (isConnected)
                        UIState.Success(true)
                    else
                        UIState.Error("No internet connection")
            }
        }
    }
}