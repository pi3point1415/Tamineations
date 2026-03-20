package com.pi3point14.tamineations

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity

object SquareDanceCommands {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                literal("square")
                    .then(
                        literal("setup")
                            .executes { ctx -> setupSquare(ctx) }
                    )
            )
        }
    }

    private fun setupSquare(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val world = source.world
        val center = source.position

        spawnSquare(world, center)
        source.sendFeedback({ net.minecraft.text.Text.literal("Square formed!") }, false)
        return 1
    }
}