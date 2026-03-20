package com.pi3point14.tamineations

import com.mojang.brigadier.arguments.FloatArgumentType.floatArg
import com.mojang.brigadier.arguments.FloatArgumentType.getFloat
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object SquareDanceCommands {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                literal("square")
                    .then(literal("setup")
                        .executes { ctx -> setupSquare(ctx) }
                    )
                    .then(literal("move")
                        .then(argument<String>("dancer", word())
                        .then(argument<Float>("x", floatArg())
                        .then(argument<Float>("y", floatArg())
                        .then(argument<Float>("z", floatArg())
                        .then(argument<Float>("yaw", floatArg())
                            .executes { ctx -> moveDancer(ctx) }
                        )))))
                    )
            )
        }
    }

    private fun setupSquare(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val world = source.world
        val center = source.position

        spawnSquare(world, center)
        source.sendFeedback({ Text.literal("Square formed!") }, false)
        return 1
    }

    private fun moveDancer(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val world = source.world

        val dancerName = getString(ctx, "dancer")
        var x = getFloat(ctx, "x").toDouble()
        var y = getFloat(ctx, "y").toDouble()
        var z = getFloat(ctx, "z").toDouble()
        val yaw = getFloat(ctx, "yaw")

        val uuid = SquareState.dancers[dancerName]
        if (uuid == null) {
            source.sendFeedback({ Text.literal("No dancer found with name '$dancerName'!") }, false)
            return 0
        }

        val entity = world.getEntity(uuid)
        if (entity == null) {
            source.sendFeedback(
                { Text.literal("Dancer '$dancerName' exists in state but not in world — did you change dimensions?") },
                false
            )
            return 0
        }

        x += SquareState.center.x
        y += SquareState.center.y
        z += SquareState.center.z

        entity.refreshPositionAndAngles(x, y, z, yaw, 0f)
        entity.yaw = yaw
        entity.headYaw = yaw
        entity.bodyYaw = yaw

        source.sendFeedback({ Text.literal("Moved $dancerName to ($x, $y, $z) facing $yaw°") }, false)
        return 1
    }
}