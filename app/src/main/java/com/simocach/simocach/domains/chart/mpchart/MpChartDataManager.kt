package com.simocach.simocach.domains.chart.mpchart

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import com.simocach.simocach.domains.chart.ChartDataHandler
import com.simocach.simocach.domains.chart.entity.ModelChartUiUpdate
import com.simocach.simocach.domains.chart.entity.ModelLineChart
import com.simocach.simocach.domains.sensors.SensorsConstants
import com.simocach.simocach.domains.sensors.packets.ModelSensorPacket
import com.simocach.simocach.ui.resource.values.JlResColors
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharedFlow

class MpChartDataManager(
    var sensorType: Int,
    private var mSensorDelayType: Int = SensorsConstants.SENSOR_DELAY_NORMAL,
    var onDestroy: (type: Int) -> Unit = {}
) {

    private var mIsRunningPeriod: Boolean = false
    private var mDataComputationScope = CoroutineScope(Job() + Dispatchers.Default)

    private var mChartDataHandler: ChartDataHandler = ChartDataHandler(sensorType)

    val mSensorPacketFlow: SharedFlow<ModelChartUiUpdate>

    init {

        val axisCount = SensorsConstants.MAP_TYPE_TO_AXIS_COUNT.get(sensorType)

        if (axisCount == 1) {
            mChartDataHandler.addDataSet(
                SensorsConstants.DATA_AXIS_VALUE,
                JlResColors.NotePink.toArgb(),
                SensorsConstants.DATA_AXIS_VALUE_STRING, emptyArray(), false
            )

        } else if (axisCount == 3) {
            mChartDataHandler.addDataSet(
                SensorsConstants.DATA_AXIS_X,
                JlResColors.NotePink.toArgb(),
                SensorsConstants.DATA_AXIS_X_STRING, emptyArray(), false
            )
            mChartDataHandler.addDataSet(
                SensorsConstants.DATA_AXIS_Y,
                JlResColors.SensifyGreen40.toArgb(),
                SensorsConstants.DATA_AXIS_Y_STRING, emptyArray(), false
            )
            mChartDataHandler.addDataSet(
                SensorsConstants.DATA_AXIS_Z,
                JlResColors.CHART_3.toArgb(),
                SensorsConstants.DATA_AXIS_Z_STRING, emptyArray(), false
            )
        }

        mSensorPacketFlow = mChartDataHandler.mSensorPacketFlow

    }

    fun destroy() {
        mChartDataHandler.destroy()
        onDestroy.invoke(sensorType)
    }

    fun setSensorDelayType(type: Int) {
        mSensorDelayType = type
    }

    fun runPeriodically() {
        if (mIsRunningPeriod) {
            return
        }
        mIsRunningPeriod = true

        mDataComputationScope.launch {
            delay(100)
            Log.d("MpChartViewManager ", "createChart periodic Task: $sensorType")
            mChartDataHandler.runPeriodicTask()
        }

    }


    fun addEntry(sensorPacket: ModelSensorPacket) {
        mChartDataHandler.addEntry(sensorPacket)
    }

    fun getModel(): ModelLineChart {
        return mChartDataHandler.mModelLineChart
    }
}