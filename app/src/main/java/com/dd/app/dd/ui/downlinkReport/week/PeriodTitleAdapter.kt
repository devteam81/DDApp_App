package com.dd.app.dd.ui.downlinkReport.week

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.ItemPeriodTitleBinding
import com.dd.app.dd.model.Period


class PeriodTitleAdapter(private val context: Context, private val periodList: List<Period>, private val clickListener: (Period) -> Unit) :
    RecyclerView.Adapter<ViewHolderPeriod>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPeriod {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPeriodTitleBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_period_title,parent,false)
        return ViewHolderPeriod(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderPeriod, position: Int) {

        holder.bind(context,periodList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return periodList.size
    }
}


class ViewHolderPeriod(private val binding: ItemPeriodTitleBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(context:Context, period: Period, clickListener: (Period) -> Unit)
    {
        binding.period = period

        if(period.isSelected)
            binding.title.backgroundTintList = ContextCompat.getColorStateList(context, R.color.button_border)
        else
            binding.title.backgroundTintList = ContextCompat.getColorStateList(context, R.color.level_color)


        //binding.title.text = period.title

        binding.root.setOnClickListener {
            clickListener(period)
        }
    }

}