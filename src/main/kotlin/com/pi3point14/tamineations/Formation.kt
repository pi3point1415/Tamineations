package com.pi3point14.tamineations

import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class DancerPosition (var vec : Vec3d, val number: Int, val lark: Boolean) {
    val id = "${if (lark) 'L' else 'R'}${number}"

    fun lerp(other: DancerPosition?, fraction: Double): DancerPosition {
        if (other == null) {
            return DancerPosition(vec, number, lark)
        }
        val vecLerp = vec.lerp(other.vec, fraction)
        return DancerPosition(vecLerp, number, lark)
    }

    fun turn(turn: Double) {
        vec = vec.add(Vec3d(0.0, 0.0, turn))
    }

    fun moveForward(forward: Double) {
        val facing = facingVec.multiply(forward.toFloat())
        val move = Vec3d(facing.x.toDouble(), facing.y.toDouble(), 0.0)
        vec = vec.add(move)
    }

    fun moveAbs(vec: Vec3d) {
        this.vec = vec
    }

    val facingVec : Vec2f
        get () {
            return Vec2f(-sin(vec.z * PI / 180).toFloat(), cos(vec.z * PI / 180).toFloat())
        }

    val rightVec: Vec2f
        get() {
            return Vec2f(-facingVec.y, facingVec.x)
        }

    val leftVec: Vec2f
        get() {
            return Vec2f(facingVec.y, -facingVec.x)
        }
}

open class Formation (val dancers : List<DancerPosition>) {

    val epsilon = 1e-6

    fun getById(id : String) : DancerPosition? {
        for (dancer in dancers) {
            if (dancer.id == id) {
                return dancer
            }
        }
        return null
    }

    fun dancerAtLocation(vec : Vec3d) : DancerPosition? {
        for (dancer in dancers) {
            val diff = Vec3d(dancer.vec.x - vec.x, dancer.vec.y - vec.y, (dancer.vec.z - vec.z) % 360)
            if (diff.length() < epsilon) {
                return dancer
            }
        }
        return null
    }

    fun filterByIdentifier(identifier : String?) : Formation {
        val list = mutableListOf<DancerPosition>()

        if (identifier == null) {
            return this
        }

        when (identifier.lowercase()) {
            "heads" -> {
                dancers.forEach { dancer ->
                    if (dancer.number % 2 == 1) {
                        list.add(dancer)
                    }
                }
            }
            "sides" -> {
                dancers.forEach { dancer ->
                    if (dancer.number % 2 == 0) {
                        list.add(dancer)
                    }
                }
            }
            "larks" -> {
                dancers.forEach { dancer ->
                    if (dancer.lark) {
                        list.add(dancer)
                    }
                }
            }
            "robins" -> {
                dancers.forEach { dancer ->
                    if (!dancer.lark) {
                        list.add(dancer)
                    }
                }
            }
        }

        return Formation(list)
    }

    fun copyFrom(subFormation: Formation) {
        subFormation.dancers.forEach { dancer ->
            val id = dancer.id
            getById(id)?.moveAbs(dancer.vec)
        }
    }

    fun lerp(other: Formation, fraction: Double): Formation {

        val dancersLerp : MutableList<DancerPosition> = mutableListOf()

        dancers.forEach { pos ->
            dancersLerp.add(pos.lerp(other.getById(pos.id), fraction))
        }

        return Formation(dancersLerp)
    }

    fun copy() : Formation {
        return Formation(
            dancers.map { it.copy() }
        )
    }

    fun outTurn(id : String) : Double {
        val dancer = getById(id) ?: return 0.0
        val right = dancer.rightVec
        val diff = Vec2f(dancer.vec.x.toFloat(), dancer.vec.y.toFloat())
        val dot = diff.dot(right)

        return if (dot > 0) {
            90.0
        } else {
            -90.0
        }
    }
}