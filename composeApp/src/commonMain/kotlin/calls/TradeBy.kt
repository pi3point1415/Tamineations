package calls

import Formation
import BaseCall
import Modifier
import Position

class TradeBy (val modifiers : List<Modifier> = listOf()) : BaseCall() {
    override fun compute(start: Formation): List<Formation> {
        // Todo: 3/4 tag
        val (subs, inactive) = filterFormation(start, FormationList.TradeBy, modifiers) ?: return listOf()

        val newPositions = List(2) { mutableListOf<Position>() }

        for (sub in subs) {
            val centers = PassThru().compute(Formation(sub.centers))
            val ends = Trade().compute(Formation(sub.ends))

            if (centers.size < 2) return listOf()
            if (ends.size < 2) return listOf()

            newPositions[0].addAll(centers[0].positions)
            newPositions[1].addAll(centers[1].positions)

            newPositions[0].addAll(ends[0].positions)
            newPositions[1].addAll(ends[1].positions)
        }

        newPositions[0].addAll(inactive)
        newPositions[1].addAll(inactive)

        return newPositions.map { Formation(it) }
    }
}