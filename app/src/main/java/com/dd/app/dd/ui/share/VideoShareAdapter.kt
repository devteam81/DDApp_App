package com.dd.app.dd.ui.share

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.ItemVideoShareBinding
import com.bumptech.glide.Glide
import com.dd.digitaldistribution.data.model.VideoShare

class VideoShareAdapter(private val context: Context, private val videosList: List<VideoShare>, private val clickListener: (VideoShare) -> Unit) : RecyclerView.Adapter<ViewHolderVideo>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderVideo {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemVideoShareBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_video_share,parent,false)
        //binding.image.layoutParams = ResponsivenessUtils.getHorizontalResizeImage(context)
        return ViewHolderVideo(context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolderVideo, position: Int) {

        holder.bind(videosList[position], clickListener, position)
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
}

class ViewHolderVideo(private val context: Context, private val binding: ItemVideoShareBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(videoShare: VideoShare, clickListener: (VideoShare) -> Unit, position: Int)
    {
        binding.videoShare = videoShare

        if (position % 2 == 0) {
            binding.rlBackground.background = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.gradient_big_dark,
                null
            )
        } else {
            binding.rlBackground.background = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.gradient_big_light,
                null
            )
        }


        Glide.with(itemView)  //2
            .load(videoShare.browseImage) //3
            //.centerCrop() //4
            .placeholder(R.drawable.bebu_placeholder_horizontal) //5
            .error(R.drawable.bebu_placeholder_horizontal) //6
            .fallback(R.drawable.bebu_placeholder_horizontal) //7
            //.transform(CircleCrop()) //4
            .into(binding.image) //8

        binding.root.setOnClickListener {
            clickListener(videoShare)
        }
    }

}