package com.pi3point14.tamineations

import com.mojang.brigadier.arguments.FloatArgumentType.floatArg
import com.mojang.brigadier.arguments.FloatArgumentType.getFloat
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object Commands {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                literal("square")
//                    .then(literal("test")
//                        .executes { ctx ->
//                            val formation = FormationManager.toFormation("static square") ?: return@executes 0
//                            SquareState.setFormation(formation)
//
//                            return@executes 1
//                        }
//                    )
                    .then(literal("setup")
                        .executes { ctx -> setupSquare(ctx) }
                    )
                    .then(literal("move")
                        .then(argument<String>("dancer", word())
                            .then(argument<Float>("x", floatArg())
                                .then(argument<Float>("z", floatArg())
                                    .then(argument<Float>("yaw", floatArg())
                                        .executes { ctx -> moveDancer(ctx) }
                                    )
                                )
                            )
                        )
                    )
                    .then(literal("moveRel")
                        .then(argument<String>("dancer", word())
                            .then(argument<Float>("forward", floatArg())
                                .then(argument<Float>("right", floatArg())
                                    .then(argument<Float>("turn", floatArg())
                                        .executes { ctx -> moveDancerRel(ctx) }
                                    )
                                )
                            )
                        )
                    )
                    .then(literal("reset")
                        .executes { ctx -> reset(ctx) }
                    )
                    .then(literal("disband")
                        .executes { ctx -> disband(ctx) }
                    )
                    .then(literal("call")
                        .then(argument<String>("call", greedyString())
                            .executes { ctx -> call(ctx) }
                        )
                    )
            )
        }
    }

    private fun setupSquare(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val world = source.world
        val center = source.position

        SquareState.spawnSquare(world, center)
        source.sendFeedback({ Text.literal("Square formed!") }, false)
        return 1
    }

    private fun reset(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source

        SquareState.goHome()
        source.sendFeedback({ Text.literal("Explode and go home") }, false)
        return 1
    }

    private fun disband(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source

        SquareState.clear()
        source.sendFeedback({ Text.literal("Square disbanded") }, false)
        return 1
    }

    private fun moveDancer(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source

        val dancerName = getString(ctx, "dancer")
        val x = getFloat(ctx, "x")
        val z = getFloat(ctx, "z")
        val yaw = getFloat(ctx, "yaw")

        val result = SquareState.moveDancer(dancerName, x, z, yaw)

        val feedback = if (result == 1)
            "Moved $dancerName to ($x, $z) facing $yaw°"
        else
            "No dancer found with name \"$dancerName\"!"

        source.sendFeedback({ Text.literal(feedback) }, false)

        return result
    }

    private fun moveDancerRel(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source

        val dancerName = getString(ctx, "dancer")
        val forward = getFloat(ctx, "forward")
        val right = getFloat(ctx, "right")
        val turn = getFloat(ctx, "turn")

        val result = SquareState.moveDancerRel(dancerName, forward, right, turn)

        val feedback = if (result == 1)
            "Moved $dancerName by ($forward, $right) and $turn°"
        else
            "No dancer found with name \"$dancerName\"!"

        source.sendFeedback({ Text.literal(feedback) }, false)

        return result
    }

    private fun call(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source

        val callName = getString(ctx, "call")

        val call = CallList.createCall(callName)

        if (call == null) {
            source.sendFeedback({ Text.literal("Call \"$callName\" not found") }, false)
            return 0
        }

        if (!call.isLegal()) {
            source.sendFeedback({ Text.literal("Call \"$callName\" is not legal") }, false)
            return 0
        }

        val callback = call.callback()

        source.sendFeedback({ Text.literal(callback) }, false)

        Ticker.play(call)

        return 1
    }
}