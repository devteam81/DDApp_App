package com.dd.app.dd.ui.transactions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.app.R
import com.dd.app.databinding.ActivityTransactionDetailBinding
import com.dd.app.dd.model.Invoice
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.videoDetail.VideoDetailActivity
import com.dd.app.dd.ui.wallet.InvoiceAdapter
import com.dd.app.dd.ui.wallet.WalletDetailModel
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class TransactionDetailActivity : AppCompatActivity() {

    companion object{
        private val TAG  = TransactionDetailActivity::class.simpleName.toString()
    }

    private lateinit var binding: ActivityTransactionDetailBinding
    private lateinit var walletDetailModel: WalletDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_transaction_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_detail)

        binding.toolbar.setNavigationOnClickListener { finish() }

        walletDetailModel = ViewModelProvider(this)[WalletDetailModel::class.java]

        binding.lifecycleOwner = this
        binding.rvVideoTransactions.layoutManager = LinearLayoutManager(this)
        observeViewModel()

    }

    private fun observeViewModel() {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            // Update the list when the data changes
            val id = LoginUser.ottId
            jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
            jsonObject.put("ott_id", LoginUser.ottId)

            //filter:1 for last month result, filter:2 for last 3 month, filter:3 for last year
            jsonObject.put("filter_id", 3)
            jsonObject.put("from_date", "")
            jsonObject.put("to_date", "")

            walletDetailModel.adminTransactionFilterForInvoice(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var invoiceList = listOf<Invoice>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    invoiceList = ParseResponseData.parseInvoice(mJSONArray)
                }

                val tempInvoiceList = invoiceList

                binding.rvVideoTransactions.layoutManager = LinearLayoutManager(this)
                binding.rvVideoTransactions.adapter = InvoiceAdapter(tempInvoiceList) { selectedItem: Invoice ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(tempInvoiceList.isEmpty()) {
                    binding.rvVideoTransactions.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvVideoTransactions.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })
        }
        else{
            //User

            val id = LoginUser.ottId
            jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
            jsonObject.put("ott_id", LoginUser.ottId)

            //filter:1 for last month result, filter:2 for last 3 month, filter:3 for last year
            jsonObject.put("filter_id", 3)
            jsonObject.put("from_date", "")
            jsonObject.put("to_date", "")

            walletDetailModel.transactionFilterForInvoice(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var invoiceList = listOf<Invoice>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    invoiceList = ParseResponseData.parseInvoice(mJSONArray)
                }

                val tempInvoiceList = invoiceList

                binding.rvVideoTransactions.layoutManager = LinearLayoutManager(this)
                binding.rvVideoTransactions.adapter = InvoiceAdapter(tempInvoiceList) { selectedItem: Invoice ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(tempInvoiceList.isEmpty()) {
                    binding.rvVideoTransactions.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvVideoTransactions.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })
        }
    }

    private fun listItemClicked(invoice: Invoice)
    {
        UiUtils.log(TAG, "Name: ${invoice._id}")
        val intent = Intent(this, VideoDetailActivity::class.java)
        UiUtils.log(TAG,"Pass ID: "+ invoice._id)
        intent.putExtra("video", invoice)
        //send whole model
        startActivity(intent)
    }

}