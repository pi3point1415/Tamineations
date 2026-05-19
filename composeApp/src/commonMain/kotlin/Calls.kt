enum class Modifier {
    HEADS,
    SIDES,
    BOYS,
    GIRLS,
}

enum class Direction {
    LEFT,
    RIGHT,
}

abstract class BaseCall {
    abstract fun compute(start: Formation) : List<Formation>

    fun filterFormation(formation: Formation, filter: Formation, modifiers: List<Modifier>): Pair<List<Formation>, List<Position>>? {
        val active = formation.filterBy(modifiers)

        val subs = active.disjointSubFormations(filter)

        var sum = 0
        for (sub in subs) {
            sum += sub.positions.size
        }

        if (sum != active.positions.size) return null

        val inactive = formation.positions.filter { !active.positions.contains(it) }

        return Pair(subs, inactive)
    }
}