package com.pi3point14.tamineations

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

object Ticker {
    private val calls = mutableListOf<Call>()

    fun register() {
        ServerTickEvents.END_SERVER_TICK.register { _ ->
            val iter = calls.iterator()
            while (iter.hasNext()) {
                val anim = iter.next()
                anim.onTick()
                anim.tick++
                if (anim.tick >= anim.length) {
                    anim.end()
                    iter.remove()
                }
            }
        }
    }

    fun play(call: Call) {
        calls.add(call)
    }
}