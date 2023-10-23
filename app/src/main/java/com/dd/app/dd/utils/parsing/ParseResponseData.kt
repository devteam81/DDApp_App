package com.dd.app.dd.utils.parsing

import android.util.Log
import com.dd.app.dd.model.*
import com.dd.app.dd.network.ApiConstants
import com.dd.app.util.UiUtils
import com.dd.digitaldistribution.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type


class ParseResponseData {

    companion object {

        private val TAG  = ParseResponseData::class.simpleName.toString()

        //Video Purchase
        fun parseVideoPurchase(jsonArray: JSONArray): List<VideoPurchase> {
            //val jsonArray = jsonObject.getJSONArray("data")

            //val membersList: ArrayList<Videos> = ArrayList()
            UiUtils.log("TAG", "Array Size: "+ jsonArray.length())

            val collectionType: Type = object : TypeToken<Collection<VideoPurchase?>?>() {}.type
            val videoPurchaseList: Collection<VideoPurchase> =
                Gson().fromJson(jsonArray.toString(), collectionType)
            UiUtils.log(TAG, "Video Purchase Size-> "+ videoPurchaseList.size)
            return videoPurchaseList as List<VideoPurchase>

            /*for (i in 0 until jsonArray.length()) {
                UiUtils.log(TAG, "Purchase -> "+ boxSearchCollection.elementAt(i).purchaseId)

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i);

                    //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                    var member = Members()
                    member.id = jsonObject.getString("_id");
                    member.firstName = jsonObject.getString("first_name")
                    member.middleName = jsonObject.getString("middle_name")
                    member.lastName = jsonObject.getString("last_name")
                    member.profilePhoto = jsonObject.getString("profile_photo")
                    member.gender = jsonObject.getString("gender")
                    member.dob = jsonObject.getString("dob")
                    member.age = jsonObject.getInt("age")
                    member.isActive = jsonObject.getBoolean("isActive")
                    member.qualification = parseQualificationArray(jsonObject.getJSONArray("qualifications"))
                    member.relationship = parseRelationshipArray(jsonObject.getJSONArray("relationship"))
                    member.workExperience = parseWorkExperienceArray(jsonObject.getJSONArray("work_experience"))
                    member.communityId = jsonObject.getString("community_id")

                    member.createdAt = jsonObject.getString("createdAt")
                    member.updatedAt = jsonObject.getString("updatedAt")

                    videosList.add(member)
                    // Notify the adapter
                }catch (e:Exception)
                {
                    UiUtils.log("parseMember",Log.getStackTraceString(e))
                }
            }

            return videosList*/
        }

        //Video Coins
        fun parseVideoCoin(jsonArray: JSONArray): List<CoinHistory> {
            //val jsonArray = jsonObject.getJSONArray("data")

            //val membersList: ArrayList<Videos> = ArrayList()
            UiUtils.log("TAG", "Array Size: "+ jsonArray.length())

            val collectionType: Type = object : TypeToken<Collection<CoinHistory?>?>() {}.type
            val videoCoinList: Collection<CoinHistory> =
                Gson().fromJson(jsonArray.toString(), collectionType)

            for ((count, item) in videoCoinList.withIndex()) {

                UiUtils.log(TAG, "count: $count")


                val jsonObject: JSONObject = jsonArray.getJSONObject(count)

                val jsonArray = jsonObject.getJSONArray("result")

                if (jsonArray.length() > 0) {
                    val dataObject: JSONObject = jsonObject.getJSONArray("result").getJSONObject(0)
                    Log.d("parseVideoCoin", "dataObject: "+dataObject)

                    item.apply {
                        ddId = dataObject.getString("_id")
                        purchaseId = dataObject.getString("purcahase_id")
                        thumbnail = if(dataObject.has("thumbnail")){ dataObject.getString("thumbnail") } else ""
                    }
                    // Continue parsing and working with the JSON object
                } else {
                    // Handle the case where the array is empty
                }



            }

            UiUtils.log(TAG, "Video Size-> "+ videoCoinList.size)
            return videoCoinList as List<CoinHistory>

            /*for (i in 0 until jsonArray.length()) {
                UiUtils.log(TAG, "Purchase -> "+ boxSearchCollection.elementAt(i).purchaseId)

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i);

                    //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                    var member = Members()
                    member.id = jsonObject.getString("_id");
                    member.firstName = jsonObject.getString("first_name")
                    member.middleName = jsonObject.getString("middle_name")
                    member.lastName = jsonObject.getString("last_name")
                    member.profilePhoto = jsonObject.getString("profile_photo")
                    member.gender = jsonObject.getString("gender")
                    member.dob = jsonObject.getString("dob")
                    member.age = jsonObject.getInt("age")
                    member.isActive = jsonObject.getBoolean("isActive")
                    member.qualification = parseQualificationArray(jsonObject.getJSONArray("qualifications"))
                    member.relationship = parseRelationshipArray(jsonObject.getJSONArray("relationship"))
                    member.workExperience = parseWorkExperienceArray(jsonObject.getJSONArray("work_experience"))
                    member.communityId = jsonObject.getString("community_id")

                    member.createdAt = jsonObject.getString("createdAt")
                    member.updatedAt = jsonObject.getString("updatedAt")

                    videosList.add(member)
                    // Notify the adapter
                }catch (e:Exception)
                {
                    UiUtils.log("parseMember",Log.getStackTraceString(e))
                }
            }

            return videosList*/
        }

        //Graph for line
        fun parseGraphLineData(jsonArray: JSONArray, type: Int): List<Graph> {
            /*val collectionType: Type = object : TypeToken<Collection<Graph?>?>() {}.type
            val graphList: Collection<Graph> =
                Gson().fromJson(jsonArray.toString(), collectionType)*/
            val graphList: ArrayList<Graph> = ArrayList()
            val graphVideoShareList: ArrayList<GraphData> = ArrayList()
            val graphCustomerDownloadList: ArrayList<GraphData> = ArrayList()
            val graphTotalCoinsList: ArrayList<GraphData> = ArrayList()
            for (i in 0 until jsonArray.length()) {

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    //UiUtils.log(TAG, "Purchase -> "+ jsonObject.getString("day"))
                    //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                    //Video Share
                    try {
                        if(type == ApiConstants.PROFILE) {
                            val graphVideoShareData: GraphData = GraphData().apply {
                                id = i + 1
                                title = jsonObject.getString("day")
                                value = jsonObject.getDouble("shareVideo").toInt()
                            }
                            graphVideoShareList.add(graphVideoShareData)
                        }
                    }catch (e: Exception){
                        UiUtils.log(TAG, Log.getStackTraceString(e))
                    }


                    //Downlink
                    try {
                        val graphCustomerDownloadData: GraphData = GraphData().apply {
                            id = i + 1
                            title = jsonObject.getString("day")
                            value = jsonObject.getDouble("customerCount").toInt()
                        }
                        graphCustomerDownloadList.add(graphCustomerDownloadData)
                    }catch (e: Exception){
                        UiUtils.log(TAG, Log.getStackTraceString(e))
                    }

                    //Coins
                    try {
                        val graphTotalCoinsData: GraphData = GraphData().apply {
                            id = i + 1
                            title = jsonObject.getString("day")
                            value = jsonObject.getDouble("coinsTotal").toInt()
                        }
                        graphTotalCoinsList.add(graphTotalCoinsData)
                    }catch (e: Exception){
                        UiUtils.log(TAG, Log.getStackTraceString(e))
                    }

                }catch (e:Exception)
                {
                    UiUtils.log(TAG, Log.getStackTraceString(e))
                }
            }

            if(type == ApiConstants.PROFILE) {
                //Video Share
                val graphVideoShare: Graph = Graph().apply {
                    title = "Video Share"
                    graphDataList = graphVideoShareList
                }

                graphList.add(graphVideoShare)
            }
            //Customer
            val graphCustomerDownload: Graph = Graph().apply {
                title = "Members"
                graphDataList = graphCustomerDownloadList
            }
            //Coins
            val graphTotalCoins: Graph = Graph().apply {
                title = "No. of Coins"
                graphDataList = graphTotalCoinsList
            }

            graphList.add(graphCustomerDownload)
            graphList.add(graphTotalCoins)


            return graphList
        }

