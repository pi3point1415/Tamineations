package com.pi3point14.tamineations

import kotlin.math.sqrt

class Vec2d (val x: Double, val y: Double) {
    fun multiply (value : Double) : Vec2d {
        return Vec2d(x * value, y * value)
    }

    fun dot (vec : Vec2d) : Double {
        return x * vec.x + y * vec.y
    }

    fun add (vec : Vec2d) : Vec2d {
        return Vec2d(x + vec.x, y + vec.y)
    }

    fun add (value : Double) : Vec2d {
        return Vec2d(x + value, y + value)
    }

    fun equals(other : Vec2d) : Boolean {
        return x == other.x && y == other.y
    }

    fun normalize () : Vec2d {
        val f = sqrt(x * x + y * y)
        return if (f < 1.0e-4) Vec2d(0.0, 0.0) else Vec2d(x / f, y / f)
    }

    fun length () : Double {
        return sqrt(x * x + y * y)
    }

    fun negate () : Vec2d {
        return Vec2d(-x, -y)
    }
}