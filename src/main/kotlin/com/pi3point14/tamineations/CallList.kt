package com.pi3point14.tamineations

import com.pi3point14.tamineations.calls.UTurnBack
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object CallList {
    val calls = mutableMapOf<String, KClass<out Call>>()

    init {
        calls["u turn back"] = UTurnBack::class
    }

    fun createCall(type: String): Call? {
        val kClass = calls[type.lowercase()] ?: return null
        val constructor = kClass.primaryConstructor
        return constructor?.call()
    }
}