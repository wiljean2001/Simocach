package com.simocach.simocach.domains.chart.entity

import com.simocach.simocach.domains.sensors.packets.ModelSensorPacket

data class ModelChartUiUpdate(
    var sensorType: Int,
    var size: Int,
    var packets: List<ModelSensorPacket> = listOf(),
    var timestamp: Long = System.currentTimeMillis()
)