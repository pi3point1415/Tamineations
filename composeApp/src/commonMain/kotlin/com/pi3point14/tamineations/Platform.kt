package com.pi3point14.tamineations

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform