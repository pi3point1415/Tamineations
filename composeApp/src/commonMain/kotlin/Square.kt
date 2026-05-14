object Square {
    val formation = Formation(
        listOf(
            Position(-1, 3, Facing.N, Gender.BOY, Side.HEAD, 1),
            Position(1, 3, Facing.N, Gender.GIRL, Side.HEAD, 1),
            Position(3, 1, Facing.W, Gender.BOY, Side.SIDE, 2),
            Position(3, -1, Facing.W, Gender.GIRL, Side.SIDE, 2),
            Position(1, -3, Facing.S, Gender.BOY, Side.HEAD, 3),
            Position(-1, -3, Facing.S, Gender.GIRL, Side.HEAD, 3),
            Position(-3, -1, Facing.E, Gender.BOY, Side.SIDE, 4),
            Position(-3, 1, Facing.E, Gender.GIRL, Side.SIDE, 4),
            Position(0, 0, null, null, null, null),
        )
    )
}