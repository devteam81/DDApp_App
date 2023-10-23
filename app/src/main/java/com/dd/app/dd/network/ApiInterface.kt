package com.dd.app.dd.network

import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.model.ResponseBodyArray
import com.dd.app.dd.model.ResponseLogin
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    //Common
    @PUT("user")
    fun updateUserName(@Body userData: RequestBody) : Call<ResponseBody>

    @PUT("user/update/password")
    fun updateUserPassword(@Body userData: RequestBody) : Call<ResponseBody>

    //Member
    @POST("auth/login")
    fun loginApi(@Body userData: RequestBody) : Call<ResponseLogin>

    @POST("getUserDetails")
    fun getUserDetails(@Body userData: RequestBody) : Call<Object>

    @GET("user-purchase")
    fun getUserVideoPurchase(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("user-coin-transaction-listing")
    fun getUserCoinDetailsData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("share_videos")
    fun getUserVideoShare(@Query("ott_id") id:String) : Call<ResponseBody>



    @GET("user-total-coins")
    fun getUserProfileData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("userShareVideolineGraph")
    fun getUserGraphData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("getDownlineDetails")
    fun getUserDownlinkData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("downlineUserDayGraph")
    fun getUserDownlinkReportDayGraphData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("downlineUserWeekGraph")
    fun getUserDownlinkReportWeekGraphData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("downlineUserMonthGraph")
    fun getUserDownlinkMonthGraphData(@Query("ott_id") id:String) : Call<ResponseBody>


    //Wallte Details

    @GET("get_bank_details")
    fun getUserWalletDetails(@Query("ott_id") id:String) : Call<ResponseBody>

    @POST("add_bank_details")
    fun addUserWalletDetails(@Body userData: RequestBody) : Call<ResponseBody>

    @POST("withdraw")
    fun withdrawWalletDetails(@Body userData: RequestBody) : Call<ResponseBody>

    @POST("transaction_filter")
    fun transactionFilterForInvoiceApiCall(@Body userData: RequestBody) : Call<ResponseBody>


    // Admin

    @GET("purchase")
    fun getAdminVideoPurchase() : Call<ResponseBody>

    @GET("shareVideolineGraph")
    fun getAdminGraphData() : Call<ResponseBody>

    @GET("adminUserProfileData")
    fun getAdminProfileData() : Call<ResponseBody>

    @GET("user-admin-videos")
    fun getAdminCoinDetailsData() : Call<ResponseBody>

    @GET("admin_share_videos")
    fun getAdminVideoShare() : Call<ResponseBody>

    @GET("getDownlineDetails")
    fun getAdminDownlinkData(@Query("ott_id") id:String) : Call<ResponseBody>


    @GET("downlineDayGraph")
    fun getAdminDownlinkReportDayGraphData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("downlineWeekGraph")
    fun getAdminDownlinkReportWeekGraphData(@Query("ott_id") id:String) : Call<ResponseBody>

    @GET("downlineMonthGraph")
    fun getAdminDownlinkMonthGraphData(@Query("ott_id") id:String) : Call<ResponseBody>

    @POST("admin_transaction_filter")
    fun adminTransactionFilterForInvoiceApiCall(@Body userData: RequestBody) : Call<ResponseBody>

    @GET("bank_withdraw")
    fun adminTransactionsForInvoiceApiCall() : Call<ResponseBody>


}