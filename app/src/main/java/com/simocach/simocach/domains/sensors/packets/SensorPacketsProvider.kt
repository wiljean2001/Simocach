package com.simocach.simocach.domains.sensors.packets

import android.util.Log
import android.util.SparseArray
import androidx.core.util.valueIterator
import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.data.WaterQualityResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SensorPacketsProvider {


    companion object {
        private var sSensorPacketsProvider: SensorPacketsProvider? = null
        private val lock = Any()

        fun getInstance(): SensorPacketsProvider {
            synchronized(lock) {
                if (sSensorPacketsProvider == null) {
                    sSensorPacketsProvider = SensorPacketsProvider(

                    )
                }
                return sSensorPacketsProvider!!
            }
        }
    }

    private var mDefaultScope = CoroutineScope(Job() + Dispatchers.Default)
    private var mSensorConfigs = SparseArray<SensorPacketConfig>()
    private val _mSensorPacketFlow = MutableSharedFlow<ModelSensorPacket>(replay = 0)
    val mSensorPacketFlow = _mSensorPacketFlow.asSharedFlow()

    private val _apiResponse = MutableStateFlow<WaterQualityResponse?>(null)
    val apiResponse: StateFlow<WaterQualityResponse?> = _apiResponse.asStateFlow()

    fun attachSensor(config: SensorPacketConfig): SensorPacketsProvider {
        val prevConfig = mSensorConfigs[config.sensorType]
        var shouldRegister = true
        if (prevConfig != null) {
//            Log.d("SensorPacketsProvider", "attachSensor prevConfig")
            if (prevConfig.sensorType != config.sensorType) {
                unregisterSensor(config)
            } else {
                shouldRegister = false
            }
        }
        if (shouldRegister) {
//            Log.d("SensorPacketsProvider", "attachSensor shouldRegister")
            mSensorConfigs[config.sensorType] = config
            registerSensor(config)
        }
//        Log.d("SensorPacketsProvider", "mSensorConfigs: ${mSensorConfigs.size()}")

        return this
    }

    private fun unregisterSensor(config: SensorPacketConfig) {
//        if (isSensorConnected == false) return
//        Log.e("SensorPacketsProvider", "unregisterSensor(config: SensorPacketConfig)")

        // Implementa la lógica para desconectar el sensor según tu necesidad
    }

    private fun registerSensor(config: SensorPacketConfig) {
//        if (isSensorConnected == false) return
        // Implementa la lógica para conectar el sensor según tu necesidad
//        Log.e("SensorPacketsProvider", "registerSensor(config: SensorPacketConfig)")
    }

    fun detachSensor(sensorType: Int): SensorPacketsProvider {
        Log.d(
            "SensorPacketsProvider",
            "detachSensor ${mSensorConfigs.size()}"
        )
        val sensorConfig = mSensorConfigs.get(sensorType)
        if (sensorConfig != null) {
            mSensorConfigs.remove(sensorType)
        }
        return this
    }

//    fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//        //TODO("Not yet implemented")
//    }

    // This function is called when the sensor values are changed
    fun onSensorChanged(sensorType: Int, values: FloatArray) {
        Log.d(
            "SensorPacketsProvider",
            "onSensorChanged ${mSensorConfigs.size()}"
        )
        mDefaultScope.launch {
            onSensorEvent(sensorType, values)
        }
    }

    fun onSensorChangedForPredictions(sensorData: WaterQualityResponse) {
        mDefaultScope.launch {
            _apiResponse.emit(sensorData)
        }
    }

    private suspend fun onSensorEvent(sensorType: Int, values: FloatArray) {
        synchronized(this) {
            val sensorConfig = sensorType.let { mSensorConfigs.get(it) }

            if (sensorConfig != null) {
                val sensorPacket = ModelSensorPacket(
                    values, sensorType, sensorConfig.sensorDelay, System.currentTimeMillis()
                )
                mDefaultScope.launch {
                    _mSensorPacketFlow.emit(
                        sensorPacket
                    )
                }
            }
        }
    }


    fun clearAll() {

//        Log.d("SensorPacketsProvider","clearAll")
        if (mSensorConfigs.size() > 0) {
            for (sensorConfig in mSensorConfigs.valueIterator()) {
//                Log.d("SensorPacketsProvider","clearAll unregisterSensor")
            }
        }
        mSensorConfigs.clear()

        Log.d("SensorPacketsProvider", "clearAll done ${mSensorConfigs.size()}")
    }
}
