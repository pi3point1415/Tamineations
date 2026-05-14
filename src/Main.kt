fun main() {
    val base = AbstractFormation(
        listOf(
            AbstractPosition(0, 0),
            AbstractPosition(2, 0),
            AbstractPosition(2, 2),
            AbstractPosition(0, 2),
        )
    )

    println("================")
    println(base.toString())
    println("================")

    val complex = Formation(
        listOf(
            Position(0, 0, Facing.N, Gender.BOY, Side.HEAD, 1),
            Position(2, 0, Facing.N, Gender.GIRL, Side.HEAD, 2),
            Position(4, 0, Facing.S, Gender.GIRL, Side.HEAD, 2),
            Position(6, 0, Facing.S, Gender.GIRL, Side.HEAD, 2),
            Position(0, 2, Facing.N, Gender.BOY, Side.HEAD, 1),
            Position(2, 2, Facing.N, Gender.GIRL, Side.HEAD, 2),
            Position(4, 2, Facing.S, Gender.GIRL, Side.HEAD, 2),
            Position(6, 2, Facing.S, Gender.GIRL, Side.HEAD, 2),
        )
    )

    println("================")
    println(complex.toString())
    println("================")

    for (match in complex.disjointSubFormations(base)) {
        println("================")
        println(match.toFormation().toString())
        println("================")
    }


//    val facing = Formation(
//        listOf(
//            Position(0, 0, Facing.N, Gender.BOY, Side.HEAD, 1),
//            Position(2, 0, Facing.E, Gender.GIRL, Side.HEAD, 2),
//            Position(2, 2, Facing.S, Gender.BOY, Side.SIDE, 3),
//            Position(0, 2, Facing.W, Gender.GIRL, Side.SIDE, 4),
//        )
//    )
//
//    println("================")
//    println(facing.toString())
//    println("================")
}