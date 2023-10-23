package com.dd.app.dd.ui.purchase

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.ItemVideoPurchaseBinding
import com.bumptech.glide.Glide
import com.dd.app.dd.model.VideoPurchase

class VideoPurchaseAdapter(private val videosList: List<VideoPurchase>, private val clickListener: (VideoPurchase) -> Unit) : RecyclerView.Adapter<ViewHolderVideoPurchase>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderVideoPurchase {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemVideoPurchaseBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_video_purchase,parent,false)
        return ViewHolderVideoPurchase(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderVideoPurchase, position: Int) {

        holder.bind(videosList[position], clickListener, position)
    }

    override fun getItemCount(): Int {
        return videosList.size
    }
}

class ViewHolderVideoPurchase(private val binding: ItemVideoPurchaseBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(videoPurchase: VideoPurchase, clickListener: (VideoPurchase) -> Unit, position: Int)
    {
        binding.videoPurchase = videoPurchase

        Log.d("testgmm", "bind1: "+videoPurchase.thumbnail)
        Log.d("testgmm", "bind2: "+videoPurchase.mobile_image)
        Log.d("bind_test", "bind: "+videoPurchase.shareLink)

        if (position % 2 == 0) {
            binding.rlBackground.background = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.gradient_big_dark,
                null)
        } else {
            binding.rlBackground.background = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.gradient_big_light, null)
        }

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