package calls

import Formation
import BaseCall
import Modifier
import Position

class PassThru (val modifiers : List<Modifier> = listOf()) : BaseCall() {
    override fun compute(start: Formation): List<Formation> {
        // Todo: ocean wave rule
        val (subs, inactive) = filterFormation(start, Formation.Facing, modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            val pos1 = sub.positions.map { it.move(it.facingVec / 2.0 + it.facingVec.rotateLeft() / 4.0) }
            val pos2 = sub.positions.map { it.move(it.facingVec) }

            newPositions[0].addAll(pos1)
            newPositions[1].addAll(pos2)
        }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }
}