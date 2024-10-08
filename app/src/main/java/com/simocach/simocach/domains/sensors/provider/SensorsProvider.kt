package com.simocach.simocach.domains.sensors.provider

import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.SensorsConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SensorsProvider {

    companion object {
        private var sSensorsProvider: SensorsProvider? = null

        private val lock = Any()

        fun getInstance(): SensorsProvider {
            synchronized(lock) {
                if (sSensorsProvider == null) {
                    sSensorsProvider = SensorsProvider()
                }
                return sSensorsProvider!!
            }
        }

    }

    private var mSensors: List<ModelSensor> = mutableListOf()
    private val _mSensorsFlow = MutableSharedFlow<List<ModelSensor>>(replay = 0)

    val mSensorsFlow = _mSensorsFlow.asSharedFlow()

    var mDefaultScope = CoroutineScope(Job() + Dispatchers.Default)

    val SENSORS_PROVIDER = arrayOf(
        ModelSensor(
            Sensor.TYPE_TEMPERATURE,
            Sensor(
                Sensor.TYPE_TEMPERATURE,
                Sensor.NAME_TEMPERATURE,
                Sensor.VERSION_TEMPERATURE
            ),
            mutableMapOf(
                SensorsConstants.DETAIL_KEY_NAME to Sensor.NAME_TEMPERATURE,
                SensorsConstants.DETAIL_KEY_VERSION to Sensor.VERSION_TEMPERATURE,
                SensorsConstants.DETAIL_KEY_S_TYPE to Sensor.TYPE_TEMPERATURE
            ),
            Sensor.getSensorName(Sensor.TYPE_TEMPERATURE)
        ), ModelSensor(
            Sensor.TYPE_TURBIDITY,
            Sensor(Sensor.TYPE_TURBIDITY, Sensor.NAME_TURBIDITY, Sensor.VERSION_TURBIDITY),
            mutableMapOf(
                SensorsConstants.DETAIL_KEY_NAME to Sensor.NAME_TURBIDITY,
                SensorsConstants.DETAIL_KEY_VERSION to Sensor.VERSION_TURBIDITY,
                SensorsConstants.DETAIL_KEY_S_TYPE to Sensor.TYPE_TURBIDITY,
            )
        ), ModelSensor(
            Sensor.TYPE_TDS,
            Sensor(Sensor.TYPE_TDS, Sensor.NAME_TDS, Sensor.VERSION_TDS),
            mutableMapOf(
                SensorsConstants.DETAIL_KEY_NAME to Sensor.NAME_TDS,
                SensorsConstants.DETAIL_KEY_VERSION to Sensor.VERSION_TDS,
                SensorsConstants.DETAIL_KEY_S_TYPE to Sensor.TYPE_TDS
            ),
            Sensor.NAME_TDS,
        ), ModelSensor(
            Sensor.TYPE_PH,
            Sensor(Sensor.TYPE_PH, Sensor.NAME_PH, Sensor.VERSION_PH),
            mutableMapOf(
                SensorsConstants.DETAIL_KEY_NAME to Sensor.NAME_PH,
                SensorsConstants.DETAIL_KEY_VERSION to Sensor.VERSION_PH,
                SensorsConstants.DETAIL_KEY_S_TYPE to Sensor.TYPE_PH
            ),
            Sensor.NAME_PH
        )
    )
    //    Emitting data
    fun listenSensors(): SensorsProvider {
        if (mSensors.isEmpty() && SENSORS_PROVIDER.isNotEmpty()) {
            val sensorList = SENSORS_PROVIDER.filter {
                SensorsConstants.SENSORS.contains(it.type)

            }.distinctBy { it.type }.toList()

            mSensors = sensorList
        }

//            Here I should listen sensors and update sensorsFlow
        mDefaultScope.launch {
            _mSensorsFlow.emit(mSensors)
        }
        return this
    }

    fun listenSensor(sensorType: Int): Flow<ModelSensor?> {

        val flow = mSensorsFlow.map { sensors ->
            return@map sensors.singleOrNull { modelSensor ->
                modelSensor.type == sensorType
            }
        }



        return flow
    }

    fun getSensor(sensorType: Int): ModelSensor? {

        return mSensors.singleOrNull { modelSensor ->
            modelSensor.type == sensorType
        }
    }

    fun clearAll() {

    }

}