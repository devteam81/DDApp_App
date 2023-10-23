package com.dd.app.dd.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.dd.app.R
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.network.RetrofitClient
import com.dd.app.util.UiUtils
import com.dd.app.util.sharedpref.Utils
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DownlinkRepository {

    private val TAG  = DownlinkRepository::class.simpleName.toString()

    val downlinkSetterGetter = MutableLiveData<ResponseBody>()
    val downlinkGraphSetterGetter = MutableLiveData<ResponseBody>()

    fun getUserDownlinkApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.getUserDownlinkData(id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // TODO("Not yet implemented")
                UiUtils.log("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                // TODO("Not yet implemented")
                UiUtils.log("DEBUG : ", response.body().toString())

                if (response.body() != null) {

                    val success = response.body()!!.success
                    val data = response.body()!!.data
                    val msg = response.body()!!.message

                    downlinkSetterGetter.value = ResponseBody(success, msg, data)
                }
                else
                {
                    val msg = response.message()?: ""
                    UiUtils.log(TAG, "Message-> ${msg.lowercase()}")
                    if(msg != ""){
                        when(msg.lowercase()){
                            "unauthorized"-> {
                                Utils.logOutUserInDevice(context)
                                UiUtils.showLongToast(context, context.getString(R.string.session_expired))
                                UiUtils.log(TAG, context.getString(R.string.session_expired))
                            }
                            else -> UiUtils.log(TAG, "Else")
                        }
                    }
                }
            }
        })

        return downlinkSetterGetter
    }

    fun getUserDownlinkGraphDataApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getUserDownlinkMonthGraphData(id)
        response.enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {

                UiUtils.log(TAG, "Response -> " + response.body().toString())

                if(response.body() != null) {
                    UiUtils.log(TAG, "Data -> " + response.body()!!.data)
                    val success = response.body()!!.success
                    val data = response.body()!!.data?: ""
                    val msg = response.body()!!.message?: ""

                    /*if(!success){
                        if(msg != ""){
                            when(msg){
                                "unauthorised"->{
                                    UserLoginDetails.setUserLoggedOut(context)
                                    LoginUser.apply {
                                        authToken=""
                                    }
                                    UiUtils.showLongToast(context,context.getString(R.string.session_expired))
                                    UiUtils.log(TAG,context.getString(R.string.session_expired))
                                    val restartActivity = Intent(context, LoginActivity::class.java)
                                    restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(restartActivity)
                                    (context as Activity).finish()
                                }
                            }
                        }
                    }*/
                    downlinkGraphSetterGetter.value = ResponseBody(success, msg, data)

                }
                else
                {
                    val msg = response.message()?: ""
                    UiUtils.log(TAG, "Message-> ${msg.lowercase()}")
                    if(msg != ""){
                        when(msg.lowercase()){
                            "unauthorized"-> {
                                Utils.logOutUserInDevice(context)
                                UiUtils.showLongToast(context, context.getString(R.string.session_expired))
                                UiUtils.log(TAG, context.getString(R.string.session_expired))
                            }
                            else -> UiUtils.log(TAG, "Else")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                UiUtils.log(TAG, "Error-> " + t.message)
            }
        })
        return downlinkGraphSetterGetter
    }

    //admin
    fun getAdminDownlinkApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminDownlinkData(id)
        response.enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {

                UiUtils.log(TAG, "Response -> " + response.body().toString())

                if(response.body() != null) {
                    UiUtils.log(TAG, "Data -> " + response.body()!!.data)
                    val success = response.body()!!.success
                    val data = response.body()!!.data?: ""
                    val msg = response.body()!!.message?: ""

                    /*if(!success){
                        if(msg != ""){
                            when(msg){
                                "unauthorised"->{
                                    UserLoginDetails.setUserLoggedOut(context)
                                    LoginUser.apply {
                                        authToken=""
                                    }
                                    UiUtils.showLongToast(context,context.getString(R.string.session_expired))
                                    UiUtils.log(TAG,context.getString(R.string.session_expired))
                                    val restartActivity = Intent(context, LoginActivity::class.java)
                                    restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(restartActivity)
                                    (context as Activity).finish()
                                }
                            }
                        }
                    }*/
                        downlinkSetterGetter.value = ResponseBody(success, msg, data)

                }
                else
                {
                    val msg = response.message()?: ""
                    UiUtils.log(TAG, "Message-> ${msg.lowercase()}")
                    if(msg != ""){
                        when(msg.lowercase()){
                            "unauthorized"-> {
                                Utils.logOutUserInDevice(context)
                                UiUtils.showLongToast(context, context.getString(R.string.session_expired))
                                UiUtils.log(TAG, context.getString(R.string.session_expired))
                            }
                            else -> UiUtils.log(TAG, "Else")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                UiUtils.log(TAG, "Error-> " + t.message)
            }
        })
        return downlinkSetterGetter
    }

    fun getAdminDownlinkGraphDataApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminDownlinkMonthGraphData(id)
        response.enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {

                UiUtils.log(TAG, "Response -> " + response.body().toString())

                if(response.body() != null) {
                    UiUtils.log(TAG, "Data -> " + response.body()!!.data)
                    val success = response.body()!!.success
                    val data = response.body()!!.data?: ""
                    val msg = response.body()!!.message?: ""

                    /*if(!success){
                        if(msg != ""){
                            when(msg){
                                "unauthorised"->{
                                    UserLoginDetails.setUserLoggedOut(context)
                                    LoginUser.apply {
                                        authToken=""
                                    }
                                    UiUtils.showLongToast(context,context.getString(R.string.session_expired))
                                    UiUtils.log(TAG,context.getString(R.string.session_expired))
                                    val restartActivity = Intent(context, LoginActivity::class.java)
                                    restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(restartActivity)
                                    (context as Activity).finish()
                                }
                            }
                        }
                    }*/
                    downlinkGraphSetterGetter.value = ResponseBody(success, msg, data)

                }
                else
                {
                    val msg = response.message()?: ""
                    UiUtils.log(TAG, "Message-> ${msg.lowercase()}")
                    if(msg != ""){
                        when(msg.lowercase()){
                            "unauthorized"-> {
                                Utils.logOutUserInDevice(context)
                                UiUtils.showLongToast(context, context.getString(R.string.session_expired))
                                UiUtils.log(TAG, context.getString(R.string.session_expired))
                            }
                            else -> UiUtils.log(TAG, "Else")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                UiUtils.log(TAG, "Error-> " + t.message)
            }
        })
        return downlinkGraphSetterGetter
    }

}