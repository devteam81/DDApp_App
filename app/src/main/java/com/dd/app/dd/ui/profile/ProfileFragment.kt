package com.dd.app.dd.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dd.app.R
import com.dd.app.databinding.FragmentProfileBinding
import com.dd.app.dd.model.Graph
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.model.ResponseLogin
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.network.ApiConstants.Companion.PROFILE
import com.dd.app.dd.network.RetrofitClient
import com.dd.app.dd.network.UserLoginDetails
import com.dd.app.dd.ui.coins.CoinDetailsActivity
import com.dd.app.dd.ui.downlink.DownlinkActivity
import com.dd.app.dd.ui.purchase.VideoPurchaseActivity
import com.dd.app.dd.ui.settings.ProfileEditActivity
import com.dd.app.dd.ui.share.VideoShareActivity
import com.dd.app.dd.ui.transactions.TransactionDetailActivity
import com.dd.app.dd.utils.graph.GraphUtil
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper
import com.dd.app.util.UiUtils
import com.dd.app.util.dialog.LoadingDialog
import com.dd.app.util.sharedpref.Utils
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private val TAG  = ProfileFragment::class.simpleName.toString()

    private var param1: String? = null
    private var param2: String? = null

    private var mContext: Context? = null

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    private val handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        // binding.videosViewModel = videosViewModel
        binding.lifecycleOwner = this
        // setUserData()
        //observeViewModel(profileViewModel)

        /* //Profile Photo Click
        binding.img.setOnClickListener { v ->
            ElasticAnimation(v)
                .setScaleX(SCALE_X)
                .setScaleY(SCALE_Y)
                .setDuration(DURATION)
                .setOnFinishListener(ElasticFinishListener {
                    val editProfileBottomSheet: BottomSheetDialogFragment = EditProfileBottomSheet()
                    editProfileBottomSheet.show(
                        activity!!.supportFragmentManager,
                        editProfileBottomSheet.tag
                    )
                }) // Do something after duration time
                .doAction()
        }*/

        binding.llDownloadInvoice.setOnClickListener {
            val transactionIntent = Intent(activity, TransactionDetailActivity::class.java)
            startActivity(transactionIntent)
        }

        binding.llTotalCoins.setOnClickListener {
            val totalCoinsIntent = Intent(activity, CoinDetailsActivity::class.java)
            startActivity(totalCoinsIntent)
        }

        binding.llDownloadlink.setOnClickListener {
            val totalCoinsIntent = Intent(activity, DownlinkActivity::class.java)
            startActivity(totalCoinsIntent)
        }

        binding.llCoinDetails.setOnClickListener {
            val totalCoinsIntent = Intent(activity, CoinDetailsActivity::class.java)
            startActivity(totalCoinsIntent)
        }

        binding.llVideoShare.setOnClickListener {
            val videoShareIntent = Intent(activity, VideoShareActivity::class.java)
            startActivity(videoShareIntent)
        }

        binding.llVideoPurchase.setOnClickListener {
            val totalCoinsIntent = Intent(activity, VideoPurchaseActivity::class.java)
            startActivity(totalCoinsIntent)
        }

        binding.imgSettings.setOnClickListener{
            val totalCoinsIntent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(totalCoinsIntent)
        }

        LoadingDialog.getInstance().show(mContext)

        return binding.root
    }


    override fun onResume() {
        checkLoginStatus()
        // Update TextView every second
        /*handler.post(object : Runnable {
            override fun run() {
                // Keep the postDelayed before the updateTime(), so when the event ends, the handler will stop too.
                handler.postDelayed(this, 1000)
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val text = "2023-04-07"
                val date = formatter.parse(text)
                val time = AppUtils.getTimer(date)
                UiUtils.log(TAG,time)
                if(time.equals("")) {
                    // Stop Handler
                    handler.removeMessages(0)
                    UiUtils.log(TAG,"Subscription Completed")
                }
            }
        })*/
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        handler.removeMessages(0)
    }

    private fun checkLoginStatus(){

        val sharedPreferenceHelper = SharedPreferenceHelper(requireContext())
        LoginUser.apply {
            UiUtils.log(TAG,"Value present in shared-> "+sharedPreferenceHelper.getStringValue(PrefKeysDD.SESSION_TOKEN, "").toString())

            authToken = sharedPreferenceHelper.getStringValue(PrefKeysDD.SESSION_TOKEN, "").toString()
            role = sharedPreferenceHelper.getStringValue(PrefKeysDD.LOGIN_TYPE, "").toString()

            userId = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_ID, "").toString()
            ottId = sharedPreferenceHelper.getStringValue(PrefKeysDD.OTT_ID, "").toString()
            email = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_EMAIL, "").toString()
            name = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_NAME, "").toString()
            mobile = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_MOBILE, "").toString()
            profilePhoto = sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_PICTURE, "").toString()
            coins = sharedPreferenceHelper.getIntValue(PrefKeysDD.USER_COINS, 0)

            Log.d("checkLoginStatus", "checkLoginStatus: 1"+sharedPreferenceHelper.getStringValue(PrefKeysDD.SESSION_TOKEN, "").toString())
            Log.d("checkLoginStatus", "checkLoginStatus: 2"+sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_ID, "").toString())
            Log.d("checkLoginStatus", "checkLoginStatus: 3"+sharedPreferenceHelper.getStringValue(PrefKeysDD.USER_MOBILE, "").toString())
            Log.d("checkLoginStatus", "checkLoginStatus: "+ UiUtils.log(TAG,"AuthToken->"+ LoginUser.authToken))
        }
        UiUtils.log(TAG,"AuthToken->"+ LoginUser.authToken)
        Log.d("checkLoginStatus_test", "checkLoginStatus: "+ LoginUser.authToken)
        if(LoginUser.authToken.isEmpty()){
          //  Utils.logOutUserInDevice(context)
        }else{
            binding.userName.text = LoginUser.name.toString()
            setUserData()
            observeViewModel(profileViewModel)
        }
        /*if(LoginUser.role.equals(ApiConstants.ADMIN,true)){
            binding.llDownloadlink.setOnClickListener {
                val totalCoinsIntent = Intent(activity, DownlinkActivity::class.java)
                startActivity(totalCoinsIntent)
            }
        }*/
    }

    private fun setUserData(){
        if(LoginUser.name != "")
            binding.userName.text = LoginUser.name
        else
            binding.userName.visibility = View.GONE

        if(LoginUser.email != "")
            binding.userEmail.text = LoginUser.email
        else
            binding.userEmail.visibility = View.GONE

        if(LoginUser.mobile != "")
            binding.userPhone.text = LoginUser.name
        else
            binding.userPhone.visibility = View.GONE
    }

    private fun observeViewModel(viewModel: ProfileViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())


        /* val prefUtils = PrefUtils.getInstance(requireContext())
         val email: String = prefUtils.getStringValue(PrefKeys.USER_EMAIL, "")
         UiUtils.log(TAG,"Login Email ->"+ email)
         val loginDetails = async {doLoginUser(email,"123456")}

         val finalValue = loginDetails.await( )
         UiUtils.log(TAG, "mContext is not empty -> AuthToken->"+finalValue)*/
        if(LoginUser.authToken.isNotEmpty()){
            UiUtils.log(TAG, "role :"+LoginUser.role)
            if(LoginUser.role.isNotEmpty()){

                if(LoginUser.role.equals(ApiConstants.ADMIN,true)){


                    // Update the graph when the data changes
                    viewModel.getAdminGraphData(requireContext(), jsonObject.toString().toRequestBody())!!.observe(requireActivity(), Observer {

                        LoadingDialog.getInstance().dismiss()
                        if(it.success){
                            try {

                                UiUtils.log(TAG, "Response: " + it.data)
                                val jsonInString = Gson().toJson(it.data)
                                var graphList = listOf<Graph>()
                                if(jsonInString.length>2) {
                                    val mJSONArray = JSONArray(jsonInString)
                                    graphList = ParseResponseData.parseGraphLineData(mJSONArray, ApiConstants.PROFILE)


                                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                                    mContext?.let { context -> GraphUtil(context).setDataForLineChart(binding.lineChart, graphList as ArrayList<Graph>, 1, false, true,
                                        PROFILE) }

                                }
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                        }else
                        {
                            UiUtils.log(TAG, "Message: " + it.message)
                            if(it.message != ""){
                                when(it.message){
                                    "unauthorized"->{
                                        Utils.logOutUserInDevice(context)
                                        UiUtils.showLongToast(context, getString(R.string.session_expired))
                                        UiUtils.log(TAG, getString(R.string.session_expired))
                                    }
                                }
                            }
                        }
                    })

                    //update details when data changes
                    viewModel.getAdminProfileData(requireContext(), jsonObject.toString().toRequestBody())!!.observe(requireActivity(), Observer {

                        LoadingDialog.getInstance().dismiss()
                        if(it.success){
                            try {


                                UiUtils.log(TAG, "Response: " + it.data)
                                val jsonInString = Gson().toJson(it.data)
                                //var graphList = listOf<Graph>()
                                if(jsonInString.length>2) {
                                    val mJSONObject = JSONObject(jsonInString)
                                    val spentCoinsObject = mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject
                                    val totalCoinsObject = mJSONObject.getJSONArray("total_coins").get(0) as JSONObject
                                    val totalVideoShareObject = mJSONObject.getJSONArray("total_share_video").get(0) as JSONObject
                                    //Utils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                                    binding.txtTotalCoins.text = (totalCoinsObject.getString("total_coins").toDouble() +
                                            spentCoinsObject.getString("total_spent_coins") .toDouble()).toInt().toString()
                                        //totalCoinsObject.getString("total_coins").toDouble().toInt().toString()
                                    binding.txtTotalVideoShare.text = totalVideoShareObject.getString("total_count").toDouble().toInt().toString()
                                    try {
                                        binding.txtTotalVideoPurchase.text = mJSONObject.getString("total_purchase").toDouble().toInt().toString()
                                    }catch (e: Exception){
                                        e.printStackTrace()
                                    }

                                    binding.txtCurrentCoins.text = totalCoinsObject.getString("total_coins").toDouble().toInt().toString()
                                        /*(totalCoinsObject.getString("total_coins").toDouble() -
                                                spentCoinsObject.getString("total_spent_coins") .toDouble()).toInt().toString()*/


                                    binding.txtYourCurrentCoins.text =
                                        totalCoinsObject.getString("total_coins").toDouble().toInt().toString()
                                    binding.txtYourSpentCoins.text =
                                        spentCoinsObject.getString("total_spent_coins").toDouble().toInt().toString()
                                    binding.txtYourTotalCoins.text = (totalCoinsObject.getString("total_coins").toDouble() +
                                                spentCoinsObject.getString("total_spent_coins") .toDouble()).toInt().toString()
                                }

                            }catch (e: Exception){
                                e.printStackTrace()
                            }

                        }else
                        {
                            UiUtils.log(TAG, "Message: " + it.message)
                            if(it.message != ""){
                                when(it.message){
                                    "unauthorized"->{
                                        Utils.logOutUserInDevice(context)
                                        UiUtils.showLongToast(context, getString(R.string.session_expired))
                                        UiUtils.log(TAG, getString(R.string.session_expired))
                                    }
                                }
                            }
                        }
                    })
                }
                else{
                    // User

                    val id = LoginUser.ottId



                    // Update the graph when the data changes
                    viewModel.getUserGraphData(requireContext(), jsonObject.toString().toRequestBody(), id)!!.observe(requireActivity(), Observer {

                        LoadingDialog.getInstance().dismiss()
                        if(it.success){
                            try {
                                UiUtils.log(TAG, "Response: " + it.data)
                                val jsonInString = Gson().toJson(it.data)
                                var graphList = listOf<Graph>()
                                if(jsonInString.length>2) {
                                    val mJSONArray = JSONArray(jsonInString)
                                    graphList = ParseResponseData.parseGraphLineData(mJSONArray, ApiConstants.PROFILE)


                                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                                    mContext?.let { context -> GraphUtil(context).setDataForLineChart(binding.lineChart, graphList as ArrayList<Graph>, 1, false, true,
                                        PROFILE) }

                                }

                            }catch (e: Exception){
                                e.printStackTrace()
                            }

                        }else
                        {
                            UiUtils.log(TAG, "Message: " + it.message)
                            if(it.message != ""){
                                when(it.message){
                                    "unauthorized"->{
                                        Utils.logOutUserInDevice(context)
                                        UiUtils.showLongToast(context, getString(R.string.session_expired))
                                        UiUtils.log(TAG, getString(R.string.session_expired))
                                    }
                                }
                            }


                        }
                    })

                    //update details when data changes
                    viewModel.getUserProfileData(requireContext(), jsonObject.toString().toRequestBody(), id)!!.observe(requireActivity(), Observer {

                        LoadingDialog.getInstance().dismiss()
                        if(it.success){
                            try {
                                UiUtils.log(TAG, "Response: " + it.data)
                                val jsonInString = Gson().toJson(it.data)
                                //var graphList = listOf<Graph>()
                                if(jsonInString.length>2) {
                                    val mJSONObject = JSONObject(jsonInString)
                                    val spentCoins = mJSONObject.getString("spent_amt")
                                    val totalCoins = mJSONObject.getString("coins")
                                    val totalVideoShare = mJSONObject.getString("share_video")
                                    val totalVideoPurchase = mJSONObject.getString("purchase_video")
                                    //Utils.log(TAG,"Spent Coins-> "+ (mJSONObject.getJSONArray("spent_coins").get(0) as JSONObject).getString("total_spent_coins"))

                                    binding.txtTotalCoins.text = (totalCoins.toDouble() + spentCoins.toDouble()).toInt().toString()
                                    binding.txtCurrentCoins.text = totalCoins.toDouble().toInt().toString()/*(totalCoins.toDouble() - spentCoins.toDouble()).toInt().toString()*/
                                    binding.txtTotalVideoShare.text = totalVideoShare.toDouble().toInt().toString()
                                    binding.txtTotalVideoPurchase.text = totalVideoPurchase.toDouble().toInt().toString()

                                    binding.txtYourCurrentCoins.text = totalCoins.toDouble().toInt().toString()
                                    binding.txtYourSpentCoins.text = spentCoins.toDouble().toInt().toString()
                                    binding.txtYourTotalCoins.text = (totalCoins.toDouble() + spentCoins.toDouble()).toInt().toString()

                                }
                            }catch (e: Exception){
                                e.printStackTrace()
                            }

                        }else
                        {
                            UiUtils.log(TAG, "Message: " + it.message)
                            if(it.message != ""){
                                when(it.message){
                                    "unauthorized"->{
                                        Utils.logOutUserInDevice(context)
                                        UiUtils.showLongToast(context, getString(R.string.session_expired))
                                        UiUtils.log(TAG, getString(R.string.session_expired))
                                    }
                                }
                            }
                        }
                    })


                }
            }else{
                Utils.logOutUserInDevice(context)
                //UiUtils.showLongToast(context, getString(R.string.session_expired))
                //UiUtils.log(TAG, getString(R.string.session_expired))
            }
        }else{
            //replaceFragmentWithAnimation(SubscribeDDFragment.newInstance("Profile",""))
            Utils.logOutUserInDevice(context)
            UiUtils.showLongToast(context, getString(R.string.session_expired))
            UiUtils.log(TAG, getString(R.string.session_expired))
        }

    }

    private fun doLoginUser(userEmail: String, userPassword: String): String
    {
        val jsonObject = JSONObject()
        //jsonObject.put("email", "demotest@81tech.app")
        //jsonObject.put("email", "ashish@gmail.com")
        //jsonObject.put("password", "123456")
        jsonObject.put("email", userEmail)
        jsonObject.put("password", userPassword)
        UiUtils.log("jsonObject :", jsonObject.toString())
        var authentication: String =""

        val response = RetrofitClient.apiInterface.loginApi(jsonObject.toString().toRequestBody())
        response.enqueue( object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>)
            {
                if(response.body() != null) {

                    UiUtils.log(TAG, "Data-> " + response.body().toString())
                    val mJSONObject = response.body()!!

                    if (mJSONObject.success) {

                        //Utils.log("ID :", ""+mJSONObject.getString("_id"))
                        mContext?.let {
                            UserLoginDetails.setUserLoggedIn(
                                it,
                                mJSONObject.user_id,
                                mJSONObject.ott_id,
                                mJSONObject.auth_token,
                                mJSONObject.role,
                                mJSONObject.user_email,
                                mJSONObject.user_name,
                                "",
                                "",
                                "",
                                "",
                                "",
                                mJSONObject.coins
                            )
                            UiUtils.log(TAG, "mContext is not empty")
                        }

                        /*LoginUser.apply {
                            authToken =  mJSONObject.auth_token
                            role = mJSONObject.role

                            userId = mJSONObject.user_id
                            ottId = mJSONObject.ott_id
                            email = mJSONObject.user_email
                            name = mJSONObject.user_name
                            mobile = ""
                            profilePhoto = ""
                        }.also {
                            authentication = it.authToken
                        }*/

                        checkLoginStatus()

                        //replaceFragmentWithAnimation(ProfileFragment())

                        //Utils.log("TAG", "Age->" + Utils.getAge("19-08-1990"))
                        /*val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        finish()*/
                    }else
                    {
                        UiUtils.log(TAG, "Message-> "+mJSONObject.message)
                        mContext?.let{Toast.makeText(it,mJSONObject.message, Toast.LENGTH_LONG).show()}
                    }
                }
                else if(response.errorBody() != null){
                    val jObjError = JSONObject(response.errorBody()!!.string())

                    if (!jObjError.getBoolean("success")) {
                        UiUtils.log(TAG, "Message-> $jObjError")
                        UiUtils.log(TAG, "" + jObjError.getString("message"))
                        mContext?.let { Toast.makeText(it,jObjError.getString("message"), Toast.LENGTH_LONG).show() }
                    }
                }

            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                t.printStackTrace()
                UiUtils.log(TAG, "Error-> " + t.message)
            }
        })

        return authentication
    }

    private fun replaceFragmentWithAnimation(fragment: Fragment/*, tag: String?, toBackStack: Boolean*/) {
        if(mContext!=null) {
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            /*if (toBackStack) {
            transaction.addToBackStack(tag)
        }*/
            transaction.replace(R.id.container, fragment)
            transaction.commitAllowingStateLoss()
        }else
        {
            UiUtils.log(TAG,"parentFragmentManager is null")
        }
    }

    companion object {

        private val TAG  = ProfileFragment::class.simpleName.toString()


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}