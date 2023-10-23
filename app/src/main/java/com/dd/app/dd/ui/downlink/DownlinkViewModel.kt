package com.dd.app.dd.ui.downlink

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.DownlinkRepository
import okhttp3.RequestBody

class DownlinkViewModel : ViewModel() {

    var downlinkLiveData: MutableLiveData<ResponseBody>? = null
    var downlinkGraphDataLiveData: MutableLiveData<ResponseBody>? = null

    fun getUserDownlinkCoin(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkLiveData = DownlinkRepository.getUserDownlinkApiCall(context, requestBody, id)
        return downlinkLiveData
    }

    fun getUserDownlinkGraphDataCoin(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkGraphDataLiveData = DownlinkRepository.getUserDownlinkGraphDataApiCall(context, requestBody, id)
        return downlinkGraphDataLiveData
    }

    //Admin
    fun getAdminDownlinkCoin(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkLiveData = DownlinkRepository.getAdminDownlinkApiCall(context, requestBody, id)
        return downlinkLiveData
    }

    fun getAdminDownlinkGraphDataCoin(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkGraphDataLiveData = DownlinkRepository.getAdminDownlinkGraphDataApiCall(context, requestBody, id)
        return downlinkGraphDataLiveData
    }

}