package com.dd.app.dd.ui.wallet

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.WalletRepository

import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Response

class WalletDetailModel : ViewModel() {

    private val TAG  = WalletDetailModel::class.simpleName.toString()

    var walletAddDetailLiveData: MutableLiveData<ResponseBody>? = null
    var walletDetailLiveData: MutableLiveData<ResponseBody>? = null
    var withdrawWalletAmountLiveData: MutableLiveData<ResponseBody>? = null
    var transactionInvoiceLiveData: MutableLiveData<ResponseBody>? = null
    var adminTransactionInvoiceLiveData: MutableLiveData<ResponseBody>? = null
    var adminTransactionsInvoiceLiveData: MutableLiveData<ResponseBody>? = null


    fun getUserWalletDetails(
        context: Context,
        requestBody: RequestBody,
        id: String
    ): LiveData<ResponseBody>? {
        walletDetailLiveData =
            WalletRepository.getUserWalletDetailsApiCall(context, requestBody, id)
        return walletDetailLiveData
    }

    fun addUserWalletDetails(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        walletAddDetailLiveData = WalletRepository.addUserWalletDetailsApiCall(context, requestBody, "")
        return walletAddDetailLiveData
    }

    fun withdrawUserWalletAmount(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        withdrawWalletAmountLiveData = WalletRepository.withdrawWalletDetailsApiCall(context, requestBody, "")
        return withdrawWalletAmountLiveData
    }

    fun transactionFilterForInvoice(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        transactionInvoiceLiveData = WalletRepository.transactionFilterForInvoiceApiCall(context, requestBody, "")
        return transactionInvoiceLiveData
    }

    fun adminTransactionFilterForInvoice(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        adminTransactionInvoiceLiveData = WalletRepository.adminTransactionFilterForInvoiceApiCall(context, requestBody, "")
        return adminTransactionInvoiceLiveData
    }

    fun adminTransactionsForInvoice(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        adminTransactionsInvoiceLiveData = WalletRepository.adminTransactionsForInvoiceApiCall(context, requestBody, "")
        return adminTransactionsInvoiceLiveData
    }

}