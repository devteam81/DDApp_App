package com.dd.app.dd.ui.share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.app.R
import com.dd.app.databinding.ActivityVideoShareBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.profile.ProfileViewModel
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils
import com.dd.digitaldistribution.data.model.VideoShare
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class VideoShareActivity : AppCompatActivity() {

    private val TAG  = VideoShareActivity::class.simpleName.toString()
    private lateinit var binding: ActivityVideoShareBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var videoShareViewModel: VideoShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_video_share)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_share)

        binding.toolbar.setNavigationOnClickListener { finish() }

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        videoShareViewModel = ViewModelProvider(this)[VideoShareViewModel::class.java]
        binding.lifecycleOwner = this
        binding.rvVideoShare.layoutManager = LinearLayoutManager(this)
        observeViewModel(profileViewModel, videoShareViewModel)

    }

    private fun observeViewModel(profileViewModel: ProfileViewModel, videoShareViewModel: VideoShareViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            // Update the list when the data changes
            videoShareViewModel.getAdminVideoShare(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var videoShareList = listOf<VideoShare>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videoShareList = ParseResponseData.parseVideoShare(mJSONArray)
                }
                /*binding.videoPurchaseAdapter = VideoPurchaseAdapter(videoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }*/
                binding.rvVideoShare.adapter = VideoShareAdapter(this, videoShareList) { selectedItem: VideoShare ->
                    listItemClicked(
                        selectedItem
                    )
                }

            })

            //update details when data changes
            profileViewModel.getAdminProfileData(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                //var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    val mJSONObject = JSONObject(jsonInString)
                    //val spentCoinsObject = mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject
                    //val totalCoinsObject = mJSONObject.getJSONArray("total_coins").get(0) as JSONObject
                    val totalVideoShareObject = mJSONObject.getJSONArray("total_share_video").get(0) as JSONObject
                    //UiUtils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                    binding.txtTotalShare.text = totalVideoShareObject.getString("total_count").toDouble().toInt().toString()

                }

            })

        }
        else{
            //User

            val id = LoginUser.ottId

            // Update the list when the data changes
            videoShareViewModel.getUserVideoShare(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var videoShareList = listOf<VideoShare>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videoShareList = ParseResponseData.parseVideoShare(mJSONArray)
                }
                /*binding.videoPurchaseAdapter = VideoPurchaseAdapter(videoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }*/
                binding.rvVideoShare.adapter = VideoShareAdapter(this, videoShareList) { selectedItem: VideoShare ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(videoShareList.isEmpty()) {
                    binding.rvVideoShare.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvVideoShare.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })

            //update details when data changes
            profileViewModel.getUserProfileData(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                //var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    try {
                        val mJSONObject = JSONObject(jsonInString)
                        //val spentCoinsObject = mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject
                        //val totalCoinsObject = mJSONObject.getJSONArray("total_coins").get(0) as JSONObject
                        val totalVideoShare = mJSONObject.getString("share_video")
                        //UiUtils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                        binding.txtTotalShare.text = totalVideoShare.toDouble().toInt().toString()
                    } catch (e: JSONException){

                    }


                }

            })
        }
    }

    private fun listItemClicked(videoShare: VideoShare)
    {
        UiUtils.log(TAG, "Name: ${videoShare._id}")
        /*val intent = Intent(activity, MemberProfileActivity::class.java)
        UiUtils.log(TAG,"Pass ID: "+ member.id)
        intent.putExtra("member", member)
        //send whole model
        startActivity(intent)*/
    }
}