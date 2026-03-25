package com.pi3point14.tamineations.calls

import com.pi3point14.tamineations.Call
import com.pi3point14.tamineations.SquareState

class PairOff(identifier: String?) : Call(identifier) {
    override val length = 4.0

    var legal = false

    init {
        if (startFormation.matchesFormation("static square")) {
            if (identifier == "heads" || identifier == "sides") {
                legal = true
            }
        }

        if (legal) {
            activeEndFormation.dancers.forEach { dancer ->
                val turn = startFormation.outTurn(dancer.id)
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