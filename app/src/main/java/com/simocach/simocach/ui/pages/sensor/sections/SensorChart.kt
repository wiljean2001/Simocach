package com.simocach.simocach.ui.pages.sensor.sections

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.simocach.simocach.domains.chart.entity.ModelChartUiUpdate
import com.simocach.simocach.domains.chart.mpchart.MpChartDataManager
import com.simocach.simocach.domains.chart.mpchart.MpChartViewBinder
import com.simocach.simocach.domains.chart.mpchart.MpChartViewUpdater
import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.provider.ModelSensor
import com.simocach.simocach.ui.components.chart.mpchart.MpChartLineAxis
import com.simocach.simocach.ui.resource.values.JlResDimens
import com.simocach.simocach.ui.resource.values.JlResShapes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun SensorChart(
    modelSensor: ModelSensor = ModelSensor(
        type = Sensor.TYPE_TEMPERATURE
    ),
    mpChartDataManager: MpChartDataManager = MpChartDataManager(modelSensor.type),
    mpChartViewUpdater: MpChartViewUpdater = MpChartViewUpdater(),
    sensorPacketFlow: SharedFlow<ModelChartUiUpdate> = MutableSharedFlow<ModelChartUiUpdate>(replay = 0),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    Log.d(
        "HomeSensorChart",
        "Chart model: ${modelSensor.name} ${modelSensor.type}  ${mpChartDataManager.sensorType}"
    )

    //    var mpChartViewManager = MpChartViewManager(modelSensor.type)
//    val sensorUiUpdate = rememberChartUiUpdateEvent(mpChartDataManager, SensorManager.SENSOR_DELAY_NORMAL)
    val state = sensorPacketFlow.collectAsState(
        initial = ModelChartUiUpdate(
            sensorType = modelSensor.type, 0, listOf()
        )
    )
    val sensorUiUpdate = remember {
        state
    }

//    var counter = 0
//    Log.d("DefaultChartTesting", "Linechart isUpdating ${isUpdating.value}")
    var colorSurface = MaterialTheme.colorScheme.surface
    val colorOnSurface = MaterialTheme.colorScheme.onSurface
    Column(
        modifier = Modifier
            .background(color = Color.Transparent)
            .padding(horizontal = JlResDimens.dp12, vertical = JlResDimens.dp12)
//            .height(JlResDimens.dp168)
            .fillMaxSize(),
    ) {

        /*   Text(
               modifier = Modifier.padding(horizontal = JlResDimens.dp12),
               text = "${modelSensor.name}",


               color = MaterialTheme.colorScheme.onSurface,
               textAlign = TextAlign.Start,
               style = JlResTxtStyles.h5,
           )*/

        AndroidView(modifier = Modifier
            .background(color = Color.Transparent)
//            .height(JlResDimens.dp168)
            .fillMaxSize(),


            factory = { ctx ->

                Log.v("HomeSensorChart", "factory: ${mpChartDataManager.sensorType}")

                val view = MpChartLineAxis(modelSensor.type)
//                view
                return@AndroidView MpChartViewBinder(
                    ctx, view, colorOnSurface = colorOnSurface
                ).prepareDataSets(mpChartDataManager.getModel()).invalidate()
//                mpChartViewManager.createChart(ctx, colorSurface, colorOnSurface)
            }, update = {
//                Log.v("HomeSensorChart", "update aa: ${sensorUiUpdate.value.sensorType} ${it.tag}  ${sensorUiUpdate.value.timestamp} ${sensorUiUpdate.value.size}")

                mpChartViewUpdater.update(it, sensorUiUpdate.value, mpChartDataManager.getModel())
//                Log.v("HomeSensorChart", "update: ${mpChartDataManager.sensorType} ${isUpdated}")

//                mpChartDataManager.runPeriodically()
                //updateData(it, sensorUiUpdate.value)
            })
        Spacer(modifier = JlResShapes.Space.H18)

    }


}