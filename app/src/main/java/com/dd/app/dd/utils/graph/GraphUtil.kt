package com.dd.app.dd.utils.graph

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import androidx.core.content.res.ResourcesCompat
import com.dd.app.R
import com.dd.app.dd.model.Graph
import com.dd.app.dd.network.ApiConstants.Companion.DAY
import com.dd.app.dd.network.ApiConstants.Companion.MONTH
import com.dd.app.dd.network.ApiConstants.Companion.WEEK
import com.dd.app.util.UiUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils


class GraphUtil(val context: Context) {

    private val TAG: String = GraphUtil::class.java.simpleName

    private val mMonths = arrayOf(
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    )

    private val mMonthsShort = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    private val mWeek = arrayOf(
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    )

    private val mWeekShort = arrayOf(
        "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"
    )

    val REGCOMPISD = intArrayOf(ResourcesCompat.getColor(context.resources, R.color.graph_color_1, null),
        ResourcesCompat.getColor(context.resources, R.color.graph_color_2, null),
        ResourcesCompat.getColor(context.resources, R.color.graph_color_3, null))
    val THREECOLORS = intArrayOf(ResourcesCompat.getColor(context.resources, R.color.teal_200, null))
    val FOURCOLORS = intArrayOf(ResourcesCompat.getColor(context.resources, R.color.text_color, null))

    private fun String.getShortName(type:Int): String{
        UiUtils.log(TAG, "type -> $type")
        if(type == DAY || type == WEEK){
            UiUtils.log(TAG,"ShortName -> "+mWeekShort[mWeek.indexOf(this)] )
            return mWeekShort[mWeek.indexOf(this)]
        }
        if(type == MONTH){
            UiUtils.log(TAG,"ShortName -> "+mMonthsShort[mMonths.indexOf(this)] )
            return mMonthsShort[mMonths.indexOf(this)]
        }
        return this
    }

    /********************** BAR CHART  */
    fun setDataForBarChart(chart_bar: BarChart, barData: ArrayList<Graph>, colorType: Int) {
        run {
            chart_bar.description.isEnabled = false
            chart_bar.setScaleEnabled(false)

            //chart.setDrawBorders(true);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            //chart_bar.setMaxVisibleValueCount(40);

            // scaling can now only be done on x- and y-axis separately
            chart_bar.setPinchZoom(false)
            chart_bar.setDrawBarShadow(false)
            chart_bar.setDrawGridBackground(false)
            chart_bar.setDrawValueAboveBar(false)
            chart_bar.isHighlightFullBarEnabled = false

            //chart_bar.animateY(5000);

            // create a custom MarkerView (extend MarkerView) and specify the layout
            // to use for it
            val mv = MyMarkerView(context, R.layout.custom_marker_view)
            mv.chartView = chart_bar // For bounds control
            chart_bar.marker = mv // Set the marker to the chart
        }
        var xAxis: XAxis
        run {
            xAxis = chart_bar.xAxis
            //xAxis.setTypeface(tfLight);
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.setCenterAxisLabels(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            xAxis.labelCount = 6
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = 6f
            val xAxisLabel = ArrayList<String>()
            /*xAxisLabel.add("JAN");
            xAxisLabel.add("FEB");
            xAxisLabel.add("MAR");
            xAxisLabel.add("APR");
            xAxisLabel.add("MAY");
            xAxisLabel.add("JUN");*/
            //log(TAG, "Data graph -> ${barData[0].graphDataList!![0].toString()}")
            for (j in 0 until barData[0].graphDataList!!.size) {
                //Utils.log(TAG, "Data:= " + barData[0].getGraphDataList().get(j).getTitle())
                xAxisLabel.add(barData[0].graphDataList!![j].title)
                //log(TAG, "xAxis:= ${barData[0].graphDataList!![j].title}")
            }
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    UiUtils.log(TAG, "Value:= $value")
                    if (value < 0 || value > (barData[0].graphDataList!!.size-1)) return "NA"
                    UiUtils.log(TAG, "Value Data:= " + xAxisLabel[value.toInt()])
                    return xAxisLabel[value.toInt()]
                }
            }
        }
        var leftAxis: YAxis
        run {
            leftAxis = chart_bar.axisLeft
            //leftAxis.setTypeface(tfLight);
            leftAxis.valueFormatter = LargeValueFormatter()
            leftAxis.setDrawGridLines(false)
            leftAxis.spaceTop = 35f
            leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
            leftAxis.textColor = Color.WHITE
        }
        chart_bar.axisRight.isEnabled = false
        var l: Legend
        run {
            l = chart_bar.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.textColor = Color.WHITE
            l.setDrawInside(false)
            //l.setTypeface(tfLight);
            l.textSize = 14f
            l.formSize = 10f
            l.formToTextSpace = 8f
            l.yOffset = 0f
            l.xOffset = 10f
            l.xEntrySpace = 20f
            l.yEntrySpace = 80f
            l.form = Legend.LegendForm.CIRCLE
        }
        chart_bar.setExtraOffsets(0f, 0f, 0f, 15f)
        setBarData(chart_bar, barData, colorType)

