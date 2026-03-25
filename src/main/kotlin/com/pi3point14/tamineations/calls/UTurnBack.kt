package com.pi3point14.tamineations.calls

import com.pi3point14.tamineations.Call
import com.pi3point14.tamineations.SquareState

class UTurnBack(identifier: String?) : Call(identifier) {
    override val length = 2.0

    init {
        activeEndFormation.dancers.forEach { dancer ->
            dancer.turn(180.0)
        }

        endFormation.copyFrom(activeEndFormation)
    }

    override fun isLegal() : Boolean {
        return true
    }

    override fun onTick() {
        activeCurrentFormation = activeStartFormation.lerp(activeEndFormation, tick.toDouble() / lengthTicks.toDouble())
        currentFormation.copyFrom(activeCurrentFormation)

        SquareState.setFormation(currentFormation)
    }

    override fun end() {
        SquareState.setFormation(endFormation)

        println("Static square: ${endFormation.matchesFormation("static square")}")
        println("Eight chain: ${endFormation.matchesFormation("eight chain")}")
    }

    override fun callback(): String {
        return "U turn back!"
    }
}