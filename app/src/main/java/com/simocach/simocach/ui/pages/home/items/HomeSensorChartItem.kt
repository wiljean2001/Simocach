package com.simocach.simocach.ui.pages.home.items

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.simocach.simocach.domains.chart.entity.ModelChartUiUpdate
import com.simocach.simocach.domains.chart.mpchart.MpChartDataManager
import com.simocach.simocach.domains.chart.mpchart.MpChartViewBinder
import com.simocach.simocach.domains.chart.mpchart.MpChartViewUpdater
import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.ui.components.chart.mpchart.base.MpChartLineView
import com.simocach.simocach.ui.pages.home.model.ModelHomeSensor
import com.simocach.simocach.ui.resource.values.JlResDimens
import com.simocach.simocach.ui.resource.values.JlResShapes
import com.simocach.simocach.ui.resource.values.JlResTxtStyles

@Composable
fun HomeSensorChartItem(
    modelSensor: ModelHomeSensor = ModelHomeSensor(
        type = Sensor.TYPE_TURBIDITY
    ),
    mpChartDataManager: MpChartDataManager = MpChartDataManager(modelSensor.type),
    mpChartViewUpdater: MpChartViewUpdater = MpChartViewUpdater(),

    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {

    val sensorUiUpdate = mpChartDataManager.mSensorPacketFlow.collectAsState(
        initial = ModelChartUiUpdate(
            sensorType = modelSensor.type, 0, listOf()
        )
    )

    Log.d(
        "HomeSensorChart",
        "Chart model: ${modelSensor.name} ${modelSensor.type}  ${mpChartDataManager.sensorType}"
    )

    val colorOnSurface = MaterialTheme.colorScheme.onSurface
    Column(
        modifier = Modifier
            .background(color = Color.Transparent)
            .padding(horizontal = JlResDimens.dp12, vertical = JlResDimens.dp12)
//            .height(JlResDimens.dp168)
            .fillMaxSize(),
    ) {

        Text(
            modifier = Modifier.padding(horizontal = JlResDimens.dp12),
            text = modelSensor.name,

            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            style = JlResTxtStyles.h5,
        )
        AndroidView(
            modifier = Modifier
                .background(color = Color.Transparent)
//            .height(JlResDimens.dp168)
                .fillMaxSize(),
            factory = { ctx ->

                Log.v("HomeSensorChart", "factory: ${mpChartDataManager.sensorType}")

                val view = MpChartLineView(modelSensor.type)
//                view
                return@AndroidView MpChartViewBinder(
                    ctx, view, colorOnSurface = colorOnSurface
                ).prepareDataSets(mpChartDataManager.getModel()).invalidate()
//                mpChartViewManager.createChart(ctx, colorSurface, colorOnSurface)
            },
            update = {
                Log.v("HomeSensorChart", "update: ${mpChartDataManager.sensorType}")
                mpChartViewUpdater.update(it, sensorUiUpdate.value, mpChartDataManager.getModel())
            },
        )
        Spacer(modifier = JlResShapes.Space.H18)

    }

    DisposableEffect(mpChartDataManager.sensorType) {
        /*  val observer = LifecycleEventObserver { _, event ->
              if (event == Lifecycle.Event.ON_START) {
  //                currentOnStart()
              } else if (event == Lifecycle.Event.ON_DESTROY) {
  //                currentOnStop()
                  Log.v("HomeSensorChart", "destroy: ${mpChartDataManager.sensorType}")
                  mpChartDataManager.destroy()
              }
          }

          // Add the observer to the lifecycle
          lifecycleOwner.lifecycle.addObserver(observer)*/
        onDispose {

            Log.v("HomeSensorChart", "dispose: ${mpChartDataManager.sensorType}")
//            mpChartDataManager.destroy()
//            lifecycleOwner.lifecycle.removeObserver(observer)


        }
    }
}