package com.dd.app.dd.model

class ResponseBody(

    val success: Boolean,
    val data: Any?
) {


    var message: String? = ""
    var code: Int = 0

    constructor( success: Boolean,
                 message: String?,
                 data: Any?): this(success,data){
        this.message = message
    }




    /*fun setAsJsonObject(dataObject:Object): JSONObject? {
        val jsonInString = Gson().toJson(dataObject)
        val mJSONObject = JSONObject(jsonInString)
        return mJSONObject
    }*/

    override fun toString(): String {
        return super.toString()
    }
}