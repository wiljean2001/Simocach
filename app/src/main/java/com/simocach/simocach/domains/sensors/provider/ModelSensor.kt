package com.simocach.simocach.domains.sensors.provider

import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.SensorsConstants

class ModelSensor(
    var type: Int = -1,
    var sensor: Sensor? = null,
    var info: Map<String, Any> = mutableMapOf(),
    var name: String = ""

) {

    init {
        if (sensor != null) {
            name = SensorsConstants.MAP_TYPE_TO_NAME.get(type, sensor?.name ?: "")
            (info as MutableMap<String, Any>)[SensorsConstants.DETAIL_KEY_NAME] = sensor!!.name
            (info as MutableMap<String, Any>)[SensorsConstants.DETAIL_KEY_VERSION] =
                sensor!!.version
            (info as MutableMap<String, Any>)[SensorsConstants.DETAIL_KEY_S_TYPE] = sensor!!.type
        }
    }
}