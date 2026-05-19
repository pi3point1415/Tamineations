package calls

import Formation
import Direction
import BaseCall
import Modifier
import Position

class Lead (val dir : Direction, val modifiers : List<Modifier> = listOf()) : BaseCall() {
    override fun compute(start: Formation): List<Formation> {

        if (dir != Direction.RIGHT && dir != Direction.LEFT) return listOf()

        val (subs, inactive) = filterFormation(start, Formation.Couple, modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            val beaus = sub.beaus
            val belles = sub.belles

            val beaus0 : List<Position>
            val beaus1 : List<Position>

            val belles0 : List<Position>
            val belles1 : List<Position>

            if (dir == Direction.RIGHT) {
                beaus0 = beaus.map {
                    it.move(it.facingVec / 3.0 + it.facingVec.rotateRight() * 2.0 / 3.0).rotateAngle(45.0)
                }
                beaus1 = beaus.map { it.move(it.facingVec + it.facingVec.rotateRight()).rotateRight() }

                belles0 = belles.map { it.rotateAngle(45.0) }
                belles1 = belles.map { it.rotateRight() }
            }
            else {
                beaus0 = beaus.map { it.rotateAngle(-45.0) }
                beaus1 = beaus.map { it.rotateLeft() }

                belles0 = belles.map {
                    it.move(it.facingVec / 3.0 + it.facingVec.rotateLeft() * 2.0 / 3.0).rotateAngle(-45.0)
                }
                belles1 = belles.map { it.move(it.facingVec + it.facingVec.rotateLeft()).rotateLeft() }
            }

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