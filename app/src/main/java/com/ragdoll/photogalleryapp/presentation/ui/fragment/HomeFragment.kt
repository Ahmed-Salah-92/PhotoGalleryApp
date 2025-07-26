package com.ragdoll.photogalleryapp.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ragdoll.photogalleryapp.R
import com.ragdoll.photogalleryapp.data.util.Resource
import com.ragdoll.photogalleryapp.databinding.FragmentHomeBinding
import com.ragdoll.photogalleryapp.presentation.adapter.PhotoAdapter
import com.ragdoll.photogalleryapp.presentation.ui.activity.MainActivity
import com.ragdoll.photogalleryapp.presentation.viewmodel.PhotosViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 8 paging
class HomeFragment : Fragment() {
    private lateinit var homeFragmentBinding: FragmentHomeBinding
    private lateinit var viewModel: PhotosViewModel
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentBinding = FragmentHomeBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        photoAdapter = (activity as MainActivity).photoAdapter

        initRecyclerView()
        //isNetworkAvailable()
        navigateToDetailsFragment()

        lifecycleScope.launch {
            viewModel.pagedPhotos.collectLatest { pagingData ->
                photoAdapter.submitData(lifecycle, pagingData)
            }
        }

    }

    private fun initRecyclerView() = homeFragmentBinding.photosRV.apply {
        adapter = photoAdapter
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }

    /*private fun isNetworkAvailable() {
        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable)
                viewPhotosList()
            *//*else if (photoAdapter.submitData().isNotEmpty())
                lifecycleScope.launch {
                    viewModel.getPhotos().collect { photos ->
                        showRecyclerView()
                        photoAdapter.submitData(photos)
                    }
                }
            else
                showNoPhotosFound().also {
                    homeFragmentBinding.noPhotosTv.apply {
                        text = getString(R.string.no_internet_connection)
                    }
                    Snackbar.make(
                        homeFragmentBinding.root,
                        R.string.no_internet_connection,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Settings") {
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }.show()
                }*//*
        }
    }*/

    /*private fun viewPhotosList() {
        viewModel.fetchPhotos(2, 40)
        viewModel.photos.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                    hideNoPhotosFound()
                    hideRecyclerView()
                }

                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let {
                        if (it.photos.isEmpty()) {
                            showNoPhotosFound()
                            homeFragmentBinding.noPhotosTv.apply {
                                text = getString(R.string.no_photos_found)
                            }
                        } else {
                            hideNoPhotosFound()
                            showRecyclerView()
                        }
                        //photoAdapter.submitData(it.photos)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    hideRecyclerView()
                    showNoPhotosFound()
                    response.message.let { message ->
                        homeFragmentBinding.noPhotosTv.apply {
                            text = message
                        }
                    }
                }
            }
        }
    }*/

    private fun showProgressBar() {
        homeFragmentBinding.loadingProgressIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        homeFragmentBinding.loadingProgressIndicator.visibility = View.INVISIBLE
    }

    private fun showNoPhotosFound() {
        homeFragmentBinding.noPhotosTv.visibility = View.VISIBLE
    }

    private fun hideNoPhotosFound() {
        homeFragmentBinding.noPhotosTv.visibility = View.INVISIBLE
    }

    private fun hideRecyclerView() {
        homeFragmentBinding.photosRV.visibility = View.INVISIBLE
    }

    private fun showRecyclerView() {
        homeFragmentBinding.photosRV.visibility = View.VISIBLE
    }

    private fun navigateToDetailsFragment() = photoAdapter.setOnItemClickListener { photo ->
        if (photo.src.original.isNotEmpty()) {
            val bundle = Bundle().apply {
                putSerializable("selected_photo", photo)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment, bundle
            )
        } else Snackbar.make(
            requireView(), "Invalid Photo Data", Snackbar.LENGTH_SHORT
        ).show()
    }

}