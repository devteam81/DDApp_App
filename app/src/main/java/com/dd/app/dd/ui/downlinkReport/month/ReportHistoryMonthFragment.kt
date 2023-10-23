package com.dd.app.dd.ui.downlinkReport.month

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dd.app.R
import com.dd.app.databinding.FragmentReportHistoryMonthBinding
import com.dd.app.dd.model.Graph
import com.dd.app.dd.model.LoginUser
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

class ReportHistoryMonthFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var mContext: Context? = null

    private lateinit var binding: FragmentReportHistoryMonthBinding
    private lateinit var downlinkReportViewModel: DownlinkReportViewModel

    private var isViewShown = false

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()
        observeViewModel(downlinkReportViewModel)
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isViewShown = view != null && isVisibleToUser
    }*/

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
        //return inflater.inflate(R.layout.fragment_report_history_month, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_history_month, container, false)

        //mContext = context

        downlinkReportViewModel = ViewModelProvider(requireActivity())[DownlinkReportViewModel::class.java]
        observeViewModel(downlinkReportViewModel)

        return binding.root
    }

    private fun observeViewModel(viewModel: DownlinkReportViewModel) {

        val jsonObject = JSONObject()
        //jsonObject.put("id", prefUtils?.getStringValue("USER_ID",""))
        UiUtils.log("jsonObject :", jsonObject.toString())
        val id = LoginUser.ottId

        UiUtils.log(TAG, "role :"+ LoginUser.role)
        if(LoginUser.role.equals(ApiConstants.ADMIN,true)){

            // Update the graph when the data changes
            viewModel.getAdminDownlinkMonthReportGraphData(requireContext(), jsonObject.toString().toRequestBody(), id)!!.observe(requireActivity(), Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    graphList = ParseResponseData.parseGraphLineData(mJSONArray, ApiConstants.MONTH)

                    UiUtils.log(TAG,"Array-> "+ mJSONArray.getJSONObject(0).toString())
                    val totalCoins = mJSONArray.getJSONObject(0).getString("total_coins").toFloat().toInt()
                    val totalCustomer = mJSONArray.getJSONObject(0).getString("total_customer").toFloat().toInt()
                    setData (totalCoins,totalCustomer)

                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                    GraphUtil(requireContext()).setDataForLineChart(
                        binding.lineChart,
                        graphList as ArrayList<Graph>,
                        1,
                        true,
                        false,
                        ApiConstants.MONTH
                    )

                    //setDataForLineChart(binding.lineChart, graphList as ArrayList<Graph>, 1, true, false, MONTH)
                }
            })
        }else{

            // Update the graph when the data changes
            viewModel.getUserDownlinkMonthReportGraphData(requireContext(), jsonObject.toString().toRequestBody(), id)!!.observe(requireActivity(), Observer {

                UiUtils.log(TAG, "Response: " + it.data)
                val jsonInString = Gson().toJson(it.data)
                var graphList = listOf<Graph>()
                if(jsonInString.length>2) {
                    val mJSONArray = JSONArray(jsonInString)
                    graphList = ParseResponseData.parseGraphLineData(mJSONArray, ApiConstants.MONTH)

                    UiUtils.log(TAG,"Array-> "+ mJSONArray.getJSONObject(0).toString())
                    val totalCoins = mJSONArray.getJSONObject(0).getString("total_coins").toFloat().toInt()
                    val totalCustomer = mJSONArray.getJSONObject(0).getString("total_customer").toFloat().toInt()
                    setData (totalCoins,totalCustomer)

                    //graphUtil.setDataForBarChart(binding.barChart,graphData,1);
                    GraphUtil(requireContext()).setDataForLineChart(
                        binding.lineChart,
                        graphList as ArrayList<Graph>,
                        1,
                        true,
                        false,
                        ApiConstants.MONTH
                    )

                    //setDataForLineChart(binding.lineChart, graphList as ArrayList<Graph>, 1, true, false, MONTH)
                }
            })
        }

    }


    private fun setData(totalCoins: Int, totalMembers: Int){
        binding.txtTotalCoins.text = totalCoins.toString()
        binding.txtTotalMembers.text = totalMembers.toString()
    }



    companion object {

        private val TAG  = ReportHistoryMonthFragment::class.simpleName.toString()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportHistoryMonthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}