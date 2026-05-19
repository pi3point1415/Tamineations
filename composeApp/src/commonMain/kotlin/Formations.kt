import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

enum class Gender {
    BOY,
    GIRL,
}

enum class Side {
    HEAD,
    SIDE,
}

enum class Center {
    CENTER,
    END,
}

enum class Beau {
    BEAU,
    BELLE,
}

data class Position (
    val pos : Vector,
    val facing: Double? = null,
    val gender: Gender? = null,
    val side: Side? = null,
    val number: Int? = null,
    val center: Center? = null,
    val beau: Beau? = null,
) {
    constructor (
        x : Double,
        y : Double,
        facing: Double? = null,
        gender: Gender? = null,
        side: Side? = null,
        number: Int? = null,
        center: Center? = null,
        beau: Beau? = null,
    ) : this(Vector(x, y), facing, gender, side, number, center, beau)

    companion object {
        const val EPSILON = 1e-6

        fun angleEqual(a : Double?, b : Double?) : Boolean {
            if (a == null || b == null) return true
            val diff = (a - b + 180).mod(360.0) - 180.0
            return abs(diff) < EPSILON
        }
    }

    val x : Double
        get() = pos.x

    val y : Double
        get() = pos.y

    override fun toString(): String {
        return "($x, $y)"
    }

    fun move(dir : Vector) : Position {
        return Position(
            pos + dir,
            facing,
            gender,
            side,
            number,
            center,
            beau,
        )
    }

    fun rotateAngle(angle : Double) : Position {
        val newFacing = if (facing != null) (facing + angle).mod(360.0) else null
        return Position(
            x,
            y,
            newFacing,
            gender,
            side,
            number,
            center,
            beau,
        )
    }

    fun rotateRight() : Position {
        return rotateAngle(90.0)
    }

    fun rotateLeft() : Position {
        return rotateAngle(-90.0)
    }

    val facingVec : Vector
        get() {
            if (facing == null) {
                return Vector(0.0, 0.0)
            }
            val angleRad = facing * PI / 180
            return Vector(sin(angleRad), cos(angleRad))
        }
}

class Formation (val positions: List<Position>) {
    companion object {
        val StaticSquare = Formation(
            listOf(
                Position(-0.5, -1.5, 0.0, Gender.BOY, Side.HEAD, 1),
                Position(0.5, -1.5, 0.0, Gender.GIRL, Side.HEAD, 1),
                Position(1.5, -0.5, 270.0, Gender.BOY, Side.SIDE, 2),
                Position(1.5, 0.5, 270.0, Gender.GIRL, Side.SIDE, 2),
                Position(0.5, 1.5, 180.0, Gender.BOY, Side.HEAD, 3),
                Position(-0.5, 1.5, 180.0, Gender.GIRL, Side.HEAD, 3),
                Position(-1.5, 0.5, 90.0, Gender.BOY, Side.SIDE, 4),
                Position(-1.5, -0.5, 90.0, Gender.GIRL, Side.SIDE, 4),
            )
        )

        val Couple = Formation(
            listOf(
                Position(-0.5, 0.0, 0.0, beau=Beau.BEAU),
                Position(0.5, 0.0, 0.0, beau=Beau.BELLE),
            )
        )

        val RHMiniWave = Formation(
            listOf(
                Position(-0.5, 0.0, 0.0, beau=Beau.BEAU),
                Position(0.5, 0.0, 180.0, beau=Beau.BEAU),
            )
        )

        val LHMiniWave = Formation(
            listOf(
                Position(-0.5, 0.0, 180.0, beau=Beau.BELLE),
                Position(0.5, 0.0, 0.0, beau=Beau.BELLE),
            )
        )

        val Facing = Formation(
            listOf(
                Position(0.0, -0.5, 0.0),
                Position(0.0, 0.5, 180.0)
            )
        )

        val TradeBy = Formation(
            listOf(
                Position(-0.5, -1.5, 180.0, center = Center.END, beau = Beau.BELLE),
                Position(0.5, -1.5, 180.0, center = Center.END, beau = Beau.BEAU),
                Position(-0.5, -0.5, 0.0, center = Center.CENTER, beau = Beau.BEAU),
                Position(0.5, -0.5, 0.0, center = Center.CENTER, beau = Beau.BELLE),
                Position(-0.5, 0.5, 180.0, center = Center.CENTER, beau = Beau.BELLE),
                Position(0.5, 0.5, 180.0, center = Center.CENTER, beau = Beau.BEAU),
                Position(-0.5, 1.5, 0.0, center = Center.END, beau = Beau.BEAU),
                Position(0.5, 1.5, 0.0, center = Center.END, beau = Beau.BELLE),
            )
        )
    }

