package com.dd.app.dd.ui.videoDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.ItemVideoOtherPurchaseBinding
import com.bumptech.glide.Glide
import com.dd.app.dd.model.VideoPurchase

class VideoAdapter(private val videosList: List<VideoPurchase>, private val clickListener: (VideoPurchase) -> Unit) : RecyclerView.Adapter<ViewHolderVideoAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderVideoAdapter {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemVideoOtherPurchaseBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_video_other_purchase,parent,false)
        return ViewHolderVideoAdapter(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderVideoAdapter, position: Int) {

        holder.bind(videosList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
}

class ViewHolderVideoAdapter(private val binding: ItemVideoOtherPurchaseBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(videoPurchase: VideoPurchase, clickListener: (VideoPurchase) -> Unit)
    {
        binding.videoPurchaseDetail = videoPurchase

        Glide.with(itemView)  //2
            .load(videoPurchase.thumbnail) //3
            //.centerCrop() //4
            .placeholder(R.drawable.bebu_placeholder_horizontal) //5
            .error(R.drawable.bebu_placeholder_horizontal) //6
            .fallback(R.drawable.bebu_placeholder_horizontal) //7
            //.transform(CircleCrop()) //4
            .into(binding.image) //8

        binding.root.setOnClickListener {
            clickListener(videoPurchase)
        }
    }

}