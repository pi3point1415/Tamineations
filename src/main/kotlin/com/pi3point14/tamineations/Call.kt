package com.pi3point14.tamineations

abstract class Call (identifier: String?) {

    val lengthTicks: Int
        get () {
            return (length * Ticker.TICKS_PER_BEAT).toInt()
        }

    var tick = 0

    abstract val length: Double

    val startFormation : Formation = SquareState.getFormation()

    var currentFormation : Formation = startFormation.copy()

    val endFormation : Formation = startFormation.copy()

    val activeStartFormation : Formation = startFormation.filterByIdentifier(identifier)

    var activeCurrentFormation : Formation = activeStartFormation.copy()

    val activeEndFormation : Formation = activeStartFormation.copy()

    abstract fun isLegal () : Boolean

    abstract fun onTick ()

    abstract fun end()

    open fun callback () : String? {
        return null
    }
}