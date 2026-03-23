package com.pi3point14.tamineations.calls

import com.pi3point14.tamineations.Call
import com.pi3point14.tamineations.SquareState
import com.pi3point14.tamineations.formations.StaticSquare

class PairOff(identifier: String?) : Call(identifier) {
    override val length = 4f

    var legal = false

    init {
        if (StaticSquare(startFormation.dancers).isValid()) {
            if (identifier == "heads" || identifier == "sides") {
                legal = true
            }
        }

        if (legal) {
            val staticSquare = StaticSquare(startFormation.dancers)
            activeEndFormation.dancers.forEach { dancer ->
                val turn = staticSquare.outTurn(dancer.id)
                dancer.moveForward(2.0)
                dancer.turn(turn)
            }
        }

        endFormation.copyFrom(activeEndFormation)
    }

    override fun isLegal() : Boolean {
        return legal
    }

    override fun onTick() {
        activeCurrentFormation = activeStartFormation.lerp(activeEndFormation, tick.toDouble() / lengthTicks.toDouble())
        currentFormation.copyFrom(activeCurrentFormation)

        SquareState.setFormation(currentFormation)
    }

    override fun end() {
        SquareState.setFormation(endFormation)
    }
}