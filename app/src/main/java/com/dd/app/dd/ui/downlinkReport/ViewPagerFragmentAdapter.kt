package com.dd.app.dd.ui.downlinkReport

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dd.app.dd.ui.downlinkReport.day.ReportHistoryDayFragment
import com.dd.app.dd.ui.downlinkReport.month.ReportHistoryMonthFragment
import com.dd.app.dd.ui.downlinkReport.week.ReportHistoryWeekFragment


class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private var titles = arrayOf("Day", "Week", "Month")

    constructor(fragmentActivity: FragmentActivity, titlesArray: Array<String>) : this(fragmentActivity) {
        titles = titlesArray
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ReportHistoryDayFragment()
            1 -> return ReportHistoryWeekFragment()
            2 -> return ReportHistoryMonthFragment()
        }
        return ReportHistoryDayFragment()
    }
}