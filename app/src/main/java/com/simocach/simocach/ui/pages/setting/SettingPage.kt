package com.simocach.simocach.ui.pages.setting

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.simocach.simocach.ui.resource.values.JlResDimens
import com.simocach.simocach.ui.resource.values.JlResTxtStyles

@OptIn(
    ExperimentalMaterial3Api::class
)
@Preview(showBackground = true, backgroundColor = 0xFF041B11)
@Composable
fun SettingPage(modifier: Modifier = Modifier, navController: NavController? = null) {

    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    val sharedPreferences =
        remember { context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }
    var websocketIp by remember {
        mutableStateOf(
            sharedPreferences.getString(
                "websocket_ip", "192.168.4.1"
            ) ?: "192.168.4.1"
        )
    }
    var apiIp by remember {
        mutableStateOf(
            sharedPreferences.getString(
                "api_ip", "192.168.4.2"
            ) ?: "192.168.4.2"
        )
    }
    var apiPort by remember {
        mutableStateOf(
            sharedPreferences.getString("api_port", "8080") ?: "8080"
        )
    }

    var showError by remember { mutableStateOf(false) }

    val isAtTop = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }
    Scaffold(topBar = {

        TopAppBar(title = {
            Text(
                text = "Configuraciones",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = JlResTxtStyles.h4,
                fontWeight = FontWeight(400),
                modifier = modifier.fillMaxWidth(),
            )
        }, modifier = Modifier.padding(horizontal = JlResDimens.dp16), navigationIcon = {

            /*AppBarIcon(
                            icon = imageResource(
                                id = R.drawable.ic_menu_black_24dp)
                        ) {
                            // Open nav drawer
                        }*/

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

        },
            //            backgroundColor = Color.Transparent,
            colors = if (!isAtTop.value) TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), //Add your own color here, just to clarify.
            ) else TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent //Add your own color here, just to clarify.
            )
        )
    }) { it ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(it)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                        ), startY = 0f, endY = Float.POSITIVE_INFINITY

                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = websocketIp,
                    onValueChange = { websocketIp = it },
                    label = { Text("WebSocket IP(*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("192.168.4.1") },
                    isError = showError && !isValidIp(websocketIp)
                )
                Spacer(modifier = Modifier.height(JlResDimens.dp16))
                TextField(value = apiIp,
                    onValueChange = { apiIp = it },
                    label = { Text("API IP - IP del backend(*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("192.168.4.2") },
                    isError = showError && !isValidIp(apiIp)
                )
                Spacer(modifier = Modifier.height(JlResDimens.dp16))
                TextField(value = apiPort,
                    onValueChange = { apiPort = it },
                    label = { Text("API Puerto(*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("8080") },
                    isError = showError && !isValidPort(apiPort)
                )
                Spacer(modifier = Modifier.height(JlResDimens.dp16))
                Button(
                    onClick = {
                        if (isValidIp(websocketIp) && isValidIp(apiIp)) {
                            val editor = sharedPreferences.edit()
                            editor.putString("websocket_ip", websocketIp)
                            editor.putString("api_ip", apiIp)
                            editor.putString("api_port", apiPort)
                            editor.apply()
                            navController?.navigateUp()
//                            After show notification
                            Toast.makeText(
                                context,
                                "Configuraciones guardadas. Por favor reinicia la app.",
                                Toast.LENGTH_LONG
                            ).show()
                            showError = false
                        } else {
                            showError = true
                        }
                    }, modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Guardar")
                }
                if (showError) {
                    Text(
                        "Por favor ingresa direcciones IP válidas",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun isValidIp(ip: String): Boolean {
    return ip.matches(Regex("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$"))
}


fun isValidPort(port: String): Boolean {
    return port.matches(Regex("^([0-9]{1,5})$")) && port.toIntOrNull()
        ?.let { it in 0..65535 } == true
}