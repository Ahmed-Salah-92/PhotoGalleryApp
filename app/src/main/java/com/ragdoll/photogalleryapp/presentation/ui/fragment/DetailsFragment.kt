package com.ragdoll.photogalleryapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ragdoll.photogalleryapp.R
import com.ragdoll.photogalleryapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var detailsFragmentBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsFragmentBinding = FragmentDetailsBinding.bind(view)
        val args: DetailsFragmentArgs by navArgs()
        val photoDetails = args.selectedPhoto

        /*Glide
            .with(detailsFragmentBinding.imageView.context)
            .load(photoDetails.src.original)
            .placeholder(photoDetails.avgColor.toColorInt().toDrawable())
            .into(detailsFragmentBinding.imageView)*/

        detailsFragmentBinding.apply {
            photoNameTv.text = photoDetails.photographer
            if (descTv.text.isEmpty())
                descTv.text = getString(R.string.no_description_available)
            else
                descTv.text = photoDetails.alt

        }
    }
}