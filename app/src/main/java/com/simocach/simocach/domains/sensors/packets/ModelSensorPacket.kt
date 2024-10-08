package com.simocach.simocach.domains.sensors.packets

data class ModelSensorPacket(
    var values: FloatArray?,
    var type: Int,
    var delay: Int,
    var timestamp: Long
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModelSensorPacket

//        if (sensorEvent != other.sensorEvent) return false
        if (values != null) {
            if (other.values == null) return false
            if (!values.contentEquals(other.values)) return false
        } else if (other.values != null) return false
        if (type != other.type) return false
        if (delay != other.delay) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = values?.contentHashCode() ?: 0
        result = 31 * result + type
        result = 31 * result + delay
        result = 31 * result + timestamp.hashCode()
        return result
    }

}