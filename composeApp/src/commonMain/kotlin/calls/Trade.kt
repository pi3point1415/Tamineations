package calls

import Formation
import BaseCall
import Modifier
import Position

class Trade (val modifiers : List<Modifier> = listOf()) : BaseCall() {

    fun coupleTrade(start: Formation): List<Formation> {
        val (subs, inactive) = filterFormation(start, Formation.Couple, modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            val beaus = sub.beaus
            val belles = sub.belles

            val beaus0 = beaus.map { it.move(it.facingVec / 2.0 + it.facingVec.rotateRight() / 2.0).rotateRight() }
            val belles0 = belles.map { it.move(it.facingVec.rotateLeft() / 2.0).rotateLeft() }

            val beaus1 = beaus.map { it.move(it.facingVec.rotateRight()).rotateRight().rotateRight() }
            val belles1 = belles.map { it.move(it.facingVec.rotateLeft()).rotateLeft().rotateLeft() }

            newPositions[0].addAll(belles0)
            newPositions[0].addAll(beaus0)

            newPositions[1].addAll(belles1)
            newPositions[1].addAll(beaus1)
        }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }

    fun rightMiniWaveTrade(start: Formation): List<Formation> {
        val (subs, inactive) = filterFormation(start, Formation.RHMiniWave, modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            val pos1 = sub.positions.map { it.move(it.facingVec / 4.0 + it.facingVec.rotateRight() / 2.0).rotateRight() }
            val pos2 = sub.positions.map { it.move(it.facingVec.rotateRight()).rotateRight().rotateRight() }

            newPositions[0].addAll(pos1)
            newPositions[1].addAll(pos2)
        }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }

    fun leftMiniWaveTrade(start: Formation): List<Formation> {
        val (subs, inactive) = filterFormation(start, Formation.LHMiniWave, modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            val pos1 = sub.positions.map { it.move(it.facingVec / 4.0 + it.facingVec.rotateLeft() / 2.0).rotateLeft() }
            val pos2 = sub.positions.map { it.move(it.facingVec.rotateLeft()).rotateLeft().rotateLeft() }

            newPositions[0].addAll(pos1)
            newPositions[1].addAll(pos2)
        }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }

    override fun compute(start: Formation): List<Formation> {
        //TODO: this doesn't work if some dancers are in a couple and some are in a mini wave
        // Probably I should make it so that it matches any 2x1 and then does the appropriate thing
        // which will require updating the way the matching works

        // Couple
        var result = coupleTrade(start)
        if (result.isNotEmpty()) {
            return result
        }

        result = rightMiniWaveTrade(start)
        if (result.isNotEmpty()) {
            return result
        }

        // RH Mini wave
        return leftMiniWaveTrade(start)
    }
}