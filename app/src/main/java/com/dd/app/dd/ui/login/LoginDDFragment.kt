package com.dd.app.dd.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dd.app.R
import com.dd.app.databinding.FragmentLoginDdBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.network.ApiConstants.Companion.DURATION
import com.dd.app.dd.network.ApiConstants.Companion.SCALE_X
import com.dd.app.dd.network.ApiConstants.Companion.SCALE_Y
import com.dd.app.dd.ui.profile.ProfileFragment
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.LOGIN_TYPE
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.OTT_ID
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.USER_EMAIL
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.USER_ID
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.USER_MOBILE
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.USER_NAME
import com.dd.app.dd.utils.sharedPreference.PrefKeysDD.Companion.USER_PICTURE
import com.dd.app.dd.utils.sharedPreference.SharedPreferenceHelper
import com.dd.app.util.UiUtils
import com.dd.app.ui.activity.NewLoginActivity
import com.skydoves.elasticviews.ElasticAnimation
import com.skydoves.elasticviews.ElasticFinishListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginDDFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var mContext: Context? = null

    private lateinit var binding: FragmentLoginDdBinding

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
        //return inflater.inflate(R.layout.fragment_login_dd, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_dd, container, false)

        checkLoginStatus()

        binding.btnLogin.setOnClickListener { v ->
            ElasticAnimation(v).setScaleX(SCALE_X).setScaleY(SCALE_Y).setDuration(DURATION)
                .setOnFinishListener(ElasticFinishListener {
                    // Do something after duration time
                    val newIntent = Intent(mContext, NewLoginActivity::class.java)
                    startActivity(newIntent)
                }).doAction()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val sharedPreferenceHelper = SharedPreferenceHelper(requireContext())
        LoginUser.apply {
            authToken = sharedPreferenceHelper.getStringValue(PrefKeysDD.SESSION_TOKEN, "").toString()
            role = sharedPreferenceHelper.getStringValue(LOGIN_TYPE, "").toString()

            userId = sharedPreferenceHelper.getStringValue(USER_ID, "").toString()
            ottId = sharedPreferenceHelper.getStringValue(OTT_ID, "").toString()
            email = sharedPreferenceHelper.getStringValue(USER_EMAIL, "").toString()
            name = sharedPreferenceHelper.getStringValue(USER_NAME, "").toString()
            mobile = sharedPreferenceHelper.getStringValue(USER_MOBILE, "").toString()
            profilePhoto = sharedPreferenceHelper.getStringValue(USER_PICTURE, "").toString()
        }
        UiUtils.log(TAG,"AuthToken->"+ LoginUser.authToken)
        if (LoginUser.authToken.isNotEmpty()) {
            //replace fragment
            UiUtils.log(TAG,"Profile Fragment Open")
            /*val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
            finish()*/
            replaceFragmentWithAnimation(ProfileFragment())
        }
    }

    fun replaceFragmentWithAnimation(fragment: Fragment/*, tag: String?, toBackStack: Boolean*/) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        /*if (toBackStack) {
            transaction.addToBackStack(tag)
        }*/
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    companion object {

        private val TAG  = LoginDDFragment::class.simpleName.toString()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginDDFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}