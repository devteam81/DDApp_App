package com.dd.app.dd.model

import com.google.gson.annotations.SerializedName

data class CoinHistory(val _id: String) {

    @SerializedName("debit")
    lateinit var totalCoins:String
    @SerializedName("product_id")
    lateinit var productId:String
    @SerializedName("cust_id")
    lateinit var customerId:String

    lateinit var ddId:String
     var purchaseId:String?= ""
    var thumbnail:String?= ""



}