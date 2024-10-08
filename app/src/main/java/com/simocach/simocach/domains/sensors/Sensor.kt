package com.simocach.simocach.domains.sensors

class Sensor(
    val type: Int,
    val name: String,
    val version: String //val info: Map<String, Any>? = emptyMap()
) {
    companion object {
        //        Keys of sensors
        const val TYPE_ALL = -1
        const val TYPE_TEMPERATURE = 1
        const val TYPE_TURBIDITY = 2
        const val TYPE_TDS = 3
        const val TYPE_PH = 4

        //        Names of sensors
        const val NAME_TEMPERATURE = "Temperatura"
        const val NAME_TURBIDITY = "Turbidez"
        const val NAME_TDS = "TDS"
        const val NAME_PH = "PH"

        //        Versions of sensors
        const val VERSION_TEMPERATURE = "1.0"
        const val VERSION_TURBIDITY = "1.0"
        const val VERSION_TDS = "1.0"
        const val VERSION_PH = "1.0"

        // Get Sensor names
        fun getSensorName(type: Int): String {
//            switch `type` then
            return when (type) {
                TYPE_TEMPERATURE -> NAME_TEMPERATURE
                TYPE_TURBIDITY -> NAME_TURBIDITY
                TYPE_TDS -> NAME_TDS
                TYPE_PH -> NAME_PH
                else -> {
                    "Desconocido"
                }
            }

        }
    }
}