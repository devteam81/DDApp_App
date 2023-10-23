package com.dd.app.dd.ui.downlinkReport.week

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dd.app.R
import com.dd.app.databinding.FragmentReportHistoryWeekBinding
import com.dd.app.dd.model.Graph
import com.dd.app.dd.model.LoginUser
import com.dd.app.dd.model.Period
import com.dd.app.dd.model.WeekGraph
import com.dd.app.dd.network.ApiConstants
import com.dd.app.dd.ui.downlinkReport.DownlinkReportViewModel
import com.dd.app.dd.utils.graph.GraphUtil
import com.dd.app.dd.utils.parsing.ParseResponseData
import com.dd.app.util.UiUtils
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportHistoryWeekFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var mContext: Context? = null

    private lateinit var binding: FragmentReportHistoryWeekBinding
    private lateinit var downlinkReportViewModel: DownlinkReportViewModel
    private lateinit var periodTitleAdapter: PeriodTitleAdapter
    private var periodList: ArrayList<Period> = arrayListOf()
    private var selectedPosition: Int = 0
    private var graphList: ArrayList<WeekGraph> = arrayListOf()

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
        //return inflater.inflate(R.layout.fragment_report_history_week, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_history_week, container, false)

        //mContext = context
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPeriodPicker.layoutManager = layoutManager
        periodTitleAdapter = PeriodTitleAdapter(requireContext(),periodList){ selectedItem: Period ->
            listItemClicked(
                selectedItem
            )
        }
        binding.rvPeriodPicker.adapter = periodTitleAdapter


        downlinkReportViewModel = ViewModelProvider(requireActivity())[DownlinkReportViewModel::class.java]
        observeViewModel(downlinkReportViewModel)

        return binding.root
    }

    private fun listItemClicked(period: Period)
    {
        UiUtils.log(TAG, "Name: ${period.title}")
        for (i in periodList.indices) {
            if(i==period.id){
                periodList[i].isSelected = true
                selectedPosition = i
            }else{
                periodList[i].isSelected = false
            }
        }
        periodTitleAdapter.notifyDataSetChanged()
        //periodTitleAdapter.notifyDataSetChanged();
        setGraphData(graphList[selectedPosition].weekGraphDataList!!)
    }

    private fun observeViewModel(viewModel: DownlinkReportViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())
        val id = LoginUser.ottId

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){

            // Update the graph when the data changes
            viewModel.getAdminDownlinkWeekReportGraphData(requireContext(), jsonObject.toString().toRequestBody(), id)!!.observe(requireActivity(), Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var graphWeekList = listOf<WeekGraph>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    graphWeekList = ParseResponseData.parseGraphLineWeekData(mJSONArray, ApiConstants.MONTH)
                    graphList = graphWeekList as ArrayList<WeekGraph>
                    UiUtils.log(TAG, "Final Res-> ${graphWeekList[0].weekGraphDataList?.size}")

                    periodList.clear()
                    for (i in graphWeekList.indices) {
                        periodList.add(Period(i,graphWeekList[i].week,false))
                    }

                    UiUtils.log(TAG,"Array-> "+ mJSONArray.getJSONObject(0).toString())
                    val totalCoins = mJSONArray.getJSONObject(0).getString("total_coins").toFloat().toInt()
                    val totalCustomer = mJSONArray.getJSONObject(0).getString("total_customer").toFloat().toInt()
                    setData (totalCoins,totalCustomer)

                    listItemClicked(periodList[selectedPosition])
                    //periodList[selectedPosition].isSelected = true
                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                    //setGraphData(graphList[selectedPosition].weekGraphDataList!!)
                    /*GraphUtil(requireContext()).setDataForLineChart(
                       binding.lineChart,
                       graphWeekList[0].weekGraphDataList as ArrayList<Graph>,
                       1,
                       true,
                       false,
                       ApiConstants.WEEK
                   )*/

                    //setDataForLineChart(binding.lineChart, graphList as ArrayList<Graph>, 1, true, false, MONTH)
                }
            })
        }
        else{
            //User
            val id = LoginUser.ottId

            // Update the graph when the data changes
            viewModel.getUserDownlinkWeekReportGraphData(requireContext(), jsonObject.toString().toRequestBody(), id)!!.observe(requireActivity(), Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var graphWeekList = listOf<WeekGraph>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    graphWeekList = ParseResponseData.parseGraphLineWeekData(mJSONArray, ApiConstants.MONTH)
                    graphList = graphWeekList as ArrayList<WeekGraph>
                    UiUtils.log(TAG, "Final Res-> ${graphWeekList[0].weekGraphDataList?.size}")

                    periodList.clear()
                    for (i in graphWeekList.indices) {
                        periodList.add(Period(i,graphWeekList[i].week,false))
                    }

                    UiUtils.log(TAG,"Array-> "+ mJSONArray.getJSONObject(0).toString())
                    val totalCoins = mJSONArray.getJSONObject(0).getString("total_coins").toFloat().toInt()
                    val totalCustomer = mJSONArray.getJSONObject(0).getString("total_customer").toFloat().toInt()
                    setData (totalCoins,totalCustomer)

                    listItemClicked(periodList[selectedPosition])
                    //periodList[selectedPosition].isSelected = true
                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                    //setGraphData(graphList[selectedPosition].weekGraphDataList!!)
                    /*GraphUtil(requireContext()).setDataForLineChart(
                       binding.lineChart,
                       graphWeekList[0].weekGraphDataList as ArrayList<Graph>,
                       1,
                       true,
                       false,
                       ApiConstants.WEEK
                   )*/

                    //setDataForLineChart(binding.lineChart, graphList as ArrayList<Graph>, 1, true, false, MONTH)
                }
            })

        }

    }

    private fun setGraphData(weekGraphDataList: ArrayList<Graph>) {
        GraphUtil(requireContext()).setDataForLineChart(
            binding.lineChart,
            weekGraphDataList,
            1,
            true,
            false,
            ApiConstants.WEEK
        )
    }

    private fun setData(totalCoins: Int, totalMembers: Int){
        binding.txtTotalCoins.text = totalCoins.toString()
        binding.txtTotalMembers.text = totalMembers.toString()
    }

    companion object {

        private val TAG  = ReportHistoryWeekFragment::class.simpleName.toString()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportHistoryWeekFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}