package com.dd.app.dd.model

import org.json.JSONObject

class ResponseBodyArray(

    val success: Boolean,
    val data: String) {

    var message: String? = ""

    /*fun setAsJsonObject(dataObject:Object): JSONObject? {
        val jsonInString = Gson().toJson(dataObject)
        val mJSONObject = JSONObject(jsonInString)
        return mJSONObject
    }*/
}