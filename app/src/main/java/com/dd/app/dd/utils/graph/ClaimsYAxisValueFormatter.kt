package com.dd.app.dd.utils.graph

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt


class ClaimsYAxisValueFormatter : ValueFormatter() {


    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val roundoff = (value * 100.0).roundToInt() / 100.0
        return roundoff.toString() //+ "k"
    }
}