package calls

import Formation
import BaseCall
import Modifier
import Position

class Trade (val modifiers : List<Modifier> = listOf()) : BaseCall() {

    fun coupleTrade(sub: Formation): Pair<List<Position>, List<Position>> {
        val beaus = sub.beaus
        val belles = sub.belles

        val beaus0 = beaus.map { it.move(it.facingVec / 2.0 + it.facingVec.rotateRight() / 2.0).rotateRight() }
        val belles0 = belles.map { it.move(it.facingVec.rotateLeft() / 2.0).rotateLeft() }

        val beaus1 = beaus.map { it.move(it.facingVec.rotateRight()).rotateRight().rotateRight() }
        val belles1 = belles.map { it.move(it.facingVec.rotateLeft()).rotateLeft().rotateLeft() }

        val pos0 = beaus0 + belles0
        val pos1 = beaus1 + belles1

        return Pair(pos0, pos1)
    }

    fun rightMiniWaveTrade(sub: Formation): Pair<List<Position>, List<Position>> {
        val pos0 = sub.positions.map { it.move(it.facingVec / 4.0 + it.facingVec.rotateRight() / 2.0).rotateRight() }
        val pos1 = sub.positions.map { it.move(it.facingVec.rotateRight()).rotateRight().rotateRight() }

        return Pair(pos0, pos1)
    }

    fun leftMiniWaveTrade(sub: Formation): Pair<List<Position>, List<Position>> {
        val pos0 = sub.positions.map { it.move(it.facingVec / 4.0 + it.facingVec.rotateLeft() / 2.0).rotateLeft() }
        val pos1 = sub.positions.map { it.move(it.facingVec.rotateLeft()).rotateLeft().rotateLeft() }

        return Pair(pos0, pos1)
    }

    override fun compute(start: Formation): List<Formation> {
        val (subs, inactive) = filterFormation(start, listOf(
            FormationList.Couple, FormationList.RHMiniWave, FormationList.LHMiniWave), modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            if (sub.matches(FormationList.Couple)) {
                val (pos1, pos2) = coupleTrade(sub)

                newPositions[0].addAll(pos1)
                newPositions[1].addAll(pos2)
            }
            else if (sub.matches(FormationList.RHMiniWave)) {
                val (pos1, pos2) = rightMiniWaveTrade(sub)

                newPositions[0].addAll(pos1)
                newPositions[1].addAll(pos2)

            }
            else if (sub.matches(FormationList.LHMiniWave)) {
                val (pos1, pos2) = leftMiniWaveTrade(sub)

                newPositions[0].addAll(pos1)
                newPositions[1].addAll(pos2)
            }
        }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }
}