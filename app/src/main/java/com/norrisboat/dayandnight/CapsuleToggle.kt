package com.norrisboat.dayandnight

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norrisboat.dayandnight.ui.theme.DayAndNightTheme

@Composable
fun CapsuleToggle(modifier: Modifier = Modifier, isDay: Boolean, onToggle: (Boolean) -> Unit) {

    val width = 600f
    val height = 150f

    val thumbXPosition by animateFloatAsState(
        targetValue = if (isDay) 0f else width / 2f,
        tween(500)
    )

    val thumbRightPosition by animateFloatAsState(
        targetValue = if (isDay) width / 2f else width,
        tween(500)
    )

    val thumbBackground by animateColorAsState(
        targetValue = if (isDay) {
            Color(227, 227, 229)
        } else {
            Color(25, 24, 31)
        },
        tween(500)
    )

    val dayUnselectedColor = Color(119, 119, 121)

    val nightUnselectedColor = Color(109, 106, 116)

    val selectedThumbColor by animateColorAsState(
        targetValue = if (isDay) {
            Color.White
        } else {
            Color(44, 40, 53)
        },
        tween(200)
    )

    val canvasModifier = modifier
        .size(220.dp, 60.dp)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset ->
                    // Light was tapped if the x offset is less than half of the width
                    onToggle(offset.x <= width / 2)
                }
            )
        }

    Canvas(modifier = canvasModifier.size(220.dp, 60.dp), onDraw = {
        drawRoundRect(
            color = thumbBackground,
            size = Size(width, height),
            cornerRadius = CornerRadius(100f)
        )

        drawIntoCanvas {
            val paint = Paint().apply {
                isAntiAlias = true
                color = selectedThumbColor
            }
            val frameworkPaint = paint.asFrameworkPaint()
            if (isDay) {
                frameworkPaint.setShadowLayer(
                    5.dp.toPx(),
                    0f,
                    0f,
                    Color.Gray.toArgb()
                )
            }
            it.drawRoundRect(
                thumbXPosition,
                0f,
                thumbRightPosition,
                height,
                100.dp.toPx(),
                100.dp.toPx(),
                paint
            )
        }

        val selectedTextPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 20.sp.toPx()
            color = if (isDay) Color.Black.toArgb() else Color.White.toArgb()
        }

        val deselectedTextPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 20.sp.toPx()
            color = if (isDay) dayUnselectedColor.toArgb() else nightUnselectedColor.toArgb()
        }

        drawIntoCanvas {
            it.nativeCanvas.drawText(
                "Light",
                80f,
                (height / 2) + 25,
                if (isDay) selectedTextPaint else deselectedTextPaint
            )
        }

        drawIntoCanvas {
            it.nativeCanvas.drawText(
                "Dark",
                width - 200,
                (height / 2) + 25,
                if (!isDay) selectedTextPaint else deselectedTextPaint
            )
        }
    })

}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun CapsuleTogglePreview() {
    DayAndNightTheme {
        CapsuleToggle(isDay = true, onToggle = {})
    }
}