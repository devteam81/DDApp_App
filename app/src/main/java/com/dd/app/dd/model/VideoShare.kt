package com.dd.digitaldistribution.data.model

import com.google.gson.annotations.SerializedName

data class VideoShare(val _id: String) {

    @SerializedName("count")
    var videoShare:String = "0"
    @SerializedName("video_id")
    var videoId:String = ""
    @SerializedName("video_title")
    var videoTitle:String = ""
    @SerializedName("browse_image")
    var browseImage:String = ""
    @SerializedName("refrance_id")
    var referenceId:String = ""
    @SerializedName("result")
    var videoPurchase:String = "0"


}