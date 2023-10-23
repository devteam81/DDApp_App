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

object VideosRepository {

    private val TAG  = VideosRepository::class.simpleName.toString()

    val videosSetterGetter = MutableLiveData<ResponseBody>()

    fun getUserVideoPurchaseApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody> {
        Log.d("getUserVideoPurchaseApiCall", "getUserVideoPurchaseApiCall: "+id)

        val call = RetrofitClient.apiInterface.getUserVideoPurchase(id)
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
                    val data = response.body()!!.data?:""

                    videosSetterGetter.value = ResponseBody(success, data)
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

        return videosSetterGetter
    }

    fun getUserVideoShareApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getUserVideoShare(id)
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
                    videosSetterGetter.value = ResponseBody(success, msg, data)

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
        return videosSetterGetter
    }

    //admin
    fun getAdminVideoPurchaseApiCall(context: Context, requestBody: RequestBody): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminVideoPurchase()
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
                        videosSetterGetter.value = ResponseBody(success, msg, data)

                }
                /*else if(response.errorBody() != null){
                    val jObjError = JSONObject(response.errorBody()!!.string())

                    if (!jObjError.getBoolean("success")) {
                        UiUtils.log(TAG, "Message-> $jObjError")
                        UiUtils.log(TAG, "" + jObjError.getString("message"))
                        Toast.makeText(context,jObjError.getString("message"), Toast.LENGTH_LONG).show()
                    }
                }*/
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
        return videosSetterGetter
    }

    fun getAdminVideoShareApiCall(context: Context, requestBody: RequestBody): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminVideoShare()
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
                    videosSetterGetter.value = ResponseBody(success, msg, data)

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
        return videosSetterGetter
    }

}