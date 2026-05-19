package calls

import Formation
import BaseCall
import Modifier
import Position

class UTurnBack (val modifiers : List<Modifier> = listOf()) : BaseCall() {
    override fun compute(start: Formation): List<Formation> {
        // Todo: actually correct rotation direction
        val active = start.filterBy(modifiers)

        val newPositions = List(2) { mutableListOf<Position>() }

        val pos1 = active.positions.map { it.rotateRight() }
        val pos2 = active.positions.map { it.rotateRight().rotateRight() }

        newPositions[0].addAll(pos1)
        newPositions[1].addAll(pos2)

        val inactive = start.positions.filter { !active.positions.contains(it) }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }
}