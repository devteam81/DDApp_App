package com.dd.app.dd.ui.downlinkReport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dd.app.R
import com.dd.app.databinding.ActivityDownlinkReportBinding
import com.dd.app.dd.ui.downlinkReport.day.ReportHistoryDayFragment
import com.dd.app.util.UiUtils
import com.dd.app.dd.ui.downlinkReport.month.ReportHistoryMonthFragment
import com.google.android.material.tabs.TabLayoutMediator

class DownlinkReportActivity : AppCompatActivity() {

    private val TAG  = DownlinkReportActivity::class.simpleName.toString()
    private lateinit var binding: ActivityDownlinkReportBinding

    private val titles = arrayOf("Day", "Week", "Month")
    private lateinit var CURRENT_FRAGMENT: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_downlink_report)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downlink_report)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val pagerAdapter = ViewPagerFragmentAdapter(this)
        binding.viewpager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun setVisibilityForFragment(tag: String){

        UiUtils.log(TAG,tag)
        when(tag){
            titles[0] -> {
                UiUtils.log(TAG,"Inside " + titles[0])
                /*binding.ivHome.visibility = View.GONE
                binding.ivVideos.visibility = View.VISIBLE
                binding.ivProfile.visibility = View.VISIBLE

                binding.tvHome.visibility = View.VISIBLE
                binding.tvVideos.visibility = View.GONE
                binding.tvProfile.visibility = View.GONE*/
                if(CURRENT_FRAGMENT != titles[0])
                    navigateToScreen(ReportHistoryDayFragment(), titles[0],false)
            }
            titles[1] -> {
                UiUtils.log(TAG,"Inside " + titles[1])
               /* binding.ivHome.visibility = View.VISIBLE
                binding.ivVideos.visibility = View.GONE
                binding.ivProfile.visibility = View.VISIBLE

                binding.tvHome.visibility = View.GONE
                binding.tvVideos.visibility = View.VISIBLE
                binding.tvProfile.visibility = View.GONE*/
                if(CURRENT_FRAGMENT != titles[1])
                    navigateToScreen(ReportHistoryDayFragment(), titles[1],false)
            }
            titles[2] -> {
                UiUtils.log(TAG,"Inside " + titles[2])
                /*binding.ivHome.visibility = View.VISIBLE
                binding.ivVideos.visibility = View.VISIBLE
                binding.ivProfile.visibility = View.GONE

                binding.tvHome.visibility = View.GONE
                binding.tvVideos.visibility = View.GONE
                binding.tvProfile.visibility = View.VISIBLE*/
                if(CURRENT_FRAGMENT != titles[2])
                    navigateToScreen(ReportHistoryMonthFragment(), titles[2],false)
            }
            else -> UiUtils.log(TAG,"Else")
        }
    }

    private fun navigateToScreen(fragment: Fragment, tag: String, toBackStack: Boolean){

        val transaction = supportFragmentManager.beginTransaction()
        CURRENT_FRAGMENT = tag
        //this.fragment = fragment
        if (toBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}