package calls

import Formation
import BaseCall
import Modifier
import Position

class Trade (val modifiers : List<Modifier>) : BaseCall() {
    override fun compute(start: Formation): List<Formation> {
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
}