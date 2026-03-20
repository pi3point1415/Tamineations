package com.pi3point14.tamineations

import net.fabricmc.api.ModInitializer

class Tamineations : ModInitializer {

    override fun onInitialize() {
        SquareDanceCommands.register()
    }
}
