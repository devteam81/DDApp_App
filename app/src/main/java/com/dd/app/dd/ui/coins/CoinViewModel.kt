package com.dd.app.dd.ui.coins

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.CoinRepository
import okhttp3.RequestBody

class CoinViewModel : ViewModel() {

    var coinLiveData: MutableLiveData<ResponseBody>? = null

    fun getUserCoinDetail(context: Context, requestBody: RequestBody, id: String): LiveData<ResponseBody>? {
        coinLiveData = CoinRepository.getUserCoinApiCall(context, requestBody, id)
        return coinLiveData
    }

    //Admin
    fun getAdminUserCoin(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        coinLiveData = CoinRepository.getAdminCoinApiCall(context, requestBody)
        return coinLiveData
    }

}