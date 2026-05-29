package calls

import Formation
import BaseCall
import Modifier
import Position

class Hinge (val modifiers : List<Modifier> = listOf()) : BaseCall() {

    fun coupleHinge(sub: Formation): List<Position> {
        val beaus = sub.beaus.map { it.move(it.facingVec / 2.0 + it.facingVec.rotateRight() / 2.0).rotateRight() }
        val belles = sub.belles.map { it.move(it.facingVec.rotateLeft() / 2.0).rotateLeft() }

        return beaus + belles
    }

    fun rightMiniWaveHinge(sub: Formation): List<Position> {
        return sub.positions.map { it.move(it.facingVec / 4.0 + it.facingVec.rotateRight() / 2.0).rotateRight() }
    }

    fun leftMiniWaveHinge(sub: Formation): List<Position> {
        return sub.positions.map { it.move(it.facingVec / 4.0 + it.facingVec.rotateLeft() / 2.0).rotateLeft() }
    }

    override fun compute(start: Formation): List<Formation> {
        val (subs, inactive) = filterFormation(start, listOf(
            FormationList.Couple, FormationList.RHMiniWave, FormationList.LHMiniWave), modifiers) ?: return listOf()

        val newPositions = List(1) { mutableListOf<Position>() }

        for (sub in subs) {
            if (sub.matches(FormationList.Couple)) {
                newPositions[0].addAll(coupleHinge(sub))
            }
            else if (sub.matches(FormationList.RHMiniWave)) {
                newPositions[0].addAll(rightMiniWaveHinge(sub))
            }
            else if (sub.matches(FormationList.LHMiniWave)) {
                newPositions[0].addAll(leftMiniWaveHinge(sub))
            }
        }

        newPositions[0].addAll(inactive)

        val formation = newPositions.map { Formation(it) }

        return formation.map{ it.rescale() }
    }
}