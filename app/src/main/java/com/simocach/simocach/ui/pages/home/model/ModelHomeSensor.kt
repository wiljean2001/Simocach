package com.simocach.simocach.ui.pages.home.model

import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.SensorsConstants

data class ModelHomeSensor(
    var type: Int = -1,
    var sensor: Sensor? = null,
    var info: Map<String, Any> = mutableMapOf(),
    var valueRms: Float? = 0.0f,
    var isActive:  Boolean =  false,
    var name: String = ""
) {

    init {
        name = SensorsConstants.MAP_TYPE_TO_NAME.get( type, Sensor.getSensorName(type))
    }
}