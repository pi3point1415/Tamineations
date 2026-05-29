object FormationList {
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

    val formations = listOf(
        StaticSquare,
        Couple,
        RHMiniWave,
        LHMiniWave,
        Facing,
        TradeBy,
    )
}