package com.simocach.simocach.ui.pages.home.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.simocach.simocach.domains.sensors.data.WaterQualityResponse
import com.simocach.simocach.ui.resource.values.JlResColors
import com.simocach.simocach.ui.resource.values.JlResDimens


@Composable
fun getQualityIcon(prediction: String): @Composable () -> Unit {
    return when (prediction) {
        "Excelente" -> {
            {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Excelente",
                    tint = JlResColors.SensifyGreen
                )
            }
        }

        "Buena" -> {
            {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Buena",
                    tint = JlResColors.blue
                )
            }
        }

        "Aceptable" -> {
            {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "Aceptable",
                    tint = JlResColors.yellow
                )
            }
        }

        "Pobre" -> {
            {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "Pobre",
                    tint = JlResColors.goalColors[3]
                )
            }
        }

        else -> {
            {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Desconocido",
                    tint = JlResColors.SensifyGreen
                )
            }
        }
    }
}

@Composable
fun PredictionView(apiResponse: WaterQualityResponse?) {
    apiResponse?.let {
        Column(
            modifier = Modifier.padding(horizontal = JlResDimens.dp32),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Opinión de la IA",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(JlResDimens.dp8))
            Row(verticalAlignment = Alignment.CenterVertically) {
                getQualityIcon(it.prediction)() // Aquí insertamos el icono
                Spacer(modifier = Modifier.width(JlResDimens.dp8))
                Text(
                    text = it.prediction,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
