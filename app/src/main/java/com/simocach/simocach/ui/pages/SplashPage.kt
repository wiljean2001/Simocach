package com.simocach.simocach.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simocach.simocach.R
import com.simocach.simocach.ui.navigation.NavDirectionsApp
import com.simocach.simocach.ui.resource.values.JlResShapes
import com.simocach.simocach.ui.resource.values.JlResTxtStyles
import kotlinx.coroutines.delay

@Composable
fun SplashPage(navController: NavController) {
//    val systemUiController = rememberSystemUiController()
////    if(darkTheme){
//        systemUiController.setSystemBarsColor(
//            color = Color.Transparent
//        )
    LaunchedEffect(key1 = true) {
        /*scaleAnimation.animateTo(
            targetValue = 0.5F,
            animationSpec = tween(
                durationMillis = durationMillisAnimation,
                easing = {
                    OvershootInterpolator(3F).getInterpolation(it)
                }
            )
        )*/

        delay(timeMillis = 500)

        navController.popBackStack()
        navController.navigate(NavDirectionsApp.HomePage.route)

        /*navController.navigate(route = DestinationScreen.MainScreenDest.route) {
            popUpTo(route = DestinationScreen.SplashScreenDest.route) {
                inclusive = true
            }
        }*/
    }
    SplashScreen()

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
//    imagePainter: Painter,
//    scaleAnimation: Animatable<Float, AnimationVector1D>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),

                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            /*Image(
                painter = imagePainter,
                contentDescription = "Logotipo Splash Screen",
                modifier = modifier
                    .size(400.dp)
                    .scale(scale = scaleAnimation.value),
            )*/
            Image(
                painter = painterResource(id = R.drawable.pic_logo),
                contentDescription = "Logotipo Splash Screen",
                modifier = modifier
                    .size(120.dp)
//                    .scale(scale = scaleAnimation.value),
            )
            Spacer(modifier = JlResShapes.Space.H24)
            Image(
                painter = painterResource(id = R.drawable.pic_launcher),
                contentDescription = "Logotipo Splash Screen",
                modifier = modifier
                    .size(220.dp)
//                    .scale(scale = scaleAnimation.value),
            )
            Spacer(modifier = JlResShapes.Space.H56)
            Text(
                text = "Simocach",
                style = JlResTxtStyles.h1.merge(
                    other = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.colorScheme.onSurface.copy(0.1f)
                            ),
                            tileMode = TileMode.Mirror,
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY),
                        )
                    )
                ),
//                        modifier = Modifier.(scale = scaleAnimation.value
                /*color = Color.White,
                fontSize = JlResDimens.dp40,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = modifier.scale(scale = scaleAnimation.value
                */
            )
        }
    }
}