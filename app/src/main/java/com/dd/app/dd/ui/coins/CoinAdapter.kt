package com.dd.app.dd.ui.coins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.ItemCoinBinding
import com.dd.app.dd.model.CoinHistory
import com.bumptech.glide.Glide

class CoinAdapter(private val videoCoinList: List<CoinHistory>, private val clickListener: (CoinHistory) -> Unit) : RecyclerView.Adapter<ViewHolderCoin>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCoin {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCoinBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_coin,parent,false)
        return ViewHolderCoin(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderCoin, position: Int) {

        holder.bind(videoCoinList[position], clickListener,position)
    }

    override fun getItemCount(): Int {
        return videoCoinList.size
    }
}

class ViewHolderCoin(private val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(coinHistory: CoinHistory, clickListener: (CoinHistory) -> Unit, position: Int)
    {
        binding.coinHistory = coinHistory

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
            .load(coinHistory.thumbnail) //3
            //.centerCrop() //4
            .placeholder(R.drawable.bebu_placeholder_horizontal) //5
            .error(R.drawable.bebu_placeholder_horizontal) //6
            .fallback(R.drawable.bebu_placeholder_horizontal) //7
            //.transform(CircleCrop()) //4
            .into(binding.image) //8

        binding.root.setOnClickListener {
            clickListener(coinHistory)
        }
    }

}