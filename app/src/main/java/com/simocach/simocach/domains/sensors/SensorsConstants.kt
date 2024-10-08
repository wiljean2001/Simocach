package com.simocach.simocach.domains.sensors

//import android.hardware.Sensor
import android.text.Html
import android.text.Spanned
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import com.simocach.simocach.domains.sensors.provider.ModelSensor
import com.simocach.simocach.ui.resource.values.JlResDimens

object SensorsConstants {

    const val DATA_AXIS_VALUE = 10
    const val DATA_AXIS_X = 11
    const val DATA_AXIS_Y = 12
    const val DATA_AXIS_Z = 13
    const val DATA_AXIS_VALUE_STRING = "val"
    const val DATA_AXIS_X_STRING = "x"
    const val DATA_AXIS_Y_STRING = "y"
    const val DATA_AXIS_Z_STRING = "z"

    const val DETAIL_KEY_NAME = "Nombre"
    const val DETAIL_KEY_S_TYPE = "Tipo"
    const val DETAIL_KEY_VERSION = "Version"
//    const val DETAIL_KEY_POWER = "Power"
//    const val DETAIL_KEY_Resolution = "Resolution"
//    const val DETAIL_KEY_Range = "Range"

    const val SENSOR_DELAY_NORMAL = 3
    const val SENSOR_DELAY_UI = 2

    // List of sensor types
    val SENSORS = arrayOf(
        Sensor.TYPE_TEMPERATURE,
        Sensor.TYPE_TURBIDITY,
        Sensor.TYPE_TDS,
        Sensor.TYPE_PH,
    )

//    fun getSensorList(types: Int): List<Sensor> {
//        return when (types) {
//            Sensor.TYPE_ALL -> SENSORS.map { type ->
//                Sensor(type, Sensor.getSensorName(type))
////                Sensor(type, Sensor.getSensorName())
//            }
//
//            else -> SENSORS.filter { it == types }.map { type ->
//                Sensor(type, Sensor.getSensorName(type))
//            }
//        }
//    }

    // Mapping from sensor delay types to delay values
    val MAP_DELAY_TYPE_TO_DELAY: SparseIntArray = object : SparseIntArray() {
        init {
            put(3, 200)
            put(2, 60)
            put(3, 20)
            put(3, 10)
        }
    }

    // Mapping from sensor types to the number of data axes
    val MAP_TYPE_TO_AXIS_COUNT: SparseIntArray = object : SparseIntArray() {
        init {
            put(Sensor.TYPE_TEMPERATURE, 1) //1
            put(Sensor.TYPE_TURBIDITY, 1) //2
            put(Sensor.TYPE_TDS, 1) //3
            put(Sensor.TYPE_PH, 1) //4
        }
    }

    // Mapping from sensor types to their names
    val MAP_TYPE_TO_NAME: SparseArray<String> = object : SparseArray<String>() {
        init {
            put(Sensor.TYPE_TEMPERATURE, "Temperatura") //1
            put(Sensor.TYPE_TURBIDITY, "Turbidez") //2
            put(Sensor.TYPE_TDS, "TDS") //3
            put(Sensor.TYPE_PH, "PH") //4
        }
    }

    fun hasUnit(sensorType: Int): Boolean {
        val hasUnitValue = when (sensorType) {
            Sensor.TYPE_TEMPERATURE -> true
            Sensor.TYPE_TURBIDITY -> true
            Sensor.TYPE_TDS -> true
            Sensor.TYPE_PH -> true
            else -> false
        }


        return hasUnitValue
    }

    fun getUnit(builder: AnnotatedString.Builder, sensorType: Int): AnnotatedString.Builder {
        builder.apply {
            when (sensorType) {
                Sensor.TYPE_TEMPERATURE -> append("°C")
                Sensor.TYPE_TURBIDITY -> append("NTU")
                Sensor.TYPE_TDS -> append("PPM")
                Sensor.TYPE_PH -> append("pH")

                else -> append("")
            }
        }

        return builder
    }

    private fun getSquaredText(text: String, supText: String): Spanned {
        val result: Spanned =
            Html.fromHtml("$text<sup><small>$supText</small></sup>", Html.FROM_HTML_MODE_LEGACY)
        return result
    }

    private fun getSquaredText(
        builder: AnnotatedString.Builder, text: String, supText: String
    ): AnnotatedString.Builder {

        val superscript = SpanStyle(
            baselineShift = BaselineShift.Superscript, // font size of superscript
//            fontFamily = JlResTxtStyles.fontsJost,
            fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp20
        )
        // create a variable subScript
        // enter the baselineShift to
        // BaselineShift.Subscript for subscript
        val subscript = SpanStyle(
            baselineShift = BaselineShift.Subscript,// font size of subscript
            fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp20
        )

        builder.apply {
            append(text)
            withStyle(superscript) {
                append(supText)
            }
        }

        return builder
    }

}