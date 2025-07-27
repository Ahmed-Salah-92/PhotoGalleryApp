package com.ragdoll.photogalleryapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ragdoll.photogalleryapp.databinding.PhotoListItemBinding
import com.ragdoll.photogalleryapp.domain.model.Photo

class PhotoAdapter : PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((Photo) -> Unit)? = null

    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            PhotoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(
        private val binding: PhotoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo?) {
            photo?.let { currentPhoto ->
                binding.apply {
                    this.photo = currentPhoto
                    Glide.with(photoImv.context)
                        .load(currentPhoto.src.medium)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(photoImv)

                    root.setOnClickListener {
                        onItemClickListener?.invoke(currentPhoto)
                    }
                }
                binding.executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}
