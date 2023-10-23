package com.dd.app.dd.ui.wallet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dd.app.R
import com.dd.app.databinding.ActivityAddBankAccountBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.profile.ProfileViewModel
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper
import com.dd.app.util.UiUtils
import com.dd.app.util.sharedpref.Utils
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AddBankAccountActivity : AppCompatActivity() {

    companion object {
        private val TAG = AddBankAccountActivity::class.simpleName.toString()
    }

    private lateinit var binding: ActivityAddBankAccountBinding
    private lateinit var walletDetailModel: WalletDetailModel
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var accountID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_bank_account)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bank_account)

        binding.toolbar.setNavigationOnClickListener { finish() }
        accountID = ""
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        walletDetailModel = ViewModelProvider(this)[WalletDetailModel::class.java]
        binding.lifecycleOwner = this
        checkLoginStatus()

        binding.submitBtn.setOnClickListener {
            saveBankDetails()
        }

        binding.withdrawBtn.setOnClickListener {
            if (this::accountID.isInitialized && accountID.isNotEmpty()) {
                UiUtils.log(TAG, "Total Coins: ${LoginUser.coins}")
                if (LoginUser.coins >= 1000)
                    withdrawAmountConfirmation()
                else
                    Toast.makeText(
                        this,
                        "To withdraw you should have minimum 1000 coins",
                        Toast.LENGTH_LONG
                    ).show()
            } else {
                Toast.makeText(
                    this,
                    "Please add account details",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun observeViewModel(viewModel: WalletDetailModel) {

        UiUtils.showLoadingDialog(this@AddBankAccountActivity)

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())

        UiUtils.log(TAG, "role :" + LoginUser.role)
        if (LoginUser.role.equals(ApiConstants.ADMIN, true)) {
            // Update the list when the data changes

            UiUtils.hideLoadingDialog()
        } else {
            //User

            val id = LoginUser.ottId
            jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
            jsonObject.put("ott_id", LoginUser.ottId)

            //Get Wallet Details
            viewModel.getUserWalletDetails(this, jsonObject.toString().toRequestBody(), id)!!
                .observe(this, Observer {

                    UiUtils.hideLoadingDialog()

                    UiUtils.log(TAG, "Response: " + it.data)
                    val jsonInString = Gson().toJson(it.data)
                    if (jsonInString.length > 2) {
                        val mJSONArray = JSONArray(jsonInString)

                        if (mJSONArray.getJSONObject(0) != null) {

                            val userDetails = mJSONArray.getJSONObject(0)

                            accountID = userDetails.optString("_id") ?: ""
                            binding.edtBankName.setText(userDetails.optString("bank_name") ?: "")
                            binding.edtPersonName.setText(
                                userDetails.optString("benificiery_name") ?: ""
                            )
                            binding.edtAccountNumber.setText(
                                userDetails.optString("account_no") ?: ""
                            )
                            binding.edtIfscCode.setText(userDetails.optString("ifsc_code"))
                        }
                    }

                })

            jsonObject.put("filter_id", 3)
            jsonObject.put("from_date", "")
            jsonObject.put("to_date", "")

            /*viewModel.transactionFilterForInvoice(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var invoiceList = listOf<Invoice>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    invoiceList = ParseResponseData.parseInvoice(mJSONArray)
                }

                val tempInvoiceList = invoiceList

                binding.rvBankDetails.layoutManager = LinearLayoutManager(this)
                binding.rvBankDetails.adapter = InvoiceAdapter(tempInvoiceList) { selectedItem: Invoice ->
                    listItemClicked(
                        selectedItem
                    )
                }

                if(tempInvoiceList.isEmpty()) {
                    binding.rvBankDetails.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                } else {
                    binding.rvBankDetails.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                }

            })*/
        }
    }

    private fun saveBankDetails() {

        val jsonObject = JSONObject()
        val id = LoginUser.ottId
        jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
        jsonObject.put("ott_id", LoginUser.ottId)
        jsonObject.put("bank_name", binding.edtBankName.text.toString())
        jsonObject.put("benificiery_name", binding.edtPersonName.text.toString())
        jsonObject.put("account_no", binding.edtAccountNumber.text.toString())
        jsonObject.put("ifsc_code", binding.edtIfscCode.text.toString())

        UiUtils.log("jsonObject :", jsonObject.toString())

        walletDetailModel.addUserWalletDetails(this, jsonObject.toString().toRequestBody())!!
            .observe(this, Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                if (jsonInString.length > 2) {
                    val id = LoginUser.ottId
                    jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
                    jsonObject.put("ott_id", LoginUser.ottId)
                    walletDetailModel.getUserWalletDetails(
                        this,
                        jsonObject.toString().toRequestBody(),
                        id
                    )
                }
            })
    }

    private fun withdrawAmountConfirmation() {
        val dialog = Dialog(this@AddBankAccountActivity)
        dialog.setContentView(R.layout.dialog_logout_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val txtYes = dialog.findViewById<TextView>(R.id.btnYes)
        val txtNo = dialog.findViewById<TextView>(R.id.btnNo)
        val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
        txtMessage.text = "You are withdrawing " + LoginUser.coins + " coins."
        txtNo.setOnClickListener { v: View? -> dialog.dismiss() }
        txtYes.setOnClickListener { v: View? ->
            dialog.dismiss()
            //Toast.makeText(this@AddBankAccountActivity,"Payment Done", Toast.LENGTH_SHORT).show();
            withdrawAmount()
        }
        if (!dialog.isShowing)
            dialog.show()
        val window = dialog.window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //window.setGravity(Gravity.CENTER);
    }

    private fun withdrawAmount() {

        val jsonObject = JSONObject()
        val id = LoginUser.ottId
        jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
        jsonObject.put("ott_id", LoginUser.ottId)
        jsonObject.put("withdrawal_amount", LoginUser.coins)
        jsonObject.put("account_id", accountID)


        UiUtils.log("jsonObject :", jsonObject.toString())

        /*walletDetailModel.withdrawUserWalletAmount(this, jsonObject.toString().toRequestBody())!!.observe(this, Observer {

            UiUtils.log(TAG, "Response: " + it.data)
            val jsonInString = Gson().toJson(it.data)
            if(jsonInString.length>2) {
                val mJSONArray = JSONObject(jsonInString)
                getCoinsDetails()

                val dialog = Dialog(this@AddBankAccountActivity)
                dialog.setContentView(R.layout.dialog_logout_popup)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val img = dialog.findViewById<ImageView>(R.id.img)
                val txtTitle = dialog.findViewById<TextView>(R.id.txt_title)
                txtTitle.visibility = View.GONE

                img.rotation = 0f
                if(it.success)
                img.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_tick, null))
                else
                    img.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_cross, null))

                val txtYes = dialog.findViewById<TextView>(R.id.btnYes)
                val txtNo = dialog.findViewById<TextView>(R.id.btnNo)
                val txtMessage = dialog.findViewById<TextView>(R.id.txt_message)
                txtYes.visibility = View.GONE
                txtMessage.text = it.message
                txtNo.text = "Ok"
                txtNo.setOnClickListener { v: View? -> dialog.dismiss() }

                if(!dialog.isShowing)
                    dialog.show()
                val window = dialog.window
                window!!.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            }

        })*/

    }

    private fun getCoinsDetails() {

        val jsonObject = JSONObject()

        val id = LoginUser.ottId
        jsonObject.put("app_id", "6124a1dec83ed94d9cca372e")
        jsonObject.put("ott_id", LoginUser.ottId)

        UiUtils.log(TAG, "role :" + LoginUser.role)
        if (LoginUser.role.isNotEmpty()) {
            if (LoginUser.role.equals(ApiConstants.ADMIN, true)) {

                //update details when data changes
                //update details when data changes
                profileViewModel.getAdminProfileData(this, jsonObject.toString().toRequestBody())!!
                    .observe(this, Observer {

                        UiUtils.log(TAG, "Response: " + it.data)
                        val jsonInString = Gson().toJson(it.data)
                        //var graphList = listOf<Graph>()
                        if (jsonInString.length > 2) {
                            val mJSONObject = JSONObject(jsonInString)
                            val spentCoinsObject =
                                mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject
                            val totalCoinsObject =
                                mJSONObject.getJSONArray("total_coins").get(0) as JSONObject
                            val totalVideoShareObject =
                                mJSONObject.getJSONArray("total_share_video").get(0) as JSONObject
                            //UiUtils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                            binding.txtTotalCoins.text =
                                (totalCoinsObject.getString("total_coins").toDouble() +
                                        spentCoinsObject.getString("total_spent_coins")
                                            .toDouble()).toInt().toString()
                            binding.txtSpentCoins.text =
                                spentCoinsObject.getString("total_spent_coins").toDouble().toInt()
                                    .toString()
                            binding.txtCurrentCoins.text =
                                totalCoinsObject.getString("total_coins").toDouble().toInt()
                                    .toString()

                        }
                    })
            } else {
                //update details when data changes
                profileViewModel.getUserProfileData(
                    this,
                    jsonObject.toString().toRequestBody(),
                    id
                )!!
                    .observe(this, Observer {

                        UiUtils.log(TAG, "Response: " + it.data)
                        val jsonInString = Gson().toJson(it.data)
                        //var graphList = listOf<Graph>()
                        if (jsonInString.length > 2) {

                            try {
                                val mJSONObject = JSONObject(jsonInString)
                                val spentCoins = mJSONObject.optString("spent_amt")
                                val totalCoins = mJSONObject.optString("coins")
                                binding.txtTotalCoins.text =
                                    (totalCoins.toDouble() + spentCoins.toDouble()).toInt()
                                        .toString()
                                binding.txtSpentCoins.text =
                                    spentCoins.toDouble().toInt().toString()
                                binding.txtCurrentCoins.text =
                                    totalCoins.toDouble().toInt().toString()
                                // Use the parsed values
                            } catch (e: JSONException) {
                                // Handle JSON parsing or extraction errors
                            }

//                            val mJSONObject = JSONObject(jsonInString)
//                            val spentCoins = mJSONObject.getString("spent_amt")
//                            val totalCoins = mJSONObject.getString("coins")

                            //UiUtils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))


                        }

                    })
            }
        } else {
            Utils.logOutUserInDevice(this@AddBankAccountActivity)
        }
    }

    private fun checkLoginStatus() {

        val sharedPreferenceHelper = SharedPreferenceHelper(this@AddBankAccountActivity)
        LoginUser.apply {
            UiUtils.log(
                TAG, "Value present in shared-> " + sharedPreferenceHelper.getStringValue(
                    PrefKeysDD.SESSION_TOKEN, ""
                ).toString()
            )
            authToken =
                sharedPreferenceHelper.getStringValue(PrefKeysDD.SESSION_TOKEN, "").toString()
            role = sharedPreferenceHelper.getStringValue(PrefKeysDD.LOGIN_TYPE, "").toString()

            userId = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_ID, "").toString()
            ottId = sharedPreferenceHelper.getStringValue(PrefKeysDD.OTT_ID, "").toString()
            email = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_EMAIL, "").toString()
            name = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_NAME, "").toString()
            mobile = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_MOBILE, "").toString()
            profilePhoto =
                sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_PICTURE, "").toString()
            coins = sharedPreferenceHelper.getIntValue(PrefKeysDD.USER_COINS, 0)
        }
        UiUtils.log(TAG, "AuthToken->" + LoginUser.authToken)
        UiUtils.log(TAG, "ottId->" + LoginUser.ottId)
        if (LoginUser.authToken.isEmpty()) {
            Utils.logOutUserInDevice(this@AddBankAccountActivity)
        } else {
            observeViewModel(walletDetailModel)
            Log.d("checkLoginStatus", "checkLoginStatus: " + walletDetailModel)
            getCoinsDetails()
        }
        /*if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            binding.llDownloadlink.setOnClickListener {
                val totalCoinsIntent = Intent(activity, DownlinkActivity::class.java)
                startActivity(totalCoinsIntent)
            }
        }*/
    }
}