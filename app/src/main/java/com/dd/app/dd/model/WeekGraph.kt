package com.dd.app.dd.model

import com.dd.app.dd.model.Graph

data class WeekGraph(
    var week: String = "",
    var weekGraphDataList: ArrayList<Graph>? = mutableListOf<Graph>() as ArrayList<Graph>
)