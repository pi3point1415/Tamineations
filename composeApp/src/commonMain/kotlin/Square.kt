object Square {
    var formation = FormationList.StaticSquare

    fun reset() {
        moveTo(FormationList.StaticSquare)
    }

    val callbacks = mutableListOf<(Formation) -> Unit>()

    fun addMoveCallback(callback: (Formation) -> Unit) {
        callbacks.add(callback)
    }

    fun clearMoveCallbacks() {
        callbacks.clear()
    }

    fun moveTo(new: Formation) {
        formation = new

        callbacks.forEach { it(new) }
    }

    fun call(call : BaseCall) {
        call.compute(formation).forEach {
            moveTo(it)
        }
    }
}