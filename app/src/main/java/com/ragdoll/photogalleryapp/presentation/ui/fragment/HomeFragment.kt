package com.ragdoll.photogalleryapp.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ragdoll.photogalleryapp.R
import com.ragdoll.photogalleryapp.databinding.FragmentHomeBinding
import com.ragdoll.photogalleryapp.presentation.adapter.PhotoAdapter
import com.ragdoll.photogalleryapp.presentation.ui.activity.MainActivity
import com.ragdoll.photogalleryapp.presentation.viewmodel.PhotosViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

// 8 paging
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: PhotosViewModel
    private val photoAdapter = PhotoAdapter()
    private val TAG = "HomeFragment"

    // Track the previous network state to detect changes
    private var previousNetworkState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        Log.d(TAG, "onViewCreated: ViewModel initialized")

        initRecyclerView()
        observeLoadStates() // Add load state observer
        observePhotos()
        observeNetworkState()
        setupPhotoClickListener()

        // Initially show loading, but this will be controlled by the load state observer
        binding.loadingProgressIndicator.visibility = View.VISIBLE
    }

    private fun initRecyclerView() = binding.photosRV.apply {
        adapter = photoAdapter
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        Log.d(TAG, "initRecyclerView: RecyclerView initialized with adapter")
    }

    // Add a new function to observe adapter load states
    private fun observeLoadStates() {
        lifecycleScope.launch {
            photoAdapter.loadStateFlow
                .distinctUntilChanged()
                .collect { loadStates ->
                    Log.d(TAG, "observeLoadStates: Load state changed: ${loadStates.refresh}")

                    // Handle loading state
                    when (loadStates.refresh) {
                        is LoadState.Loading -> {
                            binding.loadingProgressIndicator.visibility = View.VISIBLE
                            binding.errorLayout.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            val error = (loadStates.refresh as LoadState.Error).error
                            Log.e(TAG, "observeLoadStates: Error loading data", error)
                            binding.loadingProgressIndicator.visibility = View.GONE

                            // Show appropriate error message based on error type
                            if (!viewModel.isOnline.value) {
                                showErrorView(
                                    getString(R.string.no_internet_connection),
                                    getString(R.string.check_connection_and_try_again)
                                )
                            } else {
                                when (error) {
                                    is java.net.SocketTimeoutException -> {
                                        showErrorView(
                                            getString(R.string.connection_timeout),
                                            getString(R.string.server_taking_too_long)
                                        )
                                    }
                                    is java.io.IOException -> {
                                        showErrorView(
                                            getString(R.string.network_error),
                                            getString(R.string.could_not_connect_to_server)
                                        )
                                    }
                                    else -> {
                                        showErrorView(
                                            getString(R.string.unknown_error),
                                            error.message ?: getString(R.string.something_went_wrong)
                                        )
                                    }
                                }
                            }
                        }
                        is LoadState.NotLoading -> {
                            binding.loadingProgressIndicator.visibility = View.GONE
                            binding.errorLayout.visibility = View.GONE

                            if (photoAdapter.itemCount == 0) {
                                binding.noPhotosTv.apply {
                                    text = getString(R.string.no_photos_available)
                                    visibility = View.VISIBLE
                                }
                            } else {
                                binding.noPhotosTv.visibility = View.GONE
                                binding.photosRV.visibility = View.VISIBLE
                            }
                        }
                    }
                }
        }
    }

    private fun showErrorView(title: String, message: String) {
        binding.apply {
            errorTitle.text = title
            errorMessage.text = message
            errorLayout.visibility = View.VISIBLE
            photosRV.visibility = if (photoAdapter.itemCount == 0) View.GONE else View.VISIBLE
            retryButton.setOnClickListener {
                errorLayout.visibility = View.GONE
                loadingProgressIndicator.visibility = View.VISIBLE
                photoAdapter.refresh()
            }
        }
    }

    private fun observePhotos() {
        Log.d(TAG, "observePhotos: Starting to observe photos flow")
        lifecycleScope.launch {
            viewModel.photos.collectLatest { pagingData ->
                Log.d(TAG, "observePhotos: Received paging data")
                photoAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeNetworkState() {
        Log.d(TAG, "observeNetworkState: Starting to observe network state")
        lifecycleScope.launch {
            viewModel.isOnline.collect { isOnline ->
                Log.d(TAG, "observeNetworkState: Network state changed, isOnline = $isOnline")

                // Handle the case when network becomes available (was offline, now online)
                val networkBecameAvailable = !previousNetworkState && isOnline
                previousNetworkState = isOnline

                if (!isOnline && photoAdapter.itemCount == 0) {
                    binding.noPhotosTv.apply {
                        text = getString(R.string.no_internet_connection)
                        visibility = View.VISIBLE
                    }
                    binding.photosRV.visibility = View.GONE
                } else if (isOnline) {
                    binding.noPhotosTv.visibility = View.GONE
                    binding.photosRV.visibility = View.VISIBLE

                    // If network just became available, trigger a refresh
                    if (networkBecameAvailable) {
                        Log.d(TAG, "observeNetworkState: Network became available, refreshing data")
                        photoAdapter.refresh()
                    }
                }
            }
        }
    }

    private fun setupPhotoClickListener() {
        photoAdapter.setOnItemClickListener { photo ->
            // Ensure loading indicator is hidden before navigation
            binding.loadingProgressIndicator.visibility = View.GONE

            val bundle = Bundle().apply {
                putString("photo_photographer", photo.photographer)
                putString("photo_original_url", photo.src.original)
                putString("photo_alt", photo.alt)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment, bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Ensure loading indicator is hidden when fragment is destroyed
        if (::binding.isInitialized) {
            binding.loadingProgressIndicator.visibility = View.GONE
        }
    }
}