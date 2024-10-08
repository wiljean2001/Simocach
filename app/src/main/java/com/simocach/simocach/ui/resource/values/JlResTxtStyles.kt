package com.simocach.simocach.ui.resource.values

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.simocach.simocach.R

object JlResTxtStyles {

    private val fontsJost = FontFamily(
        Font(R.font.jost_light, FontWeight.Light),
        Font(R.font.jost_regular),
        Font(R.font.jost_medium, FontWeight.Medium),
        Font(R.font.jost_bold, FontWeight.Bold)
    )

    val h1 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Medium, fontSize = JlResDimens.sp42
    )

    val h2 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp32
    )

    val h3 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp28
    )

    val h4 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp20
    )

    val h5 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp18
    )

    val h6 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp14
    )

    val p1 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp20
    )

    val p2 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp16
    )

    val p3 = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp12
    )


    val button = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Medium
    )

    val caption = TextStyle(
        fontFamily = fontsJost, fontWeight = FontWeight.Normal, fontSize = JlResDimens.sp12
    )


}