        //Graph for line
        fun parseGraphLineWeekData(jsonArray: JSONArray, type: Int): List<WeekGraph> {
            /*val collectionType: Type = object : TypeToken<Collection<Graph?>?>() {}.type
            val graphList: Collection<Graph> =
                Gson().fromJson(jsonArray.toString(), collectionType)*/
            val weekGraphList: ArrayList<WeekGraph> = ArrayList()


            for (i in 0 until jsonArray.length()) {

                try {

                    val jsonObjectWeek: JSONObject = jsonArray.getJSONObject(i)

                    val weekJsonArray: JSONArray = jsonObjectWeek.getJSONArray("weekData")
                    val graphList: ArrayList<Graph> = ArrayList()
                    val graphVideoShareList: ArrayList<GraphData> = ArrayList()
                    val graphCustomerDownloadList: ArrayList<GraphData> = ArrayList()
                    val graphTotalCoinsList: ArrayList<GraphData> = ArrayList()
                    for (j in 0 until weekJsonArray.length()) {

                        try {
                            val jsonObject: JSONObject = weekJsonArray.getJSONObject(j)
                            //UiUtils.log(TAG, "Purchase -> "+ jsonObject.getString("day"))
                            //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                            //Video Share
                            try {
                                if (type == ApiConstants.PROFILE) {
                                    val graphVideoShareData: GraphData = GraphData().apply {
                                        id = j + 1
                                        title = jsonObject.getString("day")
                                        value = jsonObject.getDouble("shareVideo").toInt()
                                    }
                                    graphVideoShareList.add(graphVideoShareData)
                                }
                            } catch (e: Exception) {
                                UiUtils.log(TAG, Log.getStackTraceString(e))
                            }


                            //Downlink
                            try {
                                val graphCustomerDownloadData: GraphData = GraphData().apply {
                                    id = j + 1
                                    title = jsonObject.getString("day")
                                    value = jsonObject.getDouble("customerCount").toInt()
                                }
                                graphCustomerDownloadList.add(graphCustomerDownloadData)
                            } catch (e: Exception) {
                                UiUtils.log(TAG, Log.getStackTraceString(e))
                            }

                            //Coins
                            try {
                                val graphTotalCoinsData: GraphData = GraphData().apply {
                                    id = j + 1
                                    title = jsonObject.getString("day")
                                    value = jsonObject.getDouble("coinsTotal").toInt()
                                }
                                graphTotalCoinsList.add(graphTotalCoinsData)
                            } catch (e: Exception) {
                                UiUtils.log(TAG, Log.getStackTraceString(e))
                            }

                        } catch (e: Exception) {
                            UiUtils.log(TAG, Log.getStackTraceString(e))
                        }
                    }

                    if (type == ApiConstants.PROFILE) {
                        //Video Share
                        val graphVideoShare: Graph = Graph().apply {
                            title = "Video Share"
                            graphDataList = graphVideoShareList
                        }

                        graphList.add(graphVideoShare)
                    }
                    //Customer
                    val graphCustomerDownload: Graph = Graph().apply {
                        title = "Members"
                        graphDataList = graphCustomerDownloadList
                    }
                    //Coins
                    val graphTotalCoins: Graph = Graph().apply {
                        title = "No. of Coins"
                        graphDataList = graphTotalCoinsList
                    }

                    graphList.add(graphCustomerDownload)
                    graphList.add(graphTotalCoins)


                    weekGraphList.add(
                        WeekGraph().apply {
                            week = jsonObjectWeek.getString("week")
                            weekGraphDataList = graphList
                        }
                    )
                }catch (e: Exception){
                    UiUtils.log(TAG, Log.getStackTraceString(e))
                }
            }




            return weekGraphList
        }

