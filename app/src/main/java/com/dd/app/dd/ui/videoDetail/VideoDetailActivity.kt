package com.dd.app.dd.ui.videoDetail

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dd.app.R
import com.dd.app.databinding.ActivityVideoDetailBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.model.VideoPurchase
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.purchase.VideoPurchaseViewModel
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


class VideoDetailActivity : AppCompatActivity() {

    companion object{
        private val TAG  = VideoDetailActivity::class.simpleName.toString()
    }

    private lateinit var binding: ActivityVideoDetailBinding
    private lateinit var videoPurchaseViewModel: VideoPurchaseViewModel
    private lateinit var videoPurchase : VideoPurchase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_video_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_detail)

        binding.toolbar.setNavigationOnClickListener { finish() }

        videoPurchase = intent.getSerializableExtra("video") as VideoPurchase
        displayDetails(videoPurchase)

        videoPurchaseViewModel = ViewModelProvider(this)[VideoPurchaseViewModel::class.java]
        binding.lifecycleOwner = this
        binding.rvOtherPurchaseVideos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        observeViewModel(videoPurchaseViewModel)


        binding.txtShareUrl.setOnClickListener{
            val cm: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setText(binding.txtShareUrl.text)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        binding.btnShare.setOnClickListener {
            shareVideoButton()
        }

    }

    private fun observeViewModel(viewModel: VideoPurchaseViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())
        Log.d("observeViewModeltest", "observeViewModel: "+jsonObject)

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

                val tempVideoPurchaseList = videoPurchaseList.filter { video-> video._id  != videoPurchase._id }

                binding.rvOtherPurchaseVideos.adapter = VideoAdapter(tempVideoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(tempVideoPurchaseList.isEmpty()) {
                    binding.rvOtherPurchaseVideos.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvOtherPurchaseVideos.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })
        }
        else{
            //User

            val id = LoginUser.ottId

            viewModel.getUserVideoPurchase(this, jsonObject.toString().toRequestBody(), id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var videoPurchaseList = listOf<VideoPurchase>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videoPurchaseList = ParseResponseData.parseVideoPurchase(mJSONArray)
                }

                val tempVideoPurchaseList = videoPurchaseList.filter { video-> video._id  != videoPurchase._id }

                binding.rvOtherPurchaseVideos.adapter = VideoAdapter(tempVideoPurchaseList) { selectedItem: VideoPurchase ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(tempVideoPurchaseList.isEmpty()) {
                    binding.rvOtherPurchaseVideos.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvOtherPurchaseVideos.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })
        }
    }

    private fun displayDetails(videoPurchase: VideoPurchase){
        Glide.with(this)
            .load(videoPurchase.mobile_image)
            .override(binding.image.width,binding.image.height)
            .apply { UiUtils.getRequestOptionHorizontal() }
            .into(binding.image)
        binding.videoDetails = videoPurchase
        Log.d("displayDetails", "displayDetails: "+videoPurchase.shareLink)
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

    private fun shareVideoButton() {
        var shareUrl: String = videoPurchase.shareLink
        /*if (PrefUtils.getInstance(this).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {
            val encodedBase64Key = Crypt.encodeKey(APIConstants.URLs.SECRET_KEY)
            UiUtils.log(TAG, "EncodedBase64Key = $encodedBase64Key")
            var encryptedId: String? = null
            try {
                encryptedId = Crypt.encrypt(id.toString(), encodedBase64Key)
            } catch (e: Exception) {
                UiUtils.log(TAG, Log.getStackTraceString(e))
            }
            var refEncoded = ""
            try {
                refEncoded = URLEncoder.encode(encryptedId, "UTF-8")
                UiUtils.log(TAG, "Encode -> $refEncoded")
            } catch (e: Exception) {
                UiUtils.log(TAG, Log.getStackTraceString(e))
            }
            shareUrl = video.getShareUrl() + "&ref=" + r900efEncoded
        }*/
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            """
            ${getString(R.string.check_out_this_video)} ${videoPurchase.purchaseId}
            $shareUrl
            """.trimIndent()
        )
        startActivity(shareIntent)
    }

}