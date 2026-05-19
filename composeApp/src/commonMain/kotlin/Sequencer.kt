import calls.*

object Sequencer {
    val calls = listOf(
        Start(Modifier.SIDES),
        Lead(Direction.RIGHT, listOf(Modifier.SIDES)),
        PassThru(),
        TradeBy(),
        TouchAQuarter(),
        Trade(),
        UTurnBack(),
        Trade(),
    )
    var index = 0

    fun next() {
        if (index >= calls.size) {
            Square.reset()
            index = 0
        }
        else {
            Square.call(calls[index])
            index++
        }
    }
}