        //Graph for bar
        fun parseGraphBarData(jsonArray: JSONArray): List<Graph> {
            /*val collectionType: Type = object : TypeToken<Collection<Graph?>?>() {}.type
            val graphList: Collection<Graph> =
                Gson().fromJson(jsonArray.toString(), collectionType)*/
            val graphList: ArrayList<Graph> = ArrayList()
            var barGraphDataList: ArrayList<GraphData> = ArrayList()

            for (i in 0 until jsonArray.length()) {

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    val graphData: GraphData = GraphData().apply {
                        id = i + 1
                        title = jsonObject.getString("day")
                        value = jsonObject.getDouble("customerCount").toInt()
                        valueSecond = jsonObject.getDouble("coinsTotal").toInt()
                    }

                    barGraphDataList.add(graphData)
                }catch (e: Exception){
                    UiUtils.log(TAG, Log.getStackTraceString(e))
                }
            }

            val graph: Graph = Graph().apply {
                title = "Members"
                titleNew = "Coins"
                graphDataList = barGraphDataList
            }

            graphList.add(graph)

            return graphList
        }

        //Video Share
        fun parseVideoShare(jsonArray: JSONArray): List<VideoShare> {
            //val jsonArray = jsonObject.getJSONArray("data")

            //val membersList: ArrayList<Videos> = ArrayList()
            UiUtils.log("TAG", "Array Size: "+ jsonArray.length())

            val collectionType: Type = object : TypeToken<Collection<VideoShare?>?>() {}.type
            val videosList: Collection<VideoShare> =
                Gson().fromJson(jsonArray.toString(), collectionType)
            UiUtils.log(TAG, "Video Size-> "+ videosList.size)
            return videosList as List<VideoShare>

            /*for (i in 0 until jsonArray.length()) {
                UiUtils.log(TAG, "Purchase -> "+ boxSearchCollection.elementAt(i).purchaseId)

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i);

                    //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                    var member = Members()
                    member.id = jsonObject.getString("_id");
                    member.firstName = jsonObject.getString("first_name")
                    member.middleName = jsonObject.getString("middle_name")
                    member.lastName = jsonObject.getString("last_name")
                    member.profilePhoto = jsonObject.getString("profile_photo")
                    member.gender = jsonObject.getString("gender")
                    member.dob = jsonObject.getString("dob")
                    member.age = jsonObject.getInt("age")
                    member.isActive = jsonObject.getBoolean("isActive")
                    member.qualification = parseQualificationArray(jsonObject.getJSONArray("qualifications"))
                    member.relationship = parseRelationshipArray(jsonObject.getJSONArray("relationship"))
                    member.workExperience = parseWorkExperienceArray(jsonObject.getJSONArray("work_experience"))
                    member.communityId = jsonObject.getString("community_id")

                    member.createdAt = jsonObject.getString("createdAt")
                    member.updatedAt = jsonObject.getString("updatedAt")

                    videosList.add(member)
                    // Notify the adapter
                }catch (e:Exception)
                {
                    UiUtils.log("parseMember",Log.getStackTraceString(e))
                }
            }

            return videosList*/
        }

        fun parseInvoice(jsonArray: JSONArray): List<Invoice> {
            //val jsonArray = jsonObject.getJSONArray("data")

            //val membersList: ArrayList<Videos> = ArrayList()
            UiUtils.log("TAG", "Array Size: "+ jsonArray.length())

            val collectionType: Type = object : TypeToken<Collection<Invoice?>?>() {}.type
            val invoiceList: Collection<Invoice> =
                Gson().fromJson(jsonArray.toString(), collectionType)
            UiUtils.log(TAG, "Invoice Size-> "+ invoiceList.size)
            return invoiceList as List<Invoice>

            /*for (i in 0 until jsonArray.length()) {
                UiUtils.log(TAG, "Purchase -> "+ boxSearchCollection.elementAt(i).purchaseId)

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i);

                    //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                    var member = Members()
                    member.id = jsonObject.getString("_id");
                    member.firstName = jsonObject.getString("first_name")
                    member.middleName = jsonObject.getString("middle_name")
                    member.lastName = jsonObject.getString("last_name")
                    member.profilePhoto = jsonObject.getString("profile_photo")
                    member.gender = jsonObject.getString("gender")
                    member.dob = jsonObject.getString("dob")
                    member.age = jsonObject.getInt("age")
                    member.isActive = jsonObject.getBoolean("isActive")
                    member.qualification = parseQualificationArray(jsonObject.getJSONArray("qualifications"))
                    member.relationship = parseRelationshipArray(jsonObject.getJSONArray("relationship"))
                    member.workExperience = parseWorkExperienceArray(jsonObject.getJSONArray("work_experience"))
                    member.communityId = jsonObject.getString("community_id")

                    member.createdAt = jsonObject.getString("createdAt")
                    member.updatedAt = jsonObject.getString("updatedAt")

                    videosList.add(member)
                    // Notify the adapter
                }catch (e:Exception)
                {
                    UiUtils.log("parseMember",Log.getStackTraceString(e))
                }
            }

            return videosList*/
        }

        fun parseAdminInvoice(jsonArray: JSONArray): List<Invoice> {
            //val jsonArray = jsonObject.getJSONArray("data")

            //val membersList: ArrayList<Videos> = ArrayList()
            UiUtils.log("TAG", "Array Size: "+ jsonArray.length())

            /*val collectionType: Type = object : TypeToken<Collection<Invoice?>?>() {}.type
            var invoiceList: Collection<Invoice> =
                Gson().fromJson(jsonArray.toString(), collectionType)
            UiUtils.log(TAG, "Invoice Size-> "+ invoiceList.size)
            return invoiceList as List<Invoice>*/

            var invoiceList = mutableListOf<Invoice>()

                    for (i in 0 until jsonArray.length()) {
                //UiUtils.log(TAG, "Purchase -> "+ boxSearchCollection.elementAt(i).purchaseId)

                try {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i);

                    //val user: UserDetails = gson.fromJson(json, UserDetails::class.java)

                    var invoice = Invoice(jsonObject.getString("_id")).apply {

                        debit = jsonObject.getString("withdrawal_amount")
                        custId = jsonObject.getJSONArray("result").getJSONObject(0).getString("benificiery_name")
                        createdAt = jsonObject.getString("createdAt")
                    }

                    invoiceList.add(invoice)
                    // Notify the adapter
                }catch (e:Exception)
                {
                    UiUtils.log("parseMember",Log.getStackTraceString(e))
                }
            }

            return invoiceList
        }
    }

}