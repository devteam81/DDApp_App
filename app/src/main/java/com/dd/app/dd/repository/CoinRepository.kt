package com.dd.app.dd.repository

import android.content.Context
import android.util.Log
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

object CoinRepository {

    private val TAG  = CoinRepository::class.simpleName.toString()

    val coinSetterGetter = MutableLiveData<ResponseBody>()

    fun getUserCoinApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.getUserCoinDetailsData(id)
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

                Log.d("onResponse_test", "onResponse: "+response.body())

                if (response.body() != null) {

                    val success = response.body()!!.success
                    val data = response.body()!!.data
                    val msg = response.body()!!.message

                    if(!success){
                        if(msg != ""){
                            when(msg){
                                "unauthorised"->{
                                    Utils.logOutUserInDevice(context)
                                    UiUtils.showLongToast(context, context.getString(R.string.session_expired))
                                    UiUtils.log(TAG, context.getString(R.string.session_expired))
                                }
                            }
                        }
                    }
                    coinSetterGetter.value = ResponseBody(success, msg, data)
                }
            }
        })

        return coinSetterGetter
    }

    //admin
    fun getAdminCoinApiCall(context: Context, requestBody: RequestBody): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminCoinDetailsData()
        response.enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {

                UiUtils.log(TAG, "Response -> " + response.body().toString())
                Log.d("onResponse_test", "onResponse: 1"+response.body())
                if(response.body() != null) {
                    UiUtils.log(TAG, "Data -> " + response.body()!!.data)
                    val success = response.body()!!.success
                    val data = response.body()!!.data?: ""
                    val msg = response.body()!!.message?: ""

                    if(!success){
                        if(msg != ""){
                            when(msg){
                                "unauthorised"->{
                                    Utils.logOutUserInDevice(context)
                                    UiUtils.showLongToast(context, context.getString(R.string.session_expired))
                                    UiUtils.log(TAG, context.getString(R.string.session_expired))
                                }
                            }
                        }
                    }
                        coinSetterGetter.value = ResponseBody(success, msg, data)

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
        return coinSetterGetter
    }

}