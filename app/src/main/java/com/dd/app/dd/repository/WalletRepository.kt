package com.dd.app.dd.repository

import android.content.Context
import android.widget.Toast
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

object WalletRepository {

    private val TAG  = WalletRepository::class.simpleName.toString()

    val getWalletSetterGetter = MutableLiveData<ResponseBody>()
    val addWalletSetterGetter = MutableLiveData<ResponseBody>()
    val withdrawWalletSetterGetter = MutableLiveData<ResponseBody>()
    val transactionInvoiceSetterGetter = MutableLiveData<ResponseBody>()
    val adminTransactionInvoiceSetterGetter = MutableLiveData<ResponseBody>()
    val adminTransactionsSetterGetter = MutableLiveData<ResponseBody>()


    fun getUserWalletDetailsApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.getUserWalletDetails(id)
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

                    getWalletSetterGetter.value = ResponseBody(success, data)
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

        return getWalletSetterGetter
    }

    fun addUserWalletDetailsApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.addUserWalletDetails(requestBody)
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
                    Toast.makeText(context, msg,Toast.LENGTH_LONG).show()
                    addWalletSetterGetter.value = ResponseBody(success, msg, data)

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
        return addWalletSetterGetter
    }

    fun withdrawWalletDetailsApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.withdrawWalletDetails(requestBody)
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
                    Toast.makeText(context, msg,Toast.LENGTH_LONG).show()
                    withdrawWalletSetterGetter.value = ResponseBody(success, msg, data)
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
        return withdrawWalletSetterGetter
    }

    fun transactionFilterForInvoiceApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.transactionFilterForInvoiceApiCall(requestBody)
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
                    transactionInvoiceSetterGetter.value = ResponseBody(success, msg, data)

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
        return transactionInvoiceSetterGetter
    }



    //admin
    fun adminTransactionFilterForInvoiceApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.adminTransactionFilterForInvoiceApiCall(requestBody)
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
                    adminTransactionInvoiceSetterGetter.value = ResponseBody(success, msg, data)

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
        return adminTransactionInvoiceSetterGetter
    }

    fun adminTransactionsForInvoiceApiCall(context: Context, requestBody: RequestBody, id:String): MutableLiveData<ResponseBody>{

        val response = RetrofitClient.apiInterface.adminTransactionsForInvoiceApiCall()
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
                    adminTransactionsSetterGetter.value = ResponseBody(success, msg, data)

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
        return adminTransactionsSetterGetter
    }


}