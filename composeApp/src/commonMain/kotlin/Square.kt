object Square {
    var formation = Formation.StaticSquare

    fun reset() {
        moveTo(Formation.StaticSquare)
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