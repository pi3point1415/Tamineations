import kotlin.math.roundToInt

enum class FormationType{
    EMPTY,
    DANCER,
    ROW,
    COLUMN,
}

data class Coordinate(val x : Double, val y : Double, val number: Int)

data class IntCoordinate(val x : Int, val y : Int, val number: Int)

data class FormationCoordinates (val dancers : List<Coordinate>, val xbb : Int, val ybb : Int)

data class IntFormationCoordinates (val dancers : List<IntCoordinate>, val xbb : Int, val ybb : Int)

class Formation (var type: FormationType, val children: MutableList<Formation>, val number : Int?) {

    constructor () : this(FormationType.EMPTY, mutableListOf(), null)
    constructor (number: Int?) : this(FormationType.DANCER, mutableListOf(), number)
    constructor (type: FormationType, children: MutableList<Formation>) : this(type, children, null)
    constructor (type: FormationType, vararg children: Formation) : this(type, children.toMutableList())
    constructor (type: FormationType, vararg children: Int) : this(type, children.map { Formation(it) }.toMutableList())

    fun flatten() {
        if (type == FormationType.EMPTY) return
        if (type == FormationType.DANCER) return

        children.forEach { child ->
            child.flatten()

            if (type == child.type) {
                children.addAll(child.children)
                children.remove(child)
            }
        }
    }

    fun toCoordinates () : FormationCoordinates {
        when (type) {
            FormationType.EMPTY -> return FormationCoordinates(listOf(Coordinate(0.0, 0.0, 0)), 1, 1)
            FormationType.DANCER -> return FormationCoordinates(listOf(
                Coordinate(0.0, 0.0, number ?: 0)), 1, 1
            )
            FormationType.ROW -> {
                val boxes = children.map { it.toCoordinates() }
                val updatedCoordinates = mutableListOf<Coordinate>()

                var count = 0
                boxes.forEach { box ->
                    val newCoords = mutableListOf<Coordinate>()
                    box.dancers.forEach { dancer ->
                        newCoords.add(Coordinate(dancer.x + count, dancer.y - (box.ybb.toDouble() - 1) / 2, dancer.number))
                    }
                    count += box.xbb

                    updatedCoordinates.addAll(newCoords)
                }

                val minY = updatedCoordinates.minBy { it.y }.y

                val finalCoordinates = updatedCoordinates.map { Coordinate(it.x, it.y - minY, it.number) }

                val ybb = boxes.maxBy { it.ybb }.ybb

                return FormationCoordinates(finalCoordinates, count, ybb)
            }
            FormationType.COLUMN -> {
                val boxes = children.map { it.toCoordinates() }
                val updatedCoordinates = mutableListOf<Coordinate>()

                var count = 0
                boxes.forEach { box ->
                    val newCoords = mutableListOf<Coordinate>()
                    box.dancers.forEach { dancer ->
                        newCoords.add(Coordinate(dancer.x - (box.xbb.toDouble() - 1) / 2, dancer.y + count, dancer.number))
                    }
                    count += box.ybb

                    updatedCoordinates.addAll(newCoords)
                }

                val minX = updatedCoordinates.minBy { it.x }.x

                val finalCoordinates = updatedCoordinates.map { Coordinate(it.x - minX, it.y, it.number) }

                val xbb = boxes.maxBy { it.xbb }.xbb

                return FormationCoordinates(finalCoordinates, xbb, count)
            }
        }
    }

    fun toIntCoordinates () : IntFormationCoordinates {
        val coords = toCoordinates()

        val factor = 12

        val scaledCoords = coords.dancers.map {
            IntCoordinate((it.x * factor).roundToInt(), (it.y * factor).roundToInt(), it.number)
        }

        tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

        val minX = scaledCoords.minOf { it.x }
        val minY = scaledCoords.minOf { it.y }

        val shiftedCoords = scaledCoords.map { IntCoordinate(it.x - minX, it.y - minY, it.number) }

        val allValues = shiftedCoords.flatMap {listOf(it.x, it.y) }.filter { it != 0 }

        val gcd = if (allValues.isEmpty()) 1 else allValues.reduce { a, b -> gcd(a, b) }

        val reducedCoords = shiftedCoords.map { IntCoordinate(it.x / gcd, it.y / gcd, it.number) }

        val xbb = reducedCoords.maxOf { it.x }
        val ybb = reducedCoords.maxOf { it.y }

        return IntFormationCoordinates(reducedCoords, xbb, ybb)
    }

    private fun optionalCombinations(lists: List<List<Formation>>): List<List<Formation>> {
        if (lists.isEmpty()) return listOf(emptyList())

        val rest = optionalCombinations(lists.drop(1))

        return rest.map { listOf(Formation()) + it } + lists.first().flatMap { element ->
            rest.map { combo -> listOf(element) + combo }
        }
    }

    fun getSubsets() : List<Formation> {
        when (type) {
            FormationType.EMPTY -> return listOf(Formation())
            FormationType.DANCER -> return listOf(Formation(number))
            FormationType.ROW, FormationType.COLUMN -> {
                val subsets = children.map { it.getSubsets() }
                val combinations = optionalCombinations(subsets)
                return combinations.map {
                    if (it.isEmpty()) {
                        Formation()
                    }
                    else if (it.size == 1) {
                        Formation(it[0].number)
                    }
                    else {
                        Formation(type, it.toMutableList())
                    }
                }
            }
        }
    }
}