        /*BarData data = setBarData(chart_bar,barData, colorType);
        chart_bar.setData(data);
        chart_bar.invalidate();*/
    }


    private fun setBarData(barChart: BarChart, range: ArrayList<Graph>, colorType: Int) {
        val groupSpace = 0.51f
        val barSpace = 0.04f // x4 DataSet
        val barWidth = 0.2f // x4 DataSet
        val groupCount = 6
        val dataSets = ArrayList<IBarDataSet>()
        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        for (i in 0 until range[0].graphDataList!!.size) {
            val val1: Float = (range[0].graphDataList!![i].value).toFloat()
            values1.add(BarEntry(i.toFloat(), val1))
            val val2: Float = (range[0].graphDataList!![i].valueSecond).toFloat()
            values2.add(BarEntry(i.toFloat(), val2))
        }

        /*float randomMultiplier = 6 * 100000f;

        for (int k = 0; k < 6; k++) {
            values1.add(new BarEntry(k, (float) (Math.random() * randomMultiplier)));
            values2.add(new BarEntry(k, (float) (Math.random() * randomMultiplier)));
        }*/
        val set1: BarDataSet
        val set2: BarDataSet
        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            set1 = barChart.data.getDataSetByIndex(0) as BarDataSet
            set2 = barChart.data.getDataSetByIndex(1) as BarDataSet
            set1.values = values1
            set2.values = values2
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            // create 4 DataSets
            set1 = BarDataSet(values1, range[0].title)
            set2 = BarDataSet(values2, range[0].titleNew)
            set1.setDrawIcons(false)
            set1.setDrawValues(false)
            set2.setDrawIcons(false)
            set2.setDrawValues(false)

            // draw dashed line
            //set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.color = REGCOMPISD[1]
            set2.color = REGCOMPISD[2]
            dataSets.add(set1)
            dataSets.add(set2)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.barWidth = 0.2f
            data.setValueFormatter(LargeValueFormatter())
            //data.setValueTypeface(tfLight);
            barChart.data = data
        }
        barChart.setFitBars(true) // make the x-axis fit exactly all bars
        //barChart.animateXY(3000,3000);


        // specify the width each bar should have
        barChart.barData.barWidth = barWidth

        // restrict the x-axis range
        //barChart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        //barChart.getXAxis().setAxisMaximum(startYear + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);


        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        //barChart.getXAxis().setAxisMaximum(startYear + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0.05f, groupSpace, barSpace)
        barChart.setFitBars(true)
        //barChart.animateXY(3000,3000);
        barChart.invalidate() // refresh
    }

    /********************** LINE CHART  */
    fun setDataForLineChart(
        chart_line: LineChart,
        lineData: ArrayList<Graph>,
        colorType: Int,
        fillColor: Boolean,
        drawCircle: Boolean,
        type: Int
    ) {
        run {

            // background color
            chart_line.setBackgroundColor(Color.TRANSPARENT)

            // disable description text
            //chart_line.setDescription(null);
            chart_line.description.isEnabled = false

            // enable touch gestures
            chart_line.setTouchEnabled(true)
            chart_line.setDrawGridBackground(false)

            // create marker to display box when values are selected
            val mv = MyMarkerView(context, R.layout.custom_marker_view)

            // Set the marker to the chart
            mv.chartView = chart_line
            chart_line.marker = mv

            // enable scaling and dragging
            chart_line.isDragEnabled = true
            chart_line.setScaleEnabled(false)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart_line.setPinchZoom(true)
            chart_line.axisLeft.setDrawGridLines(false)
            chart_line.xAxis.setDrawGridLines(false)
            chart_line.setExtraOffsets(5f, 5f, 5f, 15f)

            // set listeners
            chart_line.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(
                    e: Entry,
                    h: Highlight
                ) {
                }

                override fun onNothingSelected() {}
            })
        }

        //ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart_line);
        var xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart_line.xAxis

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            /*if(type==3)
        xAxis.setLabelCount(10);*/

            //Space
            xAxis.spaceMin = 0.3f

            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            if(type == MONTH)
                xAxis.labelCount = 6
            else
                xAxis.labelCount = 7

            // axis range
            //xAxis.setAxisMaximum(200f);
            //xAxis.setAxisMinimum(0f);
            /*if (type == MONTH)
                xAxis.valueFormatter = DayAxisValueFormatter(chart_line)
            else {*/
            val xAxisLabel = ArrayList<String>()
            for (j in 0 until lineData[0].graphDataList!!.size) {
                //log(TAG, "label:= " + lineData[0].graphDataList!![j].title)
                xAxisLabel.add(lineData[0].graphDataList!![j].title.getShortName(type))
            }
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if (value < 0 || value > (lineData[0].graphDataList!!.size - 1)) return "NA"
                    return xAxisLabel[value.toInt()]
                }
            }
            //}
        }
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = chart_line.axisLeft

            // disable dual axis (only use LEFT axis)
            chart_line.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            var maxValue = 0f;
            // axis range
            for (i in lineData.indices) {
                for (j in 0 until lineData[i].graphDataList!!.size) {
                    val value: Float = lineData[i].graphDataList!![j].value.toFloat()
                    maxValue = maxValue.coerceAtLeast(value)
                    //values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
                }
            }

            UiUtils.log(TAG, "maxValue->$maxValue")
            //yAxis.axisMaximum = maxValue
            yAxis.axisMinimum = 0f

            /*if(maxValue>6)
                yAxis.labelCount = 7
            else
                yAxis.labelCount = maxValue.toInt() + 1*/

            //Space
            //yAxis.setSpaceMin(50f);

            yAxis.valueFormatter = ClaimsYAxisValueFormatter()

            //color
            yAxis.textColor = Color.WHITE
        }
        run {
            // // Create Limit Lines // //
            val llXAxis = LimitLine(9f, "Index 10")
            llXAxis.lineWidth = 4f
            llXAxis.enableDashedLine(10f, 10f, 0f)
            llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            llXAxis.textSize = 10f
            llXAxis.textColor = Color.WHITE
            //llXAxis.setTypeface(tfRegular);
            val ll1 = LimitLine(150f, "Upper Limit")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 10f
            llXAxis.textColor = Color.WHITE
            //ll1.setTypeface(tfRegular);
            val ll2 = LimitLine(-30f, "Lower Limit")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 10f
            llXAxis.textColor = Color.WHITE
            //ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(false)
            xAxis.setDrawLimitLinesBehindData(false)
        }
        chart_line.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                //Utils.log(TAG, "Selected: $e")
                //Utils.log(TAG, "Value: " + e.y)
            }

            override fun onNothingSelected() {}
        })
        var l: Legend
        run {
            l = chart_line.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.textColor = Color.WHITE
            l.setDrawInside(false)
            l.textSize = 10f
            l.formSize = 10f
            l.formToTextSpace = 5f
            l.xEntrySpace = 20f
            l.yEntrySpace = 80f
            l.form = Legend.LegendForm.CIRCLE
        }
        chart_line.setExtraOffsets(0f, 0f, 0f, 15f)
        setLineData(chart_line, lineData, fillColor, drawCircle)

        /*LineData data = setLineData(chart_line,lineData, colorType);
        chart_line.setData(data);
        chart_line.invalidate();*/
    }

    private fun setLineData(
        lineChart: LineChart,
        range: ArrayList<Graph>,
        fillColor: Boolean,
        drawCircle: Boolean
    ) {
        val dataSets = ArrayList<ILineDataSet>()
        for (i in range.indices) {
            val values = ArrayList<Entry>()
            for (j in 0 until range[i].graphDataList!!.size) {
                val value: Float = range[i].graphDataList!![j].value.toFloat()
                values.add(Entry(j.toFloat(), value))
                //values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
            }
            //Utils.log(TAG, "X=$i --> $values")
            var set1: LineDataSet
            if (lineChart.data != null &&
                lineChart.data.dataSetCount > 0
            ) {
                set1 = lineChart.data.getDataSetByIndex(i) as LineDataSet
                set1.values = values
                set1.notifyDataSetChanged()
                lineChart.data.notifyDataChanged()
                lineChart.notifyDataSetChanged()
                //Utils.log(TAG, "Already added")
            } else {
                //Utils.log(TAG, "New created")
                // create a dataset and give it a type
                set1 = LineDataSet(values, range[i].title)
            }
            set1.setDrawIcons(false)
            set1.setDrawValues(false)

            // draw dashed line
            //set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.color = REGCOMPISD[i]
            set1.setCircleColor(Color.WHITE)
            set1.setDrawCircles(drawCircle)

            // line thickness and point size
            set1.lineWidth = 4f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            //set1.enableDashedHighlightLine(10f, 5f, 0f);
            //set1.setHighlightEnabled(false);

            //Cubic Shape
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER

            // set the filled area
            set1.setDrawFilled(fillColor)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                //Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
                //set1.setFillAlpha(155);
                set1.fillColor = REGCOMPISD[i]
            } else {
                set1.fillColor = Color.WHITE
            }
            dataSets.add(set1) // add the data sets
        }
        // create a data object with the data sets
        val data = LineData(dataSets)

        // set data
        lineChart.data = data
        lineChart.invalidate()
    }

    fun setDataForLineChartDay(
        chart_line: LineChart,
        lineData: ArrayList<Graph>,
        colorType: Int,
        fillColor: Boolean,
        drawCircle: Boolean
    ) {
        run {

            // background color
            chart_line.setBackgroundColor(Color.BLACK)

            // disable description text
            //chart_line.setDescription(null);
            chart_line.description.isEnabled = false

            // enable touch gestures
            chart_line.setTouchEnabled(true)
            chart_line.setDrawGridBackground(false)

            // create marker to display box when values are selected
            val mv = MyMarkerView(context, R.layout.custom_marker_view)

            // Set the marker to the chart
            mv.setChartView(chart_line)
            chart_line.marker = mv

            // enable scaling and dragging
            chart_line.isDragEnabled = true
            chart_line.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart_line.setPinchZoom(true)
            chart_line.axisLeft.setDrawGridLines(false)
            chart_line.xAxis.setDrawGridLines(false)

            // set listeners
            chart_line.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(
                    e: Entry,
                    h: Highlight
                ) {
                }

                override fun onNothingSelected() {}
            })
        }

        //ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart_line);
        var xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart_line.xAxis

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            //xAxis.setLabelCount(7);

            //Space
            xAxis.spaceMin = 0.3f

            // axis range
            //xAxis.setAxisMaximum(200f);
            //xAxis.setAxisMinimum(0f);
            val xAxisLabel = arrayOf(
                "12",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12"
            )
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    //Utils.log(TAG,"Value:= "+xAxisLabel.get((int) value));
                    return xAxisLabel[value.toInt()]
                }
            }
        }
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = chart_line.axisLeft

            // disable dual axis (only use LEFT axis)
            chart_line.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            //yAxis.setAxisMaximum(200f);
            yAxis.axisMinimum = 0f

            //Space
            //yAxis.setSpaceMin(50f);

            //color
            yAxis.textColor = Color.WHITE
        }
        run {
            // // Create Limit Lines // //
            val llXAxis = LimitLine(9f, "Index 10")
            llXAxis.lineWidth = 4f
            llXAxis.enableDashedLine(10f, 10f, 0f)
            llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            llXAxis.textSize = 10f
            llXAxis.textColor = Color.WHITE
            //llXAxis.setTypeface(tfRegular);
            val ll1 = LimitLine(150f, "Upper Limit")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 10f
            llXAxis.textColor = Color.WHITE
            //ll1.setTypeface(tfRegular);
            val ll2 = LimitLine(-30f, "Lower Limit")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 10f
            llXAxis.textColor = Color.WHITE
            //ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)
        }
        chart_line.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                //Utils.log(TAG, "Selected: $e")
                //Utils.log(TAG, "Value: " + e.y)
            }

            override fun onNothingSelected() {}
        })
        var l: Legend
        run {
            l = chart_line.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.textColor = Color.WHITE
            l.setDrawInside(false)
            l.textSize = 14f
            l.formSize = 10f
            l.formToTextSpace = 8f
            l.xEntrySpace = 20f
            l.yEntrySpace = 80f
            l.form = Legend.LegendForm.CIRCLE
        }
        setLineData(chart_line, lineData, fillColor, drawCircle)

        /*LineData data = setLineData(chart_line,lineData, colorType);
        chart_line.setData(data);
        chart_line.invalidate();*/
    }


}