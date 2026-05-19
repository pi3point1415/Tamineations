package calls

import Formation
import BaseCall
import Modifier

class Start(val modifier : Modifier) : BaseCall() {
    fun isValid (formation: Formation) : Boolean {
        if (modifier != Modifier.SIDES && modifier != Modifier.HEADS) return false
        if (formation.subFormations(Formation.StaticSquare).size != 1) return false
        return true
    }

    override fun compute(start: Formation) : List<Formation> {
        if (!isValid(start)) return emptyList()

        return PressAhead(listOf(modifier)).compute(start)
    }
}