package com.dd.app.dd.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dd.app.R
import com.dd.app.databinding.FragmentSubscribeDdBinding
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.ui.profile.ProfileFragment
import com.dd.app.ui.activity.NewLoginActivity
import com.dd.app.util.UiUtils
import com.dd.app.util.sharedpref.PrefKeys
import com.dd.app.util.sharedpref.PrefUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SubscribeDDFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSubscribeDdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        UiUtils.log(TAG,"onResume")
        UiUtils.log(TAG,"AuthToken->"+ LoginUser.authToken)

        if (PrefUtils.getInstance(requireContext()).getBoolanValue(PrefKeys.IS_LOGGED_IN, false)) {

            if(param1.equals("Profile")) {
                binding.subscribeBtn.visibility = View.GONE
                binding.textViewProgress.text = "Something went wrong"
                param1 =""
            }else {
                replaceFragmentWithAnimation(ProfileFragment())
            }
        }

    }

    private fun replaceFragmentWithAnimation(fragment: Fragment/*, tag: String?, toBackStack: Boolean*/) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        /*if (toBackStack) {
            transaction.addToBackStack(tag)
        }*/
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscribe_dd, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscribe_dd, container, false)

        if(param1.equals("Profile")){

            binding.subscribeBtn.visibility = View.GONE
            binding.textViewProgress.text = "Something went wrong"
        }else{
            binding.subscribeBtn.setOnClickListener{
                startActivity(Intent(requireContext(), NewLoginActivity::class.java))
            }
        }




        return binding.root
    }

    companion object {

        private val TAG  = SubscribeDDFragment::class.simpleName.toString()

        @JvmStatic
        public fun newInstance(screen: String, param2: String) =
            SubscribeDDFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, screen)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}