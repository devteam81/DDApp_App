package com.dd.app.dd.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Invoice(val _id: String) : Serializable {

    var credit:String = ""
    var debit:String = ""
    @SerializedName("trans_amt")
    var transAmt:String = ""
    @SerializedName("app_id")
    var appId:String = ""
    @SerializedName("product_id")
    var productId:String = ""
    @SerializedName("cust_id")
    var custId:String = ""
    @SerializedName("from_refferal")
    var fromRefferal:String = ""

    var createdAt:String = ""

    var result:List<VideoPurchase> = mutableListOf()



}