package com.dd.app.dd.ui.share

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.VideosRepository
import okhttp3.RequestBody

class VideoShareViewModel : ViewModel() {

    var videoShareLiveData: MutableLiveData<ResponseBody>? = null

    fun getUserVideoShare(context: Context, requestBody: RequestBody, id: String): LiveData<ResponseBody>? {
        videoShareLiveData = VideosRepository.getUserVideoShareApiCall(context, requestBody, id)
        return videoShareLiveData
    }

    //Admin
    fun getAdminVideoShare(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        videoShareLiveData = VideosRepository.getAdminVideoShareApiCall(context, requestBody)
        return videoShareLiveData
    }

}