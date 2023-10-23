package com.dd.app.dd.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dd.app.R
import com.dd.app.databinding.ActivityProfileEditBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.model.ResponseBody
import com.dd.app.dd.network.ApiConstants.Companion.DURATION
import com.dd.app.dd.network.ApiConstants.Companion.SCALE_X
import com.dd.app.dd.network.ApiConstants.Companion.SCALE_Y
import com.dd.app.dd.network.RetrofitClient
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper
import com.dd.app.util.UiUtils
import com.dd.app.dd.network.UserLoginDetails
import com.google.gson.Gson
import com.skydoves.elasticviews.ElasticAnimation
import com.skydoves.elasticviews.ElasticFinishListener
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEditActivity : AppCompatActivity() {
    companion object{
        private val TAG  = ProfileEditActivity::class.simpleName.toString()
    }

    private lateinit var binding: ActivityProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)

        updateName()
        //binding.edtName.setText(LoginUser.name.toString())

        binding.btnUpdate.setOnClickListener { v ->
            ElasticAnimation(v).setScaleX(SCALE_X).setScaleY(SCALE_Y).setDuration(DURATION)
                .setOnFinishListener(ElasticFinishListener {
                    // Do something after duration time
                    UiUtils.log(TAG,"Update Clicked")
                    //if (validateFields(binding.edtEmail.text.toString(),binding.edtPassword.text.toString()))
                    val name = binding.edtName.text.toString().trim()
                    if(name.isNotEmpty())
                        updateProfileDetails(name)
                    else
                        Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()

                }).doAction()
        }

        /*binding.btnChangePassword.setOnClickListener { v ->
            ElasticAnimation(v).setScaleX(SCALE_X).setScaleY(SCALE_Y).setDuration(DURATION)
                .setOnFinishListener(ElasticFinishListener {
                    // Do something after duration time
                    UiUtils.log(TAG,"Change Password Clicked")
                    val passwordIntent = Intent(this, ChangePasswordActivity::class.java)
                    startActivity(passwordIntent)

                }).doAction() }*/

        //binding.btnLogout.setOnClickListener { logoutUser() }
    }


    private fun updateProfileDetails(name: String) {
        val jsonObject = JSONObject()
        //jsonObject.put("email", "demotest@81tech.app")
        jsonObject.put("name", name)
        UiUtils.log("jsonObject :", jsonObject.toString())

        val response = RetrofitClient.apiInterface.updateUserName(jsonObject.toString().toRequestBody())
        response.enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                if(response.body() != null) {
                    UiUtils.log(TAG, "Data -> " + response.body()!!.data)
                    val success = response.body()!!.success
                    val data = response.body()!!.data?: ""
                    val msg = response.body()!!.message?: ""
                    if (success) {

                        //Utils.log("ID :", ""+mJSONObject.getString("_id"))
                        val jsonInString = Gson().toJson(data)
                        val dataObject = JSONObject(jsonInString)
                        UiUtils.log(TAG, "Name-> " + dataObject.getString("name"))
                        val sharedPreferenceHelper = SharedPreferenceHelper(this@ProfileEditActivity)
                        sharedPreferenceHelper.setValue("FIRST_NAME", dataObject.getString("name"))

                        LoginUser.name = dataObject.getString("name")
                        updateName()
                        /*val intent = Intent(baseContext, MainActivity::class.java)
                        startActivity(intent)*/
                    }else
                    {
                        UiUtils.log(TAG, ""+msg)
                        Toast.makeText(baseContext,msg, Toast.LENGTH_LONG).show()
                    }
                }else
                {
                    val msg = response.message()?: ""
                    UiUtils.log(TAG, "Message-> ${msg.lowercase()}")
                    if(msg != ""){
                        when(msg.lowercase()){
                            "unauthorized"-> {
                                UserLoginDetails.setUserLoggedOut(this@ProfileEditActivity)
                                PrefKeysDD.logoutUser(this@ProfileEditActivity)
                                /*LoginUser.apply {
                                    authToken = ""
                                }*/

                                UiUtils.showLongToast(this@ProfileEditActivity, getString(R.string.session_expired))
                                UiUtils.log(TAG, "Your session has expired.")

                                /*val restartActivity = Intent(this@ProfileEditActivity, LoginActivity::class.java)
                                restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(restartActivity)*/
                                finish()
                            }
                            else -> UiUtils.log(TAG, "Else")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                UiUtils.log(TAG, "Error-> " + t.message)
            }
        })

    }

    private fun updateName(){
        binding.edtName.setText(LoginUser.name.toString())
    }

    private fun logoutUser(){

        UserLoginDetails.setUserLoggedOut(this)
        LoginUser.apply {
            authToken = ""
        }

        UiUtils.showLongToast(this, getString(R.string.logout_message))
        UiUtils.log(TAG, getString(R.string.logout_message))

        finish()

    }
}