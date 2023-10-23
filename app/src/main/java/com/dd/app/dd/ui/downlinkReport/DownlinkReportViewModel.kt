package com.dd.app.dd.ui.downlinkReport

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.repository.DownlinkReportRepository
import com.dd.app.dd.repository.DownlinkRepository
import okhttp3.RequestBody

class DownlinkReportViewModel : ViewModel() {

    private var downlinkReportLiveData: MutableLiveData<ResponseBody>? = null
    private var downlinkReportDayGraphDataLiveData: MutableLiveData<ResponseBody>? = null
    private var downlinkReportWeekGraphDataLiveData: MutableLiveData<ResponseBody>? = null
    private var downlinkReportMonthGraphDataLiveData: MutableLiveData<ResponseBody>? = null

    fun getUserDownlinkDetail(context: Context, requestBody: RequestBody, id: String): LiveData<ResponseBody>? {
        downlinkReportLiveData = DownlinkRepository.getUserDownlinkApiCall(context, requestBody, id)
        return downlinkReportLiveData
    }

    fun getUserDownlinkMonthReportGraphData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportMonthGraphDataLiveData = DownlinkReportRepository.getUserDownlinkReportMonthGraphDataApiCall(context, requestBody, id)
        return downlinkReportMonthGraphDataLiveData
    }

    fun getUserDownlinkWeekReportGraphData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportWeekGraphDataLiveData = DownlinkReportRepository.getUserDownlinkReportWeekGraphDataApiCall(context, requestBody, id)
        return downlinkReportWeekGraphDataLiveData
    }

    fun getUserDownlinkDayReportGraphData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportDayGraphDataLiveData = DownlinkReportRepository.getUserDownlinkReportDayGraphDataApiCall(context, requestBody, id)
        return downlinkReportDayGraphDataLiveData
    }

    //Admin
    fun getAdminDownlinkCoin(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportLiveData = DownlinkRepository.getAdminDownlinkApiCall(context, requestBody, id)
        return downlinkReportLiveData
    }

    fun getAdminDownlinkMonthReportGraphData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportMonthGraphDataLiveData = DownlinkReportRepository.getAdminDownlinkReportMonthGraphDataApiCall(context, requestBody, id)
        return downlinkReportMonthGraphDataLiveData
    }

    fun getAdminDownlinkWeekReportGraphData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportWeekGraphDataLiveData = DownlinkReportRepository.getAdminDownlinkReportWeekGraphDataApiCall(context, requestBody, id)
        return downlinkReportWeekGraphDataLiveData
    }

    fun getAdminDownlinkDayReportGraphData(context: Context, requestBody: RequestBody, id:String): LiveData<ResponseBody>? {
        downlinkReportDayGraphDataLiveData = DownlinkReportRepository.getAdminDownlinkReportDayGraphDataApiCall(context, requestBody, id)
        return downlinkReportDayGraphDataLiveData
    }

}