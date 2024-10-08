package com.simocach.simocach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.simocach.simocach.domains.sensors.data.api.RetrofitClient
import com.simocach.simocach.domains.sensors.data.websocket.WebSocketClient
import com.simocach.simocach.domains.sensors.packets.SensorPacketsProvider
import com.simocach.simocach.domains.sensors.provider.SensorsProvider
import com.simocach.simocach.ui.navigation.NavGraphApp
import com.simocach.simocach.ui.resource.themes.SimocachM3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val webSocketClient = WebSocketClient(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            webSocketClient.start()  // Comienza a escuchar los datos del WebSocket
        }
        
        RetrofitClient.initialize(this)

        setContent {
            SimocachM3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavGraphApp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            SensorsProvider.getInstance().clearAll()
            SensorPacketsProvider.getInstance().clearAll()

            CoroutineScope(Job())
        }
    }

}
