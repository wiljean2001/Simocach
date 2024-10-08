package com.simocach.simocach.ui.pages.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simocach.simocach.R
import com.simocach.simocach.domains.sensors.packets.SensorPacketsProvider
import com.simocach.simocach.ui.composables.isScrollingUp
import com.simocach.simocach.ui.navigation.NavDirectionsApp
import com.simocach.simocach.ui.pages.home.items.HomeSensorItem
import com.simocach.simocach.ui.pages.home.sections.HomeHeader
import com.simocach.simocach.ui.pages.home.sections.HomeSensorGraphPager
import com.simocach.simocach.ui.pages.home.sections.PredictionView
import com.simocach.simocach.ui.resource.themes.JLThemeBase
import com.simocach.simocach.ui.resource.values.JlResDimens
import com.simocach.simocach.ui.resource.values.JlResShapes
import com.simocach.simocach.ui.resource.values.JlResTxtStyles
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Preview(showBackground = true, backgroundColor = 0xFF041B11)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory()
    )

) {

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
//    val sensorsProvider = SensorsProviderComposable()
//    val sensors = remember { sensorsProvider }

    val sensorUiState = viewModel.mUiState.collectAsState()
//    var sensorUiState = viewModel.mUiCurrentSensorState.collectAsState()

    val isAtTop = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }

    val sd = viewModel.mActiveSensorListFlow.collectAsState(initial = mutableListOf())

    val activeSensorStateList = remember { sd }

    val pagerState = rememberPagerState(
        pageCount = { activeSensorStateList.value.size },
    )

    val apiResponse by SensorPacketsProvider.getInstance().apiResponse.collectAsState()


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Simocach",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = JlResTxtStyles.h4,
                fontWeight = FontWeight(400),
                modifier = modifier.fillMaxWidth(),
            )
        }, navigationIcon = {
            Box(Modifier.padding(horizontal = JlResDimens.dp20)) {
                IconButton(onClick = { navController?.navigate(NavDirectionsApp.SettingPage.route) }) {
                    Image(
                        painterResource(id = R.drawable.pic_logo),
                        modifier = Modifier
                            .width(JlResDimens.dp32)
                            .height(JlResDimens.dp36),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
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
        }, colors = if (!isAtTop.value) TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), //Add your own color here, just to clarify.
        ) else TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent //Add your own color here, just to clarify.
        )
        )
    }, floatingActionButton = {

        AnimatedVisibility(
            visible = lazyListState.isScrollingUp(),
//                modifier = Modifier.fillMaxSize(),
            enter = scaleIn(), exit = scaleOut()
        ) {
            FloatingActionButton(
                onClick = { navController?.navigate(NavDirectionsApp.AboutPage.route) },
                shape = RoundedCornerShape(50),
                containerColor = Color.Transparent,

                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                JLThemeBase.colorPrimary.copy(alpha = 0.3f),
                                JLThemeBase.colorPrimary.copy(alpha = 0.1f),
                            )
                        ), shape = RoundedCornerShape(50.dp)
                    )
                    .border(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            )
                        ), width = JlResDimens.dp1, shape = RoundedCornerShape(50.dp)
                    ),
                elevation = FloatingActionButtonDefaults.elevation(JlResDimens.dp0)

            ) {

                Icon(Icons.Rounded.Info, "about")

            }
        }
    }

    ) { it ->

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
//                .consumedWindowInsets ,
            contentPadding = it, state = lazyListState
        ) {

            item {
                Spacer(modifier = JlResShapes.Space.H24)

            }
            // Header
            item {
                Box(
                    modifier = Modifier.padding(
                        start = JlResDimens.dp32, end = JlResDimens.dp32
                    ),
                ) {
                    HomeHeader(sensorUiState.value.currentSensor,
                        totalActive = sensorUiState.value.activeSensorCounts,
                        onClickArrow = { isLeft ->

                            val currentPage = pagerState.currentPage
                            val totalPage = pagerState.pageCount

                            if (!isLeft && currentPage + 1 < totalPage) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                }
                            } else if (isLeft && currentPage > 0 && totalPage > 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage - 1)
                                }
                            }
                        })
                }
            }
            // Plotting area
            item {
//                Spacer(modifier = Modifier.height(JlResDimens.dp350))

                HomeSensorGraphPager(viewModel = viewModel, pagerState = pagerState)

            }

            // Available Sensors
            item {
                Box(
                    modifier = Modifier.padding(
                        start = JlResDimens.dp40,
                        end = JlResDimens.dp32,
                        top = JlResDimens.dp12,
                        bottom = JlResDimens.dp16
                    ),
                ) {
                    Text(
                        text = "Sensores disponibles",
                        fontSize = JlResDimens.sp16,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }


            items(viewModel.mSensorsList.windowed(2, 2, true)) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = JlResDimens.dp32)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,


                        ) {

                        for (i in item.indices) {
                            Box(
                                modifier = Modifier.weight(1f)
//                                    .fillParentMaxWidth(0.5f)
//                                    .padding(bottom = JlResDimens.dp8)

                            ) {
                                HomeSensorItem(modelSensor = item[i],/* se = item[i].sensorName,
                                     sensorValue = item[i].sensorValue,
                                     sensorUnit = item[i].sensorUnit,
                                     sensorIcon = item[i].sensorIcon*/

                                    onCheckChange = { type: Int, isChecked: Boolean ->
                                        viewModel.onSensorChecked(type, isChecked)

                                    }, onClick = {
                                        navController?.navigate("${NavDirectionsApp.SensorDetailPage.route}/$it")
                                    })

                            }

                            if (i < item.size - 1) {
                                Spacer(modifier = Modifier.width(JlResDimens.dp8))
                            }
                        }
                        if (item.size % 2 != 0) {
                            Spacer(modifier = Modifier.width(JlResDimens.dp8))

                            Box(
                                modifier = Modifier.weight(1f)
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(JlResDimens.dp8))

            }
            item { Spacer(modifier = Modifier.height(JlResDimens.dp16)) }

            item {
                PredictionView(apiResponse = apiResponse)
            }
            item { Spacer(modifier = Modifier.height(JlResDimens.dp16)) }
        }
    }
}