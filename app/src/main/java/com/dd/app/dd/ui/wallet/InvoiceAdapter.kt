package com.dd.app.dd.ui.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.ItemInvoiceBinding
import com.dd.app.dd.model.Invoice

class InvoiceAdapter(private val invoiceList: List<Invoice>, private val clickListener: (Invoice) -> Unit) : RecyclerView.Adapter<ViewHolderInvoice>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInvoice {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemInvoiceBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_invoice,parent,false)
        return ViewHolderInvoice(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderInvoice, position: Int) {
        holder.bind(invoiceList[position], clickListener, position)
    }

    override fun getItemCount(): Int {
        return invoiceList.size
    }
}

class ViewHolderInvoice(private val binding: ItemInvoiceBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(invoice: Invoice, clickListener: (Invoice) -> Unit, position: Int) {
        binding.invoice = invoice

        if (position % 2 == 0) {
            binding.llBackground.background = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.gradient_small_dark,
                null
            )
        } else {
            binding.llBackground.background = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.gradient_small_light,
                null
            )
        }

/*Glide.with(itemView)  //2
    .load(invoice.thumbnail) //3
    //.centerCrop() //4
    .placeholder(R.drawable.bebu_placeholder_horizontal) //5
    .error(R.drawable.bebu_placeholder_horizontal) //6
    .fallback(R.drawable.bebu_placeholder_horizontal) //7
    //.transform(CircleCrop()) //4
    .into(binding.image) //8*/

//binding.title.text = invoice.transAmt

//        binding.root.setOnClickListener {
//            clickListener(invoice)
//        }
    }

}