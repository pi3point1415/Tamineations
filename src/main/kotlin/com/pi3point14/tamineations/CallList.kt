package com.pi3point14.tamineations

import com.pi3point14.tamineations.calls.PairOff
import com.pi3point14.tamineations.calls.UTurnBack
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object CallList {
    val calls = mutableMapOf<String, KClass<out Call>>()

    val identifiers = listOf("heads", "sides", "larks", "robins")

    init {
        calls["u turn back"] = UTurnBack::class
        calls["pair off"] = PairOff::class
    }

    fun createCall(name: String): Call? {
        var nameLower = name.lowercase()

        var identifier : String? = null

        for (id in identifiers) {
            if (nameLower.startsWith(id)) {
                identifier = id
                nameLower = nameLower.removeRange(0, id.length + 1)
                break
            }
        }

        val kClass = calls[nameLower] ?: return null
        val constructor = kClass.primaryConstructor

        return constructor?.call(identifier)
    }
}