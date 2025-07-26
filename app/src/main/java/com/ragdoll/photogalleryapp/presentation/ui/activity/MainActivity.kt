package com.ragdoll.photogalleryapp.presentation.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.ragdoll.photogalleryapp.R
import com.ragdoll.photogalleryapp.databinding.ActivityMainBinding
import com.ragdoll.photogalleryapp.presentation.adapter.PhotoAdapter
import com.ragdoll.photogalleryapp.presentation.viewmodel.PhotosViewModel
import com.ragdoll.photogalleryapp.presentation.viewmodel.PhotosViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: PhotosViewModelFactory
    @Inject
    lateinit var photoAdapter: PhotoAdapter
    lateinit var viewModel: PhotosViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val innerPadding = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            v.setPadding(innerPadding.left, innerPadding.top, innerPadding.right, innerPadding.bottom)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, viewModelFactory)[PhotosViewModel::class.java]

        setupNavigation()
    }

    private fun setupNavigation() {
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
    }
}