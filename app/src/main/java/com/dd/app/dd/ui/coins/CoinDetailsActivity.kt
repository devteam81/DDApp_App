package com.dd.app.dd.ui.coins

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.app.R
import com.dd.app.databinding.ActivityCoinDetailsBinding
import com.dd.app.dd.model.CoinHistory
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.profile.ProfileViewModel
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CoinDetailsActivity : AppCompatActivity() {

    companion object{
        private val TAG  = CoinDetailsActivity::class.simpleName.toString()
    }

    private lateinit var binding: ActivityCoinDetailsBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var coinViewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_coin_details)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coin_details)

        binding.toolbar.setNavigationOnClickListener { finish() }

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        coinViewModel = ViewModelProvider(this)[CoinViewModel::class.java]

        binding.lifecycleOwner = this
        binding.rvVideoCoins.layoutManager = LinearLayoutManager(this)
        observeViewModel(profileViewModel, coinViewModel)


        /*if (Build.VERSION.SDK_INT >= 33) {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT
                ) {

                    exitOnBackPressed()
                }
            } else {
                onBackPressedDispatcher.addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {

                            UiUtils.log(TAG, "handleOnBackPressed: Exit")
                            exitOnBackPressed()
                        }
                    })
            }*/

    }

    fun exitOnBackPressed() {

        finish()
    }

    private fun observeViewModel(profileViewModel: ProfileViewModel, coinViewModel: CoinViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())
        val id = LoginUser.ottId

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            // Update the graph when the data changes
            coinViewModel.getAdminUserCoin(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var videosCoinList = listOf<CoinHistory>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videosCoinList = ParseResponseData.parseVideoCoin(mJSONArray)
                }
                binding.rvVideoCoins.adapter = CoinAdapter(videosCoinList) { selectedItem: CoinHistory ->
                    listItemClicked(
                        selectedItem
                    )
                }
                if(videosCoinList.isEmpty()) {
                    binding.rvVideoCoins.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvVideoCoins.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })

            //update details when data changes
            profileViewModel.getAdminProfileData(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                //var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    val mJSONObject = JSONObject(jsonInString)
                    val spentCoinsObject = mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject
                    val totalCoinsObject = mJSONObject.getJSONArray("total_coins").get(0) as JSONObject
                    val totalVideoShareObject = mJSONObject.getJSONArray("total_share_video").get(0) as JSONObject
                    //UiUtils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                    binding.txtTotalCoins.text = (totalCoinsObject.getString("total_coins").toDouble() +
                            spentCoinsObject.getString("total_spent_coins") .toDouble()).toInt().toString()
                    binding.txtSpentCoins.text = spentCoinsObject.getString("total_spent_coins").toDouble().toInt().toString()
                    binding.txtCurrentCoins.text = totalCoinsObject.getString("total_coins").toDouble().toInt().toString()


                }

            })

        }
        else{
            //User

            val id = LoginUser.ottId

            // Update the graph when the data changes
            coinViewModel.getUserCoinDetail(this, jsonObject.toString().toRequestBody(),id)!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var videosCoinList = listOf<CoinHistory>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    videosCoinList = ParseResponseData.parseVideoCoin(mJSONArray)
                }
                binding.rvVideoCoins.adapter = CoinAdapter(videosCoinList) { selectedItem: CoinHistory ->
                    listItemClicked(
                        selectedItem
                    )
                }
                if(videosCoinList.isEmpty()) {
                    binding.rvVideoCoins.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvVideoCoins.visibility = View.VISIBLE
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
                        val spentCoins = mJSONObject.getString("spent_amt")
                        val totalCoins = mJSONObject.getString("coins")
                        //UiUtils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                        binding.txtTotalCoins.text = (totalCoins.toDouble() + spentCoins.toDouble()).toInt().toString()
                        binding.txtSpentCoins.text = spentCoins.toDouble().toInt().toString()
                        binding.txtCurrentCoins.text =totalCoins.toDouble().toInt().toString()

                        // Use the parsed values
                    } catch (e: JSONException) {
                        // Handle JSON parsing or extraction errors
                    }


                }

            })
        }
    }

    private fun listItemClicked(coins: CoinHistory)
    {
        UiUtils.log(TAG, "Name: ${coins._id}")
        /*val intent = Intent(activity, MemberProfileActivity::class.java)
        UiUtils.log(TAG,"Pass ID: "+ member.id)
        intent.putExtra("member", member)
        //send whole model
        startActivity(intent)*/
    }
}