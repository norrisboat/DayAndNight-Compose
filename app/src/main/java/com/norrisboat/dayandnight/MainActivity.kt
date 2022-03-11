package com.norrisboat.dayandnight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norrisboat.dayandnight.ui.theme.DayAndNightTheme
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DayAndNightTheme {
                // A surface container using the 'background' color from the theme

                val isDayTime = remember {
                    mutableStateOf(true)
                }

                val backgroundColor by animateColorAsState(
                    targetValue = if (isDayTime.value) Color.White else Color.Black,
                    animationSpec = tween(500)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.TopCenter
                ) {

                    DayAndNightView(
                        modifier = Modifier.padding(top = 100.dp),
                        isDay = isDayTime.value,
                        onDayChanged = { isDay ->
                            isDayTime.value = isDay
                        })

                }
            }
        }
    }
}

@Composable
fun DayAndNightView(
    modifier: Modifier = Modifier,
    isDay: Boolean,
    onDayChanged: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedCircle(isDay)
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Choose a Style",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDay) Color.Black else Color.White
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Pop or subtle. Day or night.\nCustomize our interface",
            textAlign = TextAlign.Center,
            color = if (isDay) Color.Black else Color.White
        )
        Spacer(modifier = Modifier.height(30.dp))
        CapsuleToggle(isDay = isDay, onToggle = { isDay ->
            onDayChanged(isDay)
        })
    }
}

@Composable
fun AnimatedCircle(isDay: Boolean) {

    val radiusAnimator by animateFloatAsState(
        targetValue = if (!isDay) 260f else 50f,
        animationSpec = tween(1000)
    )

    val angleAnimator by animateFloatAsState(
        targetValue = if (!isDay) 55f else 45f,
        animationSpec = tween(1000)
    )

    val yOffset by animateFloatAsState(
        targetValue = if (!isDay) 90f else -50f,
        animationSpec = tween(1000)
    )

    val xOffset by animateFloatAsState(
        targetValue = if (!isDay) 150f else -50f,
        animationSpec = tween(1000)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isDay) Color.White else Color.Black,
        animationSpec = tween(1000 / 2)
    )

    Canvas(modifier = Modifier.size(200.dp, 200.dp), onDraw = {

        val centreX = size.height / 2
        val centreY = size.height / 2
        val radius = size.height / 2

        val radians = angleAnimator * (Math.PI / 180)
        val pointY = centreY + yOffset - (cos(radians) * radius)
        val pointX = centreX - xOffset + (sin(radians) * radius)

        val deepOrange = Color(252, 53, 94)
        val lightOrange = Color(237, 114, 16)

        val deepBlue = Color(129, 110, 253)
        val lightBlue = Color(132, 194, 253)

        drawCircle(
            brush = if (isDay) {
                Brush.linearGradient(listOf(deepOrange, lightOrange))
            } else {
                Brush.linearGradient(listOf(deepBlue, lightBlue))
            }
        )

        drawCircle(
            color = backgroundColor,
            radius = radiusAnimator,
            center = Offset(pointX.toFloat(), pointY.toFloat())
        )
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DayAndNightViewPreview() {
    DayAndNightTheme {
        DayAndNightView(isDay = true, onDayChanged = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CirclesPreview() {
    DayAndNightTheme {
        AnimatedCircle(false)
    }
}