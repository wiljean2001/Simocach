package com.simocach.simocach.domains.chart.mpchart.axis

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.simocach.simocach.domains.sensors.SensorsConstants

class MpChartTimestampAxisFormatter(private var sensorDelay: Int = SensorsConstants.SENSOR_DELAY_NORMAL) : ValueFormatter() {

    fun setDelay(delay: Int){
        sensorDelay = delay;
    }
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {

        /* val LENGTH_SAMPLE: Int = mModelLineChart.getSampleLength()
         val labels = arrayOfNulls<String>(LENGTH_SAMPLE)
         val timePreiod: Float = mModelLineChart.getTimePeriod()
         for (i in labels.indices) {
             val `val` = i.toFloat() * timePreiod
             labels[i] = java.lang.Float.toString(`val`) + "s"
         }*/
        val delay  = SensorsConstants.MAP_DELAY_TYPE_TO_DELAY.get(sensorDelay)
        val totalDelay = value*delay;
        return  "${totalDelay/1000}s"
//        return super.getAxisLabel(value, axis)
    }

}