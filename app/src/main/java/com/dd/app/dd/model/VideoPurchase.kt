package com.dd.app.dd.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoPurchase(val _id: String) : Serializable {

    @SerializedName("product_id")
    var productId:String = ""
    @SerializedName("purcahase_id")
    var purchaseId:String = ""
    var thumbnail:String = ""
    @SerializedName("product_type")
    var productType:String = ""
    @SerializedName("product_description")
    var productDescription:String = ""
    @SerializedName("user_id")
     var userId:String = ""
    @SerializedName("mobile_image")
    var mobile_image:String = ""
    @SerializedName("refrance_id")
     var referanceId:String = ""
    @SerializedName("ott_user")
     var ottUser:String = ""
    @SerializedName("user_name")
     var userName:String = ""
    @SerializedName("user_email")
     var userEmail:String = ""
    @SerializedName("app_id")
     var appId:String = ""
    @SerializedName("share_link")
    var shareLink:String = ""
     var createdAt:String = ""

}