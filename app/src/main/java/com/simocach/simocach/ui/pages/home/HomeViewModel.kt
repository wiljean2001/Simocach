package com.simocach.simocach.ui.pages.home

import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simocach.simocach.domains.chart.mpchart.MpChartDataManager
import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.data.WaterQualityResponse
import com.simocach.simocach.domains.sensors.packets.SensorPacketConfig
import com.simocach.simocach.domains.sensors.packets.SensorPacketsProvider
import com.simocach.simocach.domains.sensors.provider.SensorsProvider
import com.simocach.simocach.ui.pages.home.model.ModelHomeSensor
import com.simocach.simocach.ui.pages.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var mSensors: MutableList<ModelHomeSensor> = mutableListOf()

    // Game UI state
    private val _uiState = MutableStateFlow(HomeUiState())

    // Backing property to avoid state updates from other classes
    val mUiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    private val _mSensorsList = mutableStateListOf<ModelHomeSensor>()
    val mSensorsList: SnapshotStateList<ModelHomeSensor> = _mSensorsList

    private val _mActiveSensorListFlow = MutableStateFlow<MutableList<ModelHomeSensor>>(
        mutableListOf()
    )
    val mActiveSensorListFlow: StateFlow<MutableList<ModelHomeSensor>> = _mActiveSensorListFlow

    private val _mActiveSensorList = mutableListOf<ModelHomeSensor>()

    private val mIsActiveMap = mutableMapOf(
        Pair(Sensor.TYPE_TEMPERATURE, true),
        Pair(Sensor.TYPE_TDS, true),
        Pair(Sensor.TYPE_TURBIDITY, true),
        Pair(Sensor.TYPE_PH, true)
    )

    private val mChartDataManagerMap = mutableMapOf<Int, MpChartDataManager>()

    init {

        viewModelScope.launch {
            SensorsProvider.getInstance().mSensorsFlow.map { value ->
                value.map {
                    ModelHomeSensor(
                        it.type, it.sensor, it.info, 0f, mIsActiveMap.getOrDefault(it.type, false)
                    )
                }.toMutableList()
            }.collectLatest {
                mSensors = it
//                Log.d(
//                    "HomeViewModel",
//                    "${this@HomeViewModel} init sensors active  1: $mIsActiveMap"
//                )

//                Log.d("HomeViewModel", "sensors 2: $it")
                if (_mSensorsList.size == 0) {
                    _mSensorsList.addAll(mSensors)
                    var activeSensors = it.filter { modelHomeSensor -> modelHomeSensor.isActive }
//                     _mActiveSensorStateList.addAll(activeSensors)
                    _mActiveSensorList.addAll(activeSensors)
                    _mActiveSensorListFlow.emit(_mActiveSensorList)
                    getInitialChartData()
                    initializeFlow()
                }

            }

        }
        SensorsProvider.getInstance().listenSensors()

    }

    private fun getInitialChartData() {
        for (sensor in _mActiveSensorList) {
//            Log.d("HomeViewModel", "creating sensor ")
            getChartDataManager(sensor.type)
        }

    }


    private fun initializeFlow() {

        val sensorPacketFlow = SensorPacketsProvider.getInstance().mSensorPacketFlow


        for (sensor in _mActiveSensorList) {
            Log.d("HomeViewModel", "creating sensor attach packet")
            attachPacketListener(sensor)
        }

        viewModelScope.launch {
            sensorPacketFlow.collect {
                Log.e("HomeViewModel", "sensorPacketFlow -> type: ${it.type}")
                mChartDataManagerMap[it.type]?.addEntry(it)
            }
        }
//        Size of sensorPacketFlow
        Log.e("HomeViewModel", "sensorPacketFlow")
//
//
        mChartDataManagerMap.forEach { (_, mpChartDataManager) ->
            viewModelScope.launch {
                mpChartDataManager.runPeriodically()
            }
        }
    }


    private fun attachPacketListener(sensor: ModelHomeSensor) {

        Log.d("HomeViewModel", "attachPacketListener: $sensor")
        SensorPacketsProvider.getInstance().attachSensor(
            SensorPacketConfig(sensor.type, SensorManager.SENSOR_DELAY_NORMAL)
        )
    }

    private fun detachPacketListener(sensor: ModelHomeSensor) {
        Log.d("HomeViewModel", "detachSensor sensor: ${sensor.type} ${sensor.name}")
        SensorPacketsProvider.getInstance().detachSensor(
            sensor.type
        )
    }

    fun onSensorChecked(type: Int, isChecked: Boolean) {
        var isCheckedPrev = mIsActiveMap.getOrDefault(type, false)

        if (isCheckedPrev != isChecked) {
            mIsActiveMap[type] = isChecked
        }

        var index = mSensors.indexOfFirst { it.type == type }
        if (index >= 0) {
//            Log.d("HomeViewModel", "onSensorChecked: Index: $index $isChecked")
            var sensor = mSensors[index]
            var updatedSensor =
                ModelHomeSensor(sensor.type, sensor.sensor, sensor.info, sensor.valueRms, isChecked)
            mSensors[index] = updatedSensor

            mSensorsList[index] = updatedSensor
            updateActiveSensor(updatedSensor, isChecked)

        }/*viewModelScope.launch {
            emitUiState()

        }*/

    }

    private fun updateActiveSensor(sensor: ModelHomeSensor, isChecked: Boolean = false) {
        val index = _mActiveSensorList.indexOfFirst { it.type == sensor.type }

        if (!isChecked && index >= 0) {
            var manager = mChartDataManagerMap.remove(sensor.type)
            manager?.destroy()
//            _mActiveSensorStateList.removeAt(index)
            detachPacketListener(sensor)


            _mActiveSensorList.removeAt(index)
            viewModelScope.launch {

                _mActiveSensorListFlow.emit(_mActiveSensorList)
            }

        } else if (isChecked && index < 0) {
//            _mActiveSensorStateList.add(sensor)

            _mActiveSensorList.add(sensor)
            attachPacketListener(sensor)
            viewModelScope.launch {

                _mActiveSensorListFlow.emit(_mActiveSensorList)
            }
            getChartDataManager(type = sensor.type).runPeriodically()
        }
    }


    fun getChartDataManager(type: Int): MpChartDataManager {
        val chartDataManager = mChartDataManagerMap.getOrPut(type, defaultValue = {
            MpChartDataManager(type, onDestroy = {})
        })
        Log.d("HomeViewModel", "getChartDataManager: $type")
        return chartDataManager
    }

    //
//
//
    fun setActivePage(page: Int?) {

        viewModelScope.launch {
//            Log.d("HomeViewModel", "page: $page")
            if (page != null && _mActiveSensorList.size > 0) {
                if (_mActiveSensorList.size > page) {
                    var sensor = _mActiveSensorList[page]
//                _mUiCurrentSensorState.emit(sensor)
                    _uiState.emit(
                        _uiState.value.copy(
                            currentSensor = sensor, activeSensorCounts = _mActiveSensorList.size
                        )
                    )
                } else {
                    var sensor = _mActiveSensorList[_mActiveSensorList.size - 1]
                    _uiState.emit(
                        _uiState.value.copy(
                            currentSensor = sensor, activeSensorCounts = _mActiveSensorList.size
                        )
                    )
                }

            } else {
//                _mUiCurrentSensorState.emit(null)
                _uiState.emit(
                    _uiState.value.copy(
                        currentSensor = null, activeSensorCounts = _mActiveSensorList.size
                    )
                )

            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        mChartDataManagerMap.forEach { (_, mpChartDataManager) -> mpChartDataManager.destroy() }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel() as T
        }
    }


}