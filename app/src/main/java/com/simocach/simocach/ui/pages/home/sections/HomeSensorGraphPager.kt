package com.simocach.simocach.ui.pages.home.sections

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.graphics.ColorUtils
import com.simocach.simocach.ui.pages.home.HomeViewModel
import com.simocach.simocach.ui.pages.home.items.HomeSensorChartItem
import com.simocach.simocach.ui.resource.effects.drawColoredShadow
import com.simocach.simocach.ui.resource.values.JlResDimens
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, backgroundColor = 0xFF041B11)
@Composable
fun HomeSensorGraphPager(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = HomeViewModel(),
    pagerState: PagerState = rememberPagerState(
        pageCount = { 0 },
    ),
) {
    val sd = viewModel.mActiveSensorListFlow.collectAsState(initial = mutableListOf())

    val activeSensorStateList = remember { sd }

    LaunchedEffect(pagerState) {
        snapshotFlow { "${pagerState.currentPage} ${pagerState.pageCount}" }.collect { page ->
            Log.d("HomeSensorGraphPager", "pager 2: $page")
            viewModel.setActivePage(pagerState.currentPage)
        }
    }
    Log.d("HomeSensorGraphPager", "pager 1: $pagerState ${activeSensorStateList.value.size}")

    HorizontalPager(
        state = pagerState,

        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(horizontal = JlResDimens.dp32),
        key = {
            if (it < activeSensorStateList.value.size) {

                activeSensorStateList.value[it].type
            } else {
                -1
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = JlResDimens.dp20),

        ) { page ->

        Card(
            Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = page.absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0, 1)
                    ).also { scale ->
                        Log.d("HomeSensorGraphPager", " scale: $scale")
                        scaleX = scale
                        scaleY = scale
                    }


                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0, 1)
                    )
                }
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.radialGradient(

                        listOf(
                            Color(
                                ColorUtils.blendARGB(
                                    MaterialTheme.colorScheme.onSurface.toArgb(),
                                    MaterialTheme.colorScheme.surface.toArgb(),
                                    0.8f
                                )
                            ),
                            Color(
                                ColorUtils.blendARGB(
                                    MaterialTheme.colorScheme.onSurface.toArgb(),
                                    MaterialTheme.colorScheme.surface.toArgb(),
                                    0.97f
                                )
                            ),
                        ),
                        center = Offset(200f, -30f),

                        radius = 600.0f

//                start = Offset(0f, 0f),
//                end = Offset(0f, Float.POSITIVE_INFINITY)
                    ), shape = RoundedCornerShape(JlResDimens.dp28)
                )
                .drawColoredShadow(
                    Color.Black,
                    offsetY = JlResDimens.dp12,
                    shadowRadius = JlResDimens.dp16,
                    borderRadius = JlResDimens.dp32,
                    alpha = 0.1f
                ),
            shape = RoundedCornerShape(JlResDimens.dp28),
            border = BorderStroke(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    )
                ),
                width = JlResDimens.dp1,
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),

                ) {
//                Log.d(
//                    "HomeSensorGraphPager",
//                    "Chart model: size: ${activeSensorStateList.value.size} ${page} ${activeSensorStateList.value[page]}"
//                )
                HomeSensorChartItem(
                    activeSensorStateList.value[page],
                    viewModel.getChartDataManager(activeSensorStateList.value[page].type)
                )
            }

        }
    }

}