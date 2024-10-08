package com.simocach.simocach.domains.sensors.data.websocket

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.SensorsConstants
import com.simocach.simocach.domains.sensors.data.SensorData
import com.simocach.simocach.domains.sensors.data.SensorDataSerializerAPI
import com.simocach.simocach.domains.sensors.data.WaterQualityResponse
import com.simocach.simocach.domains.sensors.data.api.RetrofitClient
import com.simocach.simocach.domains.sensors.packets.SensorPacketsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketClient(
    private val context: Context //, private val sensorPacketsProvider: SensorPacketsProvider
) : WebSocketListener() {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder().build()
    private val gson = Gson()
    var mDefaultScope = CoroutineScope(Job() + Dispatchers.Default)

    // Inicia la conexión WebSocket
    fun start() {
        val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val websocketIp = prefs.getString("websocket_ip", "192.168.4.1") ?: "192.168.4.1"
        val url = "ws://$websocketIp/ws"

        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, this)
        Log.d("WebSocketClient", "start: $url")
    }

    // Enviar un mensaje de texto al servidor WebSocket
    fun send(text: String) {
        webSocket?.send(text)
    }

    // Enviar un mensaje en forma de ByteString al servidor WebSocket
    fun send(bytes: ByteString) {
        webSocket?.send(bytes)
    }

    // Cierra la conexión WebSocket
    fun close() {
        webSocket?.close(1000, "Closing")
    }

    // Callback recibido cuando un mensaje de texto es recibido
    override fun onMessage(webSocket: WebSocket, text: String) {
//        Log.d("WebSocketClient", "message server: $text")
        val sensorData = gson.fromJson(text, SensorData::class.java)
//        Log.d("WebSocketClient", "gson: ${floatArrayOf(sensorData.TDS)}, ${floatArrayOf(sensorData.PH)}, ${floatArrayOf(sensorData.TEMP)}, ${floatArrayOf(sensorData.TURB)}")

        // Asigna los datos de cada sensor utilizando los identificadores de tipo correctos
        // Asumiendo que los tipos de sensores son 1=TEMP, 2=TURB, 3=TDS, 4=PH

        mDefaultScope.launch {
            SensorPacketsProvider.getInstance().onSensorChanged(
                Sensor.TYPE_TEMPERATURE, floatArrayOf(sensorData.TEMP)
            )
            SensorPacketsProvider.getInstance()
                .onSensorChanged(Sensor.TYPE_TURBIDITY, floatArrayOf(sensorData.TURB))
            SensorPacketsProvider.getInstance()
                .onSensorChanged(Sensor.TYPE_TDS, floatArrayOf(sensorData.TDS))
            SensorPacketsProvider.getInstance()
                .onSensorChanged(Sensor.TYPE_PH, floatArrayOf(sensorData.PH))

        }
        mDefaultScope.launch {

            try {
                // Toda tu lógica aquí
                val predictionBody = SensorDataSerializerAPI(
                    sensorData.TURB, sensorData.TDS, sensorData.PH
                )
                //            Only when after time of const SensorsConstants.SENSOR_DELAY_NORMAL execute the prediction

                val response = RetrofitClient.service.predictWaterQuality(
                    predictionBody
                )
                if (response.isSuccessful) {
                    val waterQualityResponse = response.body() as WaterQualityResponse
                    // Procesa la respuesta
                    SensorPacketsProvider.getInstance()
                        .onSensorChangedForPredictions(waterQualityResponse)
                    delay((SensorsConstants.SENSOR_DELAY_NORMAL * 1000).toLong())
                } else {
                    // Maneja error
                    val errorBody = WaterQualityResponse(
                        PH = 0f, Turbidez = 0f, TDS = 0f, prediction = "Error"
                    )
                    SensorPacketsProvider.getInstance().onSensorChangedForPredictions(errorBody)
                }

            } catch (e: Exception) {
                Log.e("WebSocketClient", "Error processing message: ", e)
                // Maneja la excepción adecuadamente
            }
        }
    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocketClient", "Error on WebSocket connection: ", t)
        // Consider implementing a retry mechanism or notifying the user/interface
        start()  // Reintenta conectarse después de un tiempo de espera
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocketClient", "WebSocket closed: $reason")
        // Handle resource cleanup or state update if necessary
    }

}
