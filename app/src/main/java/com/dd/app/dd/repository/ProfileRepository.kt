package com.dd.app.dd.repository

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

object ProfileRepository {

    private val TAG  = ProfileRepository::class.simpleName.toString()

    val graphSetterGetter = MutableLiveData<ResponseBody>()
    val profileSetterGetter = MutableLiveData<ResponseBody>()
    val graphSetterGetterAdmin = MutableLiveData<ResponseBody>()
    val profileSetterGetterAdmin = MutableLiveData<ResponseBody>()

    private fun replaceFragmentWithAnimation(parentFragmentManager: FragmentManager, fragment: Fragment/*, tag: String?, toBackStack: Boolean*/) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        /*if (toBackStack) {
            transaction.addToBackStack(tag)
        }*/
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun getUserGraphDataApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.getUserGraphData(id)
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

                    graphSetterGetter.value = ResponseBody(success, msg, data)
                }
                else
                {

                    graphSetterGetter.value = ResponseBody(false, response.message(), null)
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

        return graphSetterGetter
    }

    fun getUserProfileDataApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getUserProfileData(id)
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
                                    Utils.showLongToast(context,context.getString(R.string.session_expired))
                                    Utils.log(TAG,context.getString(R.string.session_expired))
                                    val restartActivity = Intent(context, LoginActivity::class.java)
                                    restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(restartActivity)
                                    (context as Activity).finish()
                                }
                            }
                        }
                    }*/
                    profileSetterGetter.value = ResponseBody(success, msg, data)

                }
                else
                {

                    profileSetterGetter.value = ResponseBody(false, response.message(), null)
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
        return profileSetterGetter
    }

    //admin
    fun getAdminGraphDataApiCall(context: Context, requestBody: RequestBody): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminGraphData()
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
                                    Utils.showLongToast(context,context.getString(R.string.session_expired))
                                    Utils.log(TAG,context.getString(R.string.session_expired))
                                    val restartActivity = Intent(context, LoginActivity::class.java)
                                    restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(restartActivity)
                                    (context as Activity).finish()
                                }
                            }
                        }
                    }*/
                    graphSetterGetterAdmin.value = ResponseBody(success, msg, data)

                }
                else
                {
                    graphSetterGetterAdmin.value = ResponseBody(false, response.message(), null)
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
        return graphSetterGetterAdmin
    }

    fun getAdminProfileDataApiCall(context: Context, requestBody: RequestBody): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.getAdminProfileData()
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
                                    Utils.showLongToast(context,context.getString(R.string.session_expired))
                                    Utils.log(TAG,context.getString(R.string.session_expired))
                                    val restartActivity = Intent(context, LoginActivity::class.java)
                                    restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(restartActivity)
                                    (context as Activity).finish()
                                }
                            }
                        }
                    }*/
                    profileSetterGetterAdmin.value = ResponseBody(success, msg, data)

                }
                else
                {

                    profileSetterGetterAdmin.value = ResponseBody(false, response.message(), null)
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
        return profileSetterGetterAdmin
    }

}