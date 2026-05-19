package com.pi3point14.tamineations

import Position
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Dancer (initial: Position) {
    val x = Animatable(-initial.x.toFloat())
    val y = Animatable(initial.y.toFloat())
    var facing = mutableStateOf(initial.facing)
    var side = initial.side
    var gender = initial.gender
    var number = initial.number

    val rotation = Animatable(initial.facing?.plus(180.0)?.toFloat() ?: 0f)

    suspend fun animateTo(target: Position, spec: AnimationSpec<Float> = tween(1000)) {
        val scope = coroutineScope {
            launch { x.animateTo(-target.x.toFloat(), spec) }
            launch { y.animateTo(target.y.toFloat(), spec) }
            launch {
                rotation.snapTo(rotation.value % 360)
                val current = rotation.value
                val targetRot = target.facing?.plus(180.0)?.toFloat() ?: 0f
                val delta = ((targetRot - current + 540) % 360) - 180
                rotation.animateTo(current + delta, spec)
                facing.value = target.facing
            }
        }

        scope.join()
    }
}