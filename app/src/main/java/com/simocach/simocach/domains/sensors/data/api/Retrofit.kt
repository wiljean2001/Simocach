package com.simocach.simocach.domains.sensors.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private lateinit var retrofit: Retrofit
    private var initialized = false

    fun initialize(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val ip = sharedPreferences.getString("api_ip", "localhost") ?: "localhost"
        val port = sharedPreferences.getString("api_port", "8000") ?: "8000"
        val baseUrl = "http://$ip:$port/"

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient())
            .build()

        initialized = true
    }

    private fun okHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val service: WaterQualityService
        get() {
            if (!initialized) throw IllegalStateException("RetrofitClient is not initialized")
            return retrofit.create(WaterQualityService::class.java)
        }
}
