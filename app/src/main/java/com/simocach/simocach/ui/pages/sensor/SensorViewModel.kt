package com.simocach.simocach.ui.pages.sensor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simocach.simocach.domains.chart.entity.ModelChartUiUpdate
import com.simocach.simocach.domains.chart.mpchart.MpChartDataManager
import com.simocach.simocach.domains.sensors.packets.ModelSensorPacket
import com.simocach.simocach.domains.sensors.packets.SensorPacketConfig
import com.simocach.simocach.domains.sensors.packets.SensorPacketsProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Arrays
import kotlin.math.roundToInt
import kotlin.math.sqrt

class SensorViewModel(var mSensorType: Int) : ViewModel() {
    private var mIsRunningPeriod: Boolean = false
    private var mLatestPacket: ModelSensorPacket? = null
    private var mLogTimestamp: Long = 0
    private var mSensorPacket: ModelSensorPacket? = null
    private var mChartDataManager: MpChartDataManager? = null
    private val _mSensorPacketFlow = MutableSharedFlow<ModelChartUiUpdate>(replay = 0)
    val mSensorPacketFlow = _mSensorPacketFlow.asSharedFlow()


    private val _mSensorModulus = MutableSharedFlow<Float>(replay = 0)
    val mSensorModulus = _mSensorModulus.asSharedFlow()

    init {
        getChartDataManager(mSensorType)
        initializeFlow()
    }


    fun getChartDataManager(type: Int): MpChartDataManager {

        if (mChartDataManager?.sensorType == type) {
            return mChartDataManager!!
        } else {
            mChartDataManager = MpChartDataManager(type, onDestroy = {
            })
        }

        return mChartDataManager!!
    }

    private fun initializeFlow() {

        val sensorPacketFlow =
            SensorPacketsProvider.getInstance().mSensorPacketFlow
        val sensorPacketFilteredFlow =
            sensorPacketFlow.filter { sensorPacket ->
                val filtered = sensorPacket.type == mSensorType
                // sensorPacket.sensorEvent?.values
//            Log.d("rememberChartUiUpdateEvent", "filtered: $filtered, ${mpChartViewManager.sensorType}")
                return@filter filtered
            }

        SensorPacketsProvider.getInstance().attachSensor(
            SensorPacketConfig(mSensorType, 2)
        )

        viewModelScope.launch {
            sensorPacketFilteredFlow.collect {
//                    Log.d("SensorViewModel", "init mSensorPacketFlow 2: ")
//                    Log.d("SensorViewModel", "addEntry: ${it.timestamp}")

                if (it.timestamp - mLogTimestamp < 50) {
                    Log.d(
                        "SensorViewModel",
                        "addEntry: ${it.timestamp} ${it.type} ${Arrays.toString(it.values)}"
                    )

                }

                mLogTimestamp = it.timestamp
                mChartDataManager?.addEntry(it)
            }
        }

        mChartDataManager?.runPeriodically()
        runPeriodicRms()

        viewModelScope.launch {

            mChartDataManager?.mSensorPacketFlow?.collect {

                if (it.packets.size > 0) {
                    mLatestPacket = it.packets.last()

                }

//                it.packets
//                Log.d("SensorViewModel", "init mSensorPacketFlow: ${it.timestamp} ${it.size}  ")
                _mSensorPacketFlow.emit(it)
            }
        }
    }

    private fun runPeriodicRms() {
        if (mIsRunningPeriod) {
            return
        }
        mIsRunningPeriod = true

        viewModelScope.launch {
            delay(100)
            runPeriodicTask()
        }

    }

    private fun runPeriodicTask() {

        viewModelScope.launch {
            while (viewModelScope.isActive) {

                val packet = mLatestPacket?.copy();

                packet?.values?.let {

                    var output = if (it.size > 1) {
                        sqrt((it.fold(0.0f) { acc, fl -> acc + fl * fl }).toDouble()).toFloat()
                    } else if (it.size == 1) {
                        it.first()
                    } else {
                        0.0f
                    }

                    output = (output * 100.0f).roundToInt().toFloat() / 100.00f

                    _mSensorModulus.emit(output)

                }


//                Log.d("MpChartViewManager ", "runPeriodicTask : $sensorType")
                /*                _mSensorPacketFlow.emit(
                                    ModelChartUiUpdate(
                //                    mModelLineChart
                                        sensorType,
                                        items.size, items
                                    )
                                )*/

                delay(400)
            }

        }

    }

}