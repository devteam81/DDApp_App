package com.dd.app.dd.ui.downlink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dd.app.R
import com.dd.app.databinding.ActivityDownlinkBinding
import com.dd.app.dd.model.Downlink
import com.dd.app.dd.model.Graph
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.utils.graph.GraphUtil
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils

import com.dd.app.dd.ui.downlinkReport.DownlinkReportActivity
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class DownlinkActivity : AppCompatActivity() {

    private val TAG  = DownlinkActivity::class.simpleName.toString()
    private lateinit var binding: ActivityDownlinkBinding

    private lateinit var downlinkViewModel: DownlinkViewModel
    var downlinkList = mutableListOf<Downlink>()
    private var selectedLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_downlink)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downlink)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.txtViewHistory.setOnClickListener {
            val viewReportIntent = Intent(this, DownlinkReportActivity::class.java)
            startActivity(viewReportIntent)
        }

        downlinkViewModel = ViewModelProvider(this)[DownlinkViewModel::class.java]

        binding.llLevel1.setOnClickListener {
            selectedLevel= 1
            changeLevelUI()
            filterDataByLevel()
        }
        binding.llLevel2.setOnClickListener {
            selectedLevel= 2
            changeLevelUI()
            filterDataByLevel()
        }
        binding.llLevel3.setOnClickListener {
            selectedLevel= 3
            changeLevelUI()
            filterDataByLevel()
        }
        binding.llLevel4.setOnClickListener {
            selectedLevel= 4
            changeLevelUI()
            filterDataByLevel()
        }
        binding.llLevel5.setOnClickListener {
            selectedLevel= 5
            changeLevelUI()
            filterDataByLevel()
        }

        observeViewModel(downlinkViewModel);

    }

    private fun observeViewModel(downlinkViewModel: DownlinkViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())
        val id = LoginUser.ottId

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            // Update the graph when the data changes
            downlinkViewModel.getAdminDownlinkCoin(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                downlinkList.clear()
                if(jsonInString.length>2) {
                    val mJSONObject = JSONObject(jsonInString)
                    UiUtils.log(TAG,"Value-> " + mJSONObject.getJSONObject("0").getString("level1"))
                    for (i in 0..4){
                        val levelCount:String = (i+1).toString()
                        downlinkList.add(Downlink("Level $levelCount").apply {
                            totalMembers = (mJSONObject.getJSONObject(i.toString()).getString("level$levelCount")).toDouble().toInt()
                            totalCoins = (mJSONObject.getJSONObject(i.toString()).getString("total_coins")).toDouble().toInt()
                        })
                    }
                }
                if(downlinkList.isEmpty()){

                }else
                    filterDataByLevel()

            })

            //graph Data
            downlinkViewModel.getAdminDownlinkGraphDataCoin(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    graphList = ParseResponseData.parseGraphBarData(mJSONArray)


                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                    GraphUtil(this).setDataForBarChart(binding.barChart, graphList as ArrayList<Graph>, 1)
                }
            })

        }
        else{
            //User
            val id = LoginUser.ottId

            // Update the graph when the data changes
            downlinkViewModel.getUserDownlinkCoin(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                downlinkList.clear()
                if(jsonInString.length>2) {
                    val mJSONObject = JSONObject(jsonInString)
                    UiUtils.log(TAG,"Value-> " + mJSONObject.getJSONObject("0").getString("level1"))
                    for (i in 0..4){
                        val levelCount:String = (i+1).toString()
                        downlinkList.add(Downlink("Level $levelCount").apply {
                            totalMembers = (mJSONObject.getJSONObject(i.toString()).getString("level$levelCount")).toDouble().toInt()
                            totalCoins = (mJSONObject.getJSONObject(i.toString()).getString("total_coins")).toDouble().toInt()
                        })
                    }
                }
                if(downlinkList.isEmpty()){

                }else
                    filterDataByLevel()

            })

            //graph Data
            downlinkViewModel.getUserDownlinkGraphDataCoin(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    graphList = ParseResponseData.parseGraphBarData(mJSONArray)


                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                    GraphUtil(this).setDataForBarChart(binding.barChart, graphList as ArrayList<Graph>, 1)
                }
            })
        }
    }

    private fun filterDataByLevel(){

        when(selectedLevel){
            1-> setData(downlinkList[0].totalCoins, downlinkList[0].totalMembers)
            2-> setData(downlinkList[1].totalCoins, downlinkList[1].totalMembers)
            3-> setData(downlinkList[2].totalCoins, downlinkList[2].totalMembers)
            4-> setData(downlinkList[3].totalCoins, downlinkList[3].totalMembers)
            5-> setData(downlinkList[4].totalCoins, downlinkList[4].totalMembers)
        }

    }

    private fun setData(totalCoins: Int, totalMembers: Int){
        binding.txtTotalCoins.text = totalCoins.toString()
        binding.txtTotalMembers.text = totalMembers.toString()
    }

    private fun changeLevelUI(){

        levelUINotSelected(binding.llLevel1, binding.txtLevel1Tag, binding.txtLevel1Text)
        levelUINotSelected(binding.llLevel2, binding.txtLevel2Tag, binding.txtLevel2Text)
        levelUINotSelected(binding.llLevel3, binding.txtLevel3Tag, binding.txtLevel3Text)
        levelUINotSelected(binding.llLevel4, binding.txtLevel4Tag, binding.txtLevel4Text)
        levelUINotSelected(binding.llLevel5, binding.txtLevel5Tag, binding.txtLevel5Text)

        when(selectedLevel){
            1-> levelUISelected(binding.llLevel1, binding.txtLevel1Tag, binding.txtLevel1Text)
            2-> levelUISelected(binding.llLevel2, binding.txtLevel2Tag, binding.txtLevel2Text)
            3-> levelUISelected(binding.llLevel3, binding.txtLevel3Tag, binding.txtLevel3Text)
            4-> levelUISelected(binding.llLevel4, binding.txtLevel4Tag, binding.txtLevel4Text)
            5-> levelUISelected(binding.llLevel5, binding.txtLevel5Tag, binding.txtLevel5Text)
        }

    }

    private fun levelUISelected(llLevel: LinearLayoutCompat, txtLevelTag: AppCompatTextView, txtLevelText: AppCompatTextView) {
        llLevel.background = ResourcesCompat.getDrawable(resources,R.drawable.button_gradient_color_top_bottom,null)
        txtLevelTag.setTextColor(AppCompatResources.getColorStateList(this, R.color.white))
        txtLevelText.setTextColor(AppCompatResources.getColorStateList(this, R.color.white))
    }

    private fun levelUINotSelected(llLevel: LinearLayoutCompat, txtLevelTag: AppCompatTextView, txtLevelText: AppCompatTextView){
        llLevel.background = ResourcesCompat.getDrawable(resources,R.drawable.gray_bg_small,null)
        txtLevelTag.setTextColor(AppCompatResources.getColorStateList(this, R.color.grey))
        txtLevelText.setTextColor(AppCompatResources.getColorStateList(this, R.color.grey))
    }
}