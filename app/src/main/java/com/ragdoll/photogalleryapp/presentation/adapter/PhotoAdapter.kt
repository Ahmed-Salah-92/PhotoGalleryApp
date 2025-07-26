package com.ragdoll.photogalleryapp.presentation.adapter

// 7 paging
/*
class PhotoAdapter : PagingDataAdapter<Photo, PhotoVH>(DIFF_CALLBACK) {

    private var onItemClickListener: ((Photo) -> Unit)? = null

    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val binding = PhotoListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoVH(binding)
    }


    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        holder.bind(getItem(position))
    }


    inner class PhotoVH(val binding: PhotoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo?) {
            try {
                val colorString: String = photo?.avgColor ?: "#000000" // Default to black if avgColor is null
                val placeholderColor: Int = colorString.toColorInt()
//                Glide
//                    .with(binding.photoImv.context)
//                    .load(photo?.src?.medium)
//                    .placeholder(placeholderColor.toDrawable())
//                    //.error(R.drawable.ic_broken_image)
//                    .into(binding.photoImv)
            } catch (e: Exception) {
                e.message.toString()
            }
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(photo?: return@setOnClickListener)
                }
            }
        }
    }
}*/