    fun rotate(angle : Double) : Formation {
        val newPositions = positions.map {
            val pos = it.pos.minus(center).rotate(angle).plus(center)
            Position(
                pos,
                it.facing?.plus(angle),
                it.gender,
                it.side,
                it.number,
                it.center,
                it.beau,
            )
        }

        return Formation(newPositions)
    }

    fun getDancer(number: Int?, gender: Gender?) : Position? {
        for (pos in positions) {
            if (pos.number == number && pos.gender == gender) {
                return pos
            }
        }
        return null
    }

    fun dancerAt(x: Double, y: Double) : Position? {
        for (pos in positions) {
            if (abs(pos.x - x) < Position.EPSILON && abs(pos.y - y) < Position.EPSILON) {
                return pos
            }
        }

        return null
    }

    val center : Vector
        get () {
            val minX = positions.minBy { it.x }.x
            val minY = positions.minBy { it.y }.y
            val maxX = positions.maxBy { it.x }.x
            val maxY = positions.maxBy { it.y }.y

            return Vector((minX + maxX) / 2.0, (minY + maxY) / 2.0)
        }

    val beaus : List<Position> get() {
        return positions.filter{ it.beau == Beau.BEAU }
    }

    val belles : List<Position> get() {
        return positions.filter{ it.beau == Beau.BELLE }
    }

    val centers : List<Position> get() {
        return positions.filter{ it.center == Center.CENTER }
    }

    val ends : List<Position> get() {
        return positions.filter{ it.center == Center.END }
    }

    fun subFormations(reference: Formation): List<Formation> {
        val formations = mutableListOf<Formation>()

        for (angle in 0..<360 step 45) {
            val rotated = reference.rotate(angle.toDouble())
            for (base in positions) {
                if (!Position.angleEqual(rotated.positions[0].facing, base.facing)) continue
                if (rotated.positions[0].gender != base.gender && (rotated.positions[0].gender != null && base.gender != null)) continue
                if (rotated.positions[0].side != base.side && (rotated.positions[0].side != null && base.side != null)) continue
                if (rotated.positions[0].number != base.number && (rotated.positions[0].number != null && base.number != null)) continue

                val offsetX = base.x - rotated.positions[0].x
                val offsetY = base.y - rotated.positions[0].y

                val matches = mutableListOf(Position(
                    base.pos,
                    base.facing,
                    base.gender,
                    base.side,
                    base.number,
                    rotated.positions[0].center,
                    rotated.positions[0].beau))

                for (pos in rotated.positions.subList(1, rotated.positions.size)) {
                    val match = dancerAt(pos.x + offsetX, pos.y + offsetY)
                    if (match != null) {
                        if (!Position.angleEqual(pos.facing, match.facing)) break
                        if (pos.gender != match.gender && (pos.gender != null && match.gender != null)) break
                        if (pos.side != match.side && (pos.side != null && match.side != null)) break
                        if (pos.number != match.number && (pos.number != null && match.number != null)) break
                        matches.add(Position(
                            match.pos,
                            match.facing,
                            match.gender,
                            match.side,
                            match.number,
                            pos.center,
                            pos.beau,
                        ))
                    } else {
                        break
                    }
                }

                if (matches.size == rotated.positions.size) {
                    formations.add(Formation(matches))
                }
            }
        }

        return formations
    }

    fun setsAreDisjoint(sets: List<Set<Position>>): Boolean {
        val seen = mutableSetOf<Position>()
        for (set in sets) {
            for (element in set) {
                if (!seen.add(element)) return false
            }
        }
        return true
    }

    fun disjointSubFormations(formation: Formation): List<Formation> {
        val formations = subFormations(formation)

        val sets = formations.map { it.positions.toSet() }

        val n = sets.size
        var bestSubset = emptyList<Set<Position>>()

        for (mask in 0..<(1 shl n)) {
            val candidate = mutableListOf<Set<Position>>()

            for (i in 0..<n) {
                if (mask and (1 shl i) != 0) {
                    candidate.add(sets[i])
                }
            }

            if (setsAreDisjoint(candidate) && candidate.size > bestSubset.size) {
                bestSubset = candidate.toList()
            }
        }

        return bestSubset.map { Formation(it.toList()) }
    }

    fun filterBy(modifiers : List<Modifier>) : Formation {
        var newPositions = positions
        for (modifier in modifiers) {
            newPositions = when (modifier) {
                Modifier.HEADS -> newPositions.filter { it.side == Side.HEAD }
                Modifier.SIDES -> newPositions.filter { it.side == Side.SIDE }
                Modifier.BOYS -> newPositions.filter { it.gender == Gender.BOY }
                Modifier.GIRLS -> newPositions.filter { it.gender == Gender.GIRL }
            }
        }
        return Formation(newPositions)
    }
}
