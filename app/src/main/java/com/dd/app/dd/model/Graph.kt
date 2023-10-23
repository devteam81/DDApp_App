package com.dd.app.dd.model

import com.dd.app.dd.model.GraphData

class Graph() {

    var id: Int = 0
    var title: String = ""
    var titleNew: String = ""
    var description: String = ""
    var value: Int = 0
    var graphDataList: ArrayList<GraphData>? = mutableListOf<GraphData>() as ArrayList<GraphData>

}