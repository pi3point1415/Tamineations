enum class Facing {
    N,
    E,
    S,
    W,
}

enum class Gender {
    BOY,
    GIRL,
}

enum class Side {
    HEAD,
    SIDE,
}

open class Position(val x: Int, val y: Int, val facing: Facing?, val gender: Gender?, val side: Side?, val number: Int?) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

class AbstractPosition(x : Int, y : Int) : Position(x, y, null, null, null, null)

open class AbstractFormation (open val positions: List<Position>) {
    val facingSymbol = mapOf(
        Facing.N to "^",
        Facing.E to ">",
        Facing.S to "V",
        Facing.W to "<",
        null to '.'
    )

    fun dancerAt(x: Int, y: Int) : Position? {
        for (pos in positions) {
            if (pos.x == x && pos.y == y) {
                return pos
            }
        }

        return null
    }

    fun grid(): List<MutableList<Position?>> {
        val minX = positions.minBy { it.x }.x
        val minY = positions.minBy { it.y }.y
        val maxX = positions.maxBy { it.x }.x
        val maxY = positions.maxBy { it.y }.y

        val coords = List(maxY - minY + 1) { y -> MutableList(maxX - minX + 1) { x -> dancerAt(x + minX, y + minY) } }

        return coords
    }

    override fun toString (): String {
        val grid = grid()

        val lines = grid.map { line -> line.map{ pos -> if (pos != null) 'x' else '.' }.joinToString(separator = " ") }
        return lines.joinToString("\n")
    }

    fun subFormations(formation: AbstractFormation): List<AbstractFormation> {
        val formations = mutableListOf<AbstractFormation>()

        for (base in positions) {
            val offsetX = base.x - formation.positions[0].x
            val offsetY = base.y - formation.positions[0].y

            val matches = mutableListOf(base)

            for (pos in formation.positions.subList(1, formation.positions.size)) {
                val match = dancerAt(pos.x + offsetX, pos.y + offsetY)
                if (match != null) {
                    matches.add(match)
                }
                else {
                    break
                }
            }

            if (matches.size == formation.positions.size) {
                formations.add(AbstractFormation(matches))
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

    fun disjointSubFormations(formation: AbstractFormation): List<AbstractFormation> {
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

        return bestSubset.map { AbstractFormation(it.toList()) }
    }

    fun toFormation(): Formation {
        return Formation(positions)
    }
}

class Formation (positions: List<Position>) : AbstractFormation(positions) {
    override fun toString (): String {
        val grid = grid()

        val lines = grid.map { line -> line.map{ pos -> if (pos != null) facingSymbol[pos.facing] else '.' }.joinToString(separator = " ") }
        return lines.joinToString("\n")
    }
}
