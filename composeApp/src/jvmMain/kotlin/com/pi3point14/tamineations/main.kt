package com.pi3point14.tamineations

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "tamineations",
    ) {
        App()
    }
}