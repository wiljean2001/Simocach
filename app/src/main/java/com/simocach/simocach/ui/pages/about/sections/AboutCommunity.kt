package com.simocach.simocach.ui.pages.about.sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.simocach.simocach.R
import com.simocach.simocach.ui.resource.values.JlResDimens

@Preview(showBackground = true, backgroundColor = 0xFF041B11)
@Composable
fun AboutCommunity() {

    val stroke = Stroke(
        width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(JlResDimens.dp18))
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
            )
            .border(
                color = MaterialTheme.colorScheme.surface,

                width = JlResDimens.dp1, shape = RoundedCornerShape(JlResDimens.dp18)
            )
            .padding(all = JlResDimens.dp24)
    ) {


        Column {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(Modifier.size(size = JlResDimens.dp32), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(color = onSurfaceColor.copy(alpha = 0.3f), style = stroke)
                    }
                }

                Box(Modifier.size(size = JlResDimens.dp32), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = onSurfaceColor.copy(alpha = 0.3f),
                            style = stroke,
                            cornerRadius = CornerRadius(
                                x = JlResDimens.dp14.value, y = JlResDimens.dp14.value
                            )
                        )
                    }
                }
                Box(Modifier.size(size = JlResDimens.dp32))
                Box(
                    Modifier
                        .size(size = JlResDimens.dp32)
                        .clip(shape = RoundedCornerShape(JlResDimens.dp12))
                ) {
                    Image(
                        painterResource(id = R.drawable.pic_person2),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Box(Modifier.size(size = JlResDimens.dp32))/*Box(Modifier.size(size = JlResDimens.dp32), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = onSurfaceColor.copy(alpha = 0.3f),
                            style = stroke,
                            cornerRadius = CornerRadius(x = JlResDimens.dp14.value, y = JlResDimens.dp14.value)
                        )
                    }
                }*/
                Box(
                    Modifier
                        .size(size = JlResDimens.dp32)
                        .background(Color(0xFF9747FF), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                }
            }
            Spacer(modifier = Modifier.height(JlResDimens.dp12))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Box(Modifier.size(size = JlResDimens.dp32), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = onSurfaceColor.copy(alpha = 0.3f),
                            style = stroke,
                            cornerRadius = CornerRadius(
                                x = JlResDimens.dp14.value, y = JlResDimens.dp14.value
                            )
                        )
                    }
                }
                Box(
                    Modifier
                        .size(size = JlResDimens.dp32)
                        .clip(shape = RoundedCornerShape(JlResDimens.dp12))
                ) {
                    Image(
                        painterResource(id = R.drawable.pic_person2),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    Modifier
                        .size(size = JlResDimens.dp32)
                        .background(Color(0xFFFFDB5A), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                }
                Box(Modifier.size(size = JlResDimens.dp32), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = onSurfaceColor.copy(alpha = 0.3f),
                            style = stroke,
                            cornerRadius = CornerRadius(
                                x = JlResDimens.dp14.value, y = JlResDimens.dp14.value
                            )
                        )
                    }
                }
                Box(
                    Modifier
                        .size(size = JlResDimens.dp32)
                        .background(Color(0xFF64DA93), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                }
                Box(Modifier.size(size = JlResDimens.dp32), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = onSurfaceColor.copy(alpha = 0.3f),
                            style = stroke,
                            cornerRadius = CornerRadius(
                                x = JlResDimens.dp14.value, y = JlResDimens.dp14.value
                            )
                        )
                    }
                }

            }
        }

    }
}