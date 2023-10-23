package com.dd.app.dd.ui.purchase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.app.R
import com.dd.app.databinding.ActivityVideoPurchaseBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils
import com.dd.app.dd.model.VideoPurchase
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.videoDetail.VideoDetailActivity
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class VideoPurchaseActivity : AppCompatActivity() {

    private val TAG  = VideoPurchaseActivity::class.simpleName.toString()

    private lateinit var binding: ActivityVideoPurchaseBinding
    private lateinit var videoPurchaseViewModel: VideoPurchaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_video_purchase)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_purchase)
        binding.imgBack.setOnClickListener { onBackPressed() }

        videoPurchaseViewModel = ViewModelProvider(this)[VideoPurchaseViewModel::class.java]
        // binding.videosViewModel = videosViewModel
        //binding.lifecycleOwner = this
        binding.rvVideoPurchase.layoutManager = LinearLayoutManager(this)
        observeViewModel(videoPurchaseViewModel)

    }

    private fun observeViewModel(viewModel: VideoPurchaseViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            // Update the list when the data changes
            viewModel.getAdminVideoPurchase(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var videoPurchaseList = listOf<VideoPurchase>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videoPurchaseList = ParseResponseData.parseVideoPurchase(mJSONArray)
                }
                binding.videoPurchaseAdapter = VideoPurchaseAdapter(videoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }
                /*binding.rvVideoPurchase.adapter = VideoPurchaseAdapter(videoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }*/

            })
        }
        else{
            //User

            val id = LoginUser.ottId

            viewModel.getUserVideoPurchase(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                Log.d("observeViewModel", "observeViewModel: "+it.data)
                val jsonInString = Gson().toJson(it.data)
                var videoPurchaseList = listOf<VideoPurchase>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videoPurchaseList = ParseResponseData.parseVideoPurchase(mJSONArray)

                }
                binding.videoPurchaseAdapter = VideoPurchaseAdapter(videoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(videoPurchaseList.isEmpty()) {
                    binding.rvVideoPurchase.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvVideoPurchase.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }
                /*binding.rvVideoPurchase.adapter = VideoPurchaseAdapter(videoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }*/

            })
        }
    }

    private fun listItemClicked(videoPurchase: VideoPurchase)
    {
        UiUtils.log(TAG, "Name: ${videoPurchase._id}")
        val intent = Intent(this, VideoDetailActivity::class.java)
        UiUtils.log(TAG,"Pass ID: "+ videoPurchase._id)
        intent.putExtra("video", videoPurchase)
        //send whole model
        startActivity(intent)
    }

}