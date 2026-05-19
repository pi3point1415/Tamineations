package com.pi3point14.tamineations

import Formation
import Position
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch


const val WIDTH = 16f
const val HEIGHT = 16f


fun DrawScope.drawDancer(
    dancer: Dancer,
    textMeasurer: TextMeasurer,
    scale: Float,
) {
    val colorFill = when(dancer.side) {
        Side.HEAD -> Color(0xffc2c2ff)
        Side.SIDE -> Color(0xffffc2c2)
        else -> Color(0xffd6d6d6)
    }
    val colorStroke = when(dancer.side) {
        Side.HEAD -> Color(0xff0000b3)
        Side.SIDE -> Color(0xffb30000)
        else -> Color(0xff3b3b3b)
    }

    withTransform({
        translate(-2 * dancer.x.value, -2 * dancer.y.value)
    }) {

        if (dancer.facing.value != null) {
            withTransform({
                rotate(dancer.rotation.value, pivot = Offset.Zero)
            }) {
                drawCircle(
                    color = colorStroke,
                    radius = 0.25f,
                    center = Offset(0f, 0.5f),
                    style = Fill,
                )

                when (dancer.gender) {
                    Gender.BOY -> {
                        drawRect(
                            color = colorFill,
                            size = Size(1f, 1f),
                            topLeft = Offset(-0.5f, -0.5f),
                            style = Fill,
                        )

                        drawRect(
                            color = colorStroke,
                            size = Size(1f, 1f),
                            topLeft = Offset(-0.5f, -0.5f),
                            style = Stroke(width = 0.1f),
                        )

                    }

                    Gender.GIRL -> {
                        drawCircle(
                            color = colorFill,
                            radius = 0.5f,
                            center = Offset.Zero,
                            style = Fill,
                        )

                        drawCircle(
                            color = colorStroke,
                            radius = 0.5f,
                            center = Offset.Zero,
                            style = Stroke(width = 0.1f),
                        )
                    }

                    else -> {
                        drawRoundRect(
                            color = colorFill,
                            size = Size(1f, 1f),
                            topLeft = Offset(-0.5f, -0.5f),
                            cornerRadius = CornerRadius(0.25f),
                            style = Fill,
                        )

                        drawRoundRect(
                            color = colorStroke,
                            size = Size(1f, 1f),
                            topLeft = Offset(-0.5f, -0.5f),
                            cornerRadius = CornerRadius(0.25f),
                            style = Stroke(width = 0.1f),
                        )
                    }
                }
            }
        }

        if (dancer.number != null) {
            drawCenteredText(
                text = dancer.number.toString(),
                center = Offset.Zero,
                textMeasurer = textMeasurer,
                style = TextStyle(color = colorStroke, fontSize = 0.5f.sp),
                scale,
            )
        }
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

@Composable
fun App() {
    val textMeasurer = rememberTextMeasurer()
    val scope = rememberCoroutineScope()

    val moveChannel = Channel<Formation>(Channel.UNLIMITED)

    Square.reset()

    val dancers = remember {
        Square.formation.positions.map { Dancer(it) }
    }

    fun move(formation: Formation) {
        moveChannel.trySend(formation)

//        dancers.forEach {
//            dancer ->
//            val end = Square.getDancer(dancer.number, dancer.gender) ?: return
//
//            scope.launch {
//                dancer.animateTo(end)
//            }
//        }
    }

    Square.clearMoveCallbacks()
    Square.addMoveCallback(::move)

    Canvas(modifier = Modifier.fillMaxSize().clickable { Sequencer.next() }) {
        val scaleX = size.width / WIDTH
        val scaleY = size.height / HEIGHT
        val scale = minOf(scaleX, scaleY)

        withTransform({
            translate(left = size.width / 2f, top = size.height / 2f)
            scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
        }) {
            dancers.forEach { dancer ->
                drawDancer(dancer, textMeasurer, scale)
            }
        }
    }

    scope.launch {
        for (pos in moveChannel) {
            dancers.mapNotNull { dancer ->
                val end = pos.getDancer(dancer.number, dancer.gender) ?: return@mapNotNull null

                scope.launch { dancer.animateTo(end) }
            }.joinAll()
        }
    }
}