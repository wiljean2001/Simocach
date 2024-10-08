package com.simocach.simocach.ui.resource.sensors

import android.util.SparseIntArray
import com.simocach.simocach.R
import com.simocach.simocach.domains.sensors.Sensor

object SensorsIcons {


    val MAP_TYPE_TO_ICON: SparseIntArray = object : SparseIntArray() {
        init {
            put(Sensor.TYPE_TEMPERATURE, R.drawable.ic_sensor_gyroscope) //1
            put(Sensor.TYPE_TURBIDITY, R.drawable.ic_sensor_gyroscope) //1
            put(Sensor.TYPE_TDS, R.drawable.ic_sensor_gyroscope) //1
            put(Sensor.TYPE_PH, R.drawable.ic_sensor_gyroscope) //1
        }
    }

    /*ICON_,R.drawable.ic_sensor_gyroscope
    ICON_,R.drawable.ic_sensor_gravity
    ICON_,R.drawable.ic_sensor_brightness
    ICON_,R.drawable.ic_sensor_magnet
    ICON_,R.drawable.ic_sensor_temprature
    ICON_,R.drawable.ic_sensor_proximity
    ICON_,R.drawable.ic_sensor_pressure
    ICON_,R.drawable.ic_sensor_humidity
    ICON_,R.drawable.ic_sensor_rotation
    ICON_,R.drawable.ic_sensor_linear_acceleration
    ICON_,R.drawable.ic_sensor_compass*/
}