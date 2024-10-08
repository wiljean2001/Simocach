package com.simocach.simocach.domains.sensors.data

import com.google.gson.annotations.SerializedName

data class SensorData(
    val TEMP: Float, val TURB: Float, val TDS: Float, val PH: Float
)

data class SensorDataSerializerAPI(
    @SerializedName("TURB") val turbidity: Float,
    @SerializedName("TDS") val tds: Float,
    @SerializedName("PH") val ph: Float
)

data class WaterQualityResponse(
    val PH: Float,
    val Turbidez: Float,
    val TDS: Float,
    @SerializedName("Predicción de Calidad") val prediction: String
)