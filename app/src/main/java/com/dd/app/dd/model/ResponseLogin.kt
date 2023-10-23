package com.dd.app.dd.model

class ResponseLogin(

    val success: Boolean,
    val code: String? ,
    val message: String,
    val auth_token: String,
    val role: String,
    val user_id: String,
    val ott_id: String,
    val user_name: String,
    val user_email: String,
    val coins: Int
) {

    /*fun setAsJsonObject(dataObject:Object): JSONObject? {
        val jsonInString = Gson().toJson(dataObject)
        val mJSONObject = JSONObject(jsonInString)
        return mJSONObject
    }*/

    override fun toString(): String {
        return super.toString()
    }
}