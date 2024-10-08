package com.simocach.simocach.ui.navigation

import android.hardware.Sensor
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simocach.simocach.ui.pages.SplashPage
import com.simocach.simocach.ui.pages.about.AboutPage
import com.simocach.simocach.ui.pages.home.HomePage
import com.simocach.simocach.ui.pages.sensor.SensorPage
import com.simocach.simocach.ui.pages.setting.SettingPage

sealed class NavDirectionsApp(val route: String) {
    //    object Root : NavDirectionsApp("root")
    data object HomePage : NavDirectionsApp("home_page")
    data object SensorDetailPage : NavDirectionsApp("sensor_detail_page")
    data object AboutPage : NavDirectionsApp("about_page")
    data object Splash : NavDirectionsApp("splash_page")
    data object SettingPage : NavDirectionsApp("setting_page")
}

@Composable
fun NavGraphApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavDirectionsApp.Splash.route) {

        composable(NavDirectionsApp.Splash.route) { SplashPage(navController) }
        composable(NavDirectionsApp.HomePage.route) {
            HomePage(
                navController = navController
            )
        }
        composable(NavDirectionsApp.SettingPage.route) { SettingPage(navController = navController) }
        composable("${NavDirectionsApp.SensorDetailPage.route}/{type}", listOf(navArgument("type") {
            type = NavType.IntType
        })) {
            SensorPage(
                navController = navController,
                type = it.arguments?.getInt("type") ?: Sensor.TYPE_GYROSCOPE,
                // TODO might be creating bug

            )
        }

        composable(NavDirectionsApp.AboutPage.route) { AboutPage(navController = navController) }
    }

}