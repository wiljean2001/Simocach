package com.simocach.simocach.domains.sensors.data.api

import com.simocach.simocach.domains.sensors.data.SensorDataSerializerAPI
import com.simocach.simocach.domains.sensors.data.WaterQualityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WaterQualityService {
    @POST("api/predict/")
    suspend fun predictWaterQuality(
        @Body request: SensorDataSerializerAPI
    ): Response<WaterQualityResponse>
}
