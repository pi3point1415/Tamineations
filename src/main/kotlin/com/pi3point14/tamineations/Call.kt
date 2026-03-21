package com.pi3point14.tamineations

abstract class Call {
    abstract val length: Int
    var tick = 0

    abstract fun onTick ()

    abstract fun end()

    open fun callback (): String? {
        return null
    }
}