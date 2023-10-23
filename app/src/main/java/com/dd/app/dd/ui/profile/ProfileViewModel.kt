package com.dd.app.dd.ui.profile

import android.R.attr.data
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.ProfileRepository
import okhttp3.RequestBody


class ProfileViewModel : ViewModel() {

    var graphLiveData: MutableLiveData<ResponseBody>? = null
    var profileLiveData: MutableLiveData<ResponseBody>? = null
    var graphAdminLiveData: MutableLiveData<ResponseBody>? = null
    var profileAdminLiveData: MutableLiveData<ResponseBody>? = null

    fun getUserGraphData(context: Context, requestBody: RequestBody, id: String): LiveData<ResponseBody>? {
        graphLiveData = ProfileRepository.getUserGraphDataApiCall(context, requestBody, id)
        return graphLiveData
    }

    fun getUserProfileData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        profileLiveData = ProfileRepository.getUserProfileDataApiCall(context, requestBody, id)
        return profileLiveData
    }

    //Admin
    fun getAdminGraphData(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        graphAdminLiveData = ProfileRepository.getAdminGraphDataApiCall(context, requestBody)
        return graphAdminLiveData
    }


    fun getAdminProfileData(context: Context, requestBody: RequestBody): LiveData<ResponseBody>? {
        profileAdminLiveData = ProfileRepository.getAdminProfileDataApiCall(context, requestBody)
        return profileAdminLiveData
    }


    fun clearProfileViewModel() {
        graphLiveData?.value = null
        profileLiveData?.value = null
    }

}