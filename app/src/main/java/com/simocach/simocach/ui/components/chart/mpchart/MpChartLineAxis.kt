package com.simocach.simocach.ui.components.chart.mpchart

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.simocach.simocach.ui.components.chart.mpchart.base.MpChartLineView

class MpChartLineAxis(key: Int) : MpChartLineView(key) {

    override fun create(context: Context, colorSurface: Color, colorOnSurface: Color): LineChart {
        val lineChart  = super.create(context, colorSurface, colorOnSurface)

        lineChart.apply {
            axisLeft.setDrawLabels(true)
//           axisRight.setDrawLabels(false)
            axisLeft.setDrawGridLines(true)
        }

        return lineChart
    }




}