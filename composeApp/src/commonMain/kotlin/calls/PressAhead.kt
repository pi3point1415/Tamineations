package calls

import Formation
import BaseCall
import Modifier
import Position

class PressAhead (val modifiers : List<Modifier>): BaseCall() {

    override fun compute(start: Formation) : List<Formation> {
        // Todo: handle crashing

        val active = start.filterBy(modifiers)
        val newPositions = start.positions.map { pos ->
            if (active.positions.contains(pos)) {
                Position(
                    pos.x + pos.facingVec.x,
                    pos.y + pos.facingVec.y,
                    pos.facing,
                    pos.gender,
                    pos.side,
                    pos.number
                )
            }
            else {
                pos
            }
        }
        return listOf(Formation(newPositions))
    }
}