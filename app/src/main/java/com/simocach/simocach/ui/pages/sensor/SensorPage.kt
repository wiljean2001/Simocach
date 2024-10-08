package com.simocach.simocach.ui.pages.sensor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simocach.simocach.R
import com.simocach.simocach.domains.sensors.Sensor
import com.simocach.simocach.domains.sensors.provider.SensorsProvider
import com.simocach.simocach.ui.pages.sensor.sections.SensorChart
import com.simocach.simocach.ui.pages.sensor.sections.SensorDetail
import com.simocach.simocach.ui.pages.sensor.sections.SensorDetailCurrentValue
import com.simocach.simocach.ui.pages.sensor.sections.SensorDetailHeader
import com.simocach.simocach.ui.resource.values.JlResDimens
import com.simocach.simocach.ui.resource.values.JlResTxtStyles

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Preview(showBackground = true, backgroundColor = 0xFF041B11)
@Composable
fun SensorPage(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    type: Int = Sensor.TYPE_TEMPERATURE,
    viewModel: SensorViewModel = viewModel(
        factory = SensorViewModelFactory(
            type
        )// viewModelSensor
    )
//    viewModel: SensorViewModel = SensorViewModel()
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val tabItems = listOf("Graph") //, "Visual"
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabItems.size })

    val sensorFlowState = SensorsProvider.getInstance().listenSensor(type)
        .collectAsState(initial = SensorsProvider.getInstance().getSensor(type))
    val sensorState = remember {
        sensorFlowState
    }

    val sensorRms = viewModel.mSensorModulus.collectAsState(initial = 0.0f)

    val sensorRmsState = remember { sensorRms }


    val isAtTop = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }


    Scaffold(
        topBar = {
            // AppBar
            TopAppBar(colors = if (!isAtTop.value) TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), //Add your own color here, just to clarify.
            ) else TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent //Add your own color here, just to clarify.
            ),

                modifier = Modifier.padding(horizontal = JlResDimens.dp16),

                navigationIcon = {

                    IconButton(
                        onClick = { navController?.navigateUp() },
//                    modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(Icons.Rounded.KeyboardArrowLeft, "back")

                        /* Image(
                             painterResource(id = R.drawable.ic_round_keyboard_arrow_left_24),
                             contentDescription = "slide to left",
                             colorFilter = ColorFilter.tint(Color(0xFFFFFFFF)),
                             alignment = Alignment.Center,
                         )*/
                    }

                }, title = {
                    Text(
                        text = "${sensorState.value?.name}",
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        style = JlResTxtStyles.h4,
                        fontWeight = FontWeight(400),
                        modifier = modifier.fillMaxWidth(),
                    )
                }, actions = {
                    Box(Modifier.padding(horizontal = JlResDimens.dp20)) {
                        Image(

                            painterResource(id = R.drawable.pic_logo),
                            modifier = Modifier
                                .alpha(0f)
                                .width(JlResDimens.dp32)
                                .height(JlResDimens.dp36),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                })
        },
    ) {
        LazyColumn(

            modifier = Modifier
                .consumeWindowInsets(it)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),

                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                        )
                    )
                )
                .fillMaxSize(),
//                .background(JLThemeBase.colorPrimary10)
//                .consumedWindowInsets

//                .padding(start = JlResDimens.dp32, end = JlResDimens.dp32),
            contentPadding = it, state = lazyListState
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier.padding(
                        start = JlResDimens.dp32, end = JlResDimens.dp32
                    ),
                ) {
                    SensorDetailHeader(pagerState, coroutineScope, tabItems)
                }
            }
            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .height(JlResDimens.dp350)
                        .background(color = Color.Transparent)
                ) { page ->
                    // TODO: add sensor charts
//                    if (page == 0 && sensorState.value != null) {
//                        SensorChart(
//                            sensorState.value!!,
//                            mpChartDataManager = viewModel.getChartDataManager(sensorState.value!!.type),
//                            sensorPacketFlow = viewModel.mSensorPacketFlow
//                        )
//                    }

                    when (page) {
                        0 -> {
                            // Mostrar el gráfico del sensor
                            if (sensorState.value != null) {
                                SensorChart(
                                    sensorState.value!!,
                                    mpChartDataManager = viewModel.getChartDataManager(sensorState.value!!.type),
                                    sensorPacketFlow = viewModel.mSensorPacketFlow
                                )
                            }
                        }

//                        1 -> {
//                            Otro diseño de sensor
//                            if (sensorState.value != null) {
//                                SensorVisual(sensorState.value?.info ?: mutableMapOf())
//                            }
//                        }
//                        2 -> {
                        //                            Otro diseño de sensor
//                            if (sensorState.value != null) {
//                                SensorVisual(sensorState.value?.info ?: mutableMapOf())
//                            }
//                        }
                    }

                }
            }

            // Plotting area
            item {
                Spacer(modifier = Modifier.height(JlResDimens.dp16))
            }

            item {
                Box(
                    modifier = Modifier.padding(
                        start = JlResDimens.dp32, end = JlResDimens.dp32
                    ),
                ) { SensorDetailCurrentValue(sensorType = type, value = sensorRmsState.value) }
            }

            item {
                Spacer(modifier = Modifier.height(JlResDimens.dp12))
            }

            item {
                Box(
                    modifier = Modifier.padding(
                        start = JlResDimens.dp32, end = JlResDimens.dp32
                    ),
                ) {
                    SensorDetail(sensorState.value?.info ?: mutableMapOf())
                }
            }

            item {
                Spacer(modifier = Modifier.height(JlResDimens.dp72))
            }
        }
    }

}