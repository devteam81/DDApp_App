package com.dd.app.dd.model

import com.google.gson.annotations.SerializedName

data class Downlink(var name: String = "") {

    @SerializedName("level1")
    var totalMembers:Int = 0
    @SerializedName("total_coins")
    var totalCoins: Int = 0




}