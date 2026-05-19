import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Vector (val x : Double, val y : Double) {
    constructor (x : Int, y : Int) : this(x.toDouble(), y.toDouble())
    fun rotateLeft () : Vector {
        return Vector(-y, x)
    }
    fun rotateRight() : Vector {
        return Vector(y, -x)
    }

    fun rotate(angle : Double) : Vector {
        val rad = angle * PI / 180
        return Vector(x * cos(rad) + y * sin(rad), y * cos(rad) - x * sin(rad))
    }

    operator fun plus(other : Vector) : Vector {
        return Vector(x + other.x, y + other.y)
    }

    operator fun minus(other : Vector) : Vector {
        return Vector(x - other.x, y - other.y)
    }

    operator fun times(scalar : Double) : Vector {
        return Vector(x * scalar, y * scalar)
    }

    operator fun div(scalar : Double) : Vector {
        return Vector(x / scalar, y / scalar)
    }

    fun dot(other : Vector) : Double {
        return x * other.x + y * other.y
    }

    fun cross(other : Vector) : Double {
        return x * other.y - y * other.x
    }
}