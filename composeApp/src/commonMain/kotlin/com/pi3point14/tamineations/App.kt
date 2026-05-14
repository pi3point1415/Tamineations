package com.pi3point14.tamineations

import Position
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*


const val WIDTH = 16f
const val HEIGHT = 16f


fun DrawScope.drawDancer(
    pos: Position,
    textMeasurer: TextMeasurer,
    scale: Float,
) {
    val colorFill = when(pos.side) {
        Side.HEAD -> Color(0xffc2c2ff)
        Side.SIDE -> Color(0xffffc2c2)
        else -> Color(0xffd6d6d6)
    }
    val colorStroke = when(pos.side) {
        Side.HEAD -> Color(0xff0000b3)
        Side.SIDE -> Color(0xffb30000)
        else -> Color(0xff3b3b3b)
    }

    val center = Offset(pos.x.toFloat(), pos.y.toFloat())

//    var moved by remember { mutableStateOf(false) }
//
//    val center by animateOffsetAsState(targetValue = if (moved) )

    val headOffset = when (pos.facing) {
        Facing.N -> Offset(center.x, center.y - 0.5f)
        Facing.E -> Offset(center.x + 0.5f, center.y)
        Facing.S -> Offset(center.x, center.y + 0.5f)
        Facing.W -> Offset(center.x - 0.5f, center.y)
        else -> Offset(center.x, center.y)
    }

    drawCircle(
        color = colorStroke,
        radius = 0.25f,
        center = headOffset,
        style = Fill,
    )

    when (pos.gender) {
        Gender.BOY -> {
            drawRect(
                color = colorFill,
                size = Size(1f, 1f),
                topLeft = Offset(center.x - 0.5f, center.y - 0.5f),
                style = Fill,
            )

            drawRect(
                color = colorStroke,
                size = Size(1f, 1f),
                topLeft = Offset(center.x - 0.5f, center.y - 0.5f),
                style = Stroke(width = 0.1f),
            )

        }
        Gender.GIRL -> {
            drawCircle(
                color = colorFill,
                radius = 0.5f,
                center = center,
                style = Fill,
            )

            drawCircle(
                color = colorStroke,
                radius = 0.5f,
                center = center,
                style = Stroke(width = 0.1f),
            )
        }
        else -> {
            drawRoundRect(
                color = colorFill,
                size = Size(1f, 1f),
                topLeft = Offset(center.x - 0.5f, center.y - 0.5f),
                cornerRadius = CornerRadius(0.25f),
                style = Fill,
            )

            drawRoundRect(
                color = colorStroke,
                size = Size(1f, 1f),
                topLeft = Offset(center.x - 0.5f, center.y - 0.5f),
                cornerRadius = CornerRadius(0.25f),
                style = Stroke(width = 0.1f),
            )
        }
    }

    if (pos.number != null) {
        drawCenteredText(
            text = pos.number.toString(),
            center = center,
            textMeasurer = textMeasurer,
            style = TextStyle(color = colorStroke, fontSize = 0.5f.sp),
            scale,
        )
    }
}


fun DrawScope.drawCenteredText(
    text: String,
    center: Offset,
    textMeasurer: TextMeasurer,
    style: TextStyle,
    scale: Float,
) {
    val fontSize = style.fontSize * scale
    val drawStyle = style.copy(fontSize = fontSize)
    val measured = textMeasurer.measure(text, drawStyle)

    // How much we need to shrink the text to fit logical space
    val textScale = 1f / scale

    // Half-dimensions in logical units
    val halfW = (measured.size.width / 2f) * textScale
    val halfH = (measured.size.height / 2f) * textScale

    withTransform({
        translate(center.x - halfW, center.y - halfH)
        scale(textScale, textScale, pivot = Offset.Zero)
    }) {
        drawText(measured)
    }
}

fun DrawScope.drawScene(textMeasurer: TextMeasurer, scale: Float) {
    for (pos in Square.formation.positions) {
        drawDancer(pos, textMeasurer, scale)
    }
}

@Composable
fun App() {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier.fillMaxSize()) {
        val scaleX = size.width / WIDTH
        val scaleY = size.height / HEIGHT
        val scale = minOf(scaleX, scaleY)

        withTransform({
            translate(left = size.width / 2f, top = size.height / 2f)
            scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
        }) {
            drawScene(textMeasurer, scale)
        }
    }
}