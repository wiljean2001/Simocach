package com.simocach.simocach.domains.sensors.packets

import com.simocach.simocach.domains.sensors.SensorsConstants

data class SensorPacketConfig(
    var sensorType: Int,
    var sensorDelay: Int = SensorsConstants.SENSOR_DELAY_UI
) {
}