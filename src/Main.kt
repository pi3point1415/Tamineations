import FormationType.ROW as ROW
import FormationType.COLUMN as COLUMN

fun printFormation (formation : Formation) {
    val intCoords = formation.toIntCoordinates().dancers

    val minX = intCoords.minOf { it.x }
    val maxX = intCoords.maxOf { it.x }
    val minY = intCoords.minOf { it.y }
    val maxY = intCoords.maxOf { it.y }

    fun List<IntCoordinate>.toGridMap(): Map<Int, Map<Int, IntCoordinate>> {
        return groupBy { it.x }
            .mapValues { (_, points) -> points.associateBy { it.y } }
    }

    val points = intCoords.toGridMap()

    for (y in minY..maxY) {
        val row = (minX..maxX).joinToString("") { x ->
            val point = points[x]?.get(y)
            if (point != null) "${point.number} " else "  "
        }
        println(row)
    }
}

fun main() {

    // Diamonds

//    val d1 = Formation(1)
//    val d2 = Formation(2)
//    val d3 = Formation(3)
//    val d4 = Formation(4)
//    val d5 = Formation(5)
//    val d6 = Formation(6)
//    val d7 = Formation(7)
//    val d8 = Formation(8)
//
//    val h1 = Formation(ROW, d2, d3)
//    val v1 = Formation(COLUMN, d1, h1, d4)
//
//    val h2 = Formation(ROW, d7, d6)
//    val v2 = Formation(COLUMN, d8, h2, d5)
//
//    val formation = Formation(ROW, v1, v2)
//
//    printFormation(formation)

    // Hourglass

//    val d1 = Formation(1)
//    val d2 = Formation(2)
//    val d3 = Formation(3)
//    val d4 = Formation(4)
//    val d5 = Formation(5)
//    val d6 = Formation(6)
//    val d7 = Formation(7)
//    val d8 = Formation(8)
//
//    val v1 = Formation(COLUMN, d3, d7)
//
//    val h2 = Formation(ROW, d2, v1, d6)
//
//    val h1 = Formation(ROW, d1, d8)
//    val h3 = Formation(ROW, d4, d5)
//
//    val formation = Formation(COLUMN, h1, h2, h3)
//
//    printFormation(formation)

    // 1/4 Tag

//    val d1 = Formation(1)
//    val d2 = Formation(2)
//    val d3 = Formation(3)
//    val d4 = Formation(4)
//    val d5 = Formation(5)
//    val d6 = Formation(6)
//    val d7 = Formation(7)
//    val d8 = Formation(8)
//
//    val h1 = Formation(ROW, d1, d2)
//    val h2 = Formation(ROW, d3, d4, d8, d7)
//    val h3 = Formation(ROW, d6, d5)
//
//    val formation = Formation(COLUMN, h1, h2, h3)
//
//    printFormation(formation)

    // H

    val d1 = Formation(1)
    val d2 = Formation(2)
    val d3 = Formation(3)
    val d4 = Formation(4)
    val d5 = Formation(5)
    val d6 = Formation(6)
    val d7 = Formation(7)
    val d8 = Formation(8)

    val h1 = Formation(ROW, d1, Formation(), Formation(), d2)
    val h2 = Formation(ROW, d3, d4, d8, d7)
    val h3 = Formation(ROW, d6, Formation(), Formation(), d5)

    val formation = Formation(COLUMN, h1, h2, h3)

    printFormation(formation)
}