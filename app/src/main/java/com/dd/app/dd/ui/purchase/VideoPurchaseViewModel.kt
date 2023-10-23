package com.dd.app.dd.ui.purchase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.VideosRepository

import okhttp3.RequestBody

class VideoPurchaseViewModel : ViewModel() {

    var videoPurchaseLiveData: MutableLiveData<ResponseBody>? = null

    fun getUserVideoPurchase(context: Context, requestBody: RequestBody, id: String): LiveData<ResponseBody>? {
        videoPurchaseLiveData = VideosRepository.getUserVideoPurchaseApiCall(context, requestBody, id)
        return videoPurchaseLiveData
    }

    //Admin
    fun getAdminVideoPurchase(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        videoPurchaseLiveData = VideosRepository.getAdminVideoPurchaseApiCall(context, requestBody)
        return videoPurchaseLiveData
    }

}