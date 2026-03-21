package com.pi3point14.tamineations

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import kotlin.math.floor


object SquareState {
    val DANCER_NAMES = listOf("L1", "L2", "L3", "L4", "R1", "R2", "R3", "R4")

    const val GRID = 2.0f

    val coupleColors = mapOf(
        "1" to 0xFF0000,  // red
        "2" to 0x00FF00,  // green
        "3" to 0x0000FF,  // blue
        "4" to 0xFFFF00,  // yellow
    )

    val dancers = mutableMapOf<String, Dancer>()
    var center: Vec3d = Vec3d(0.0, 0.0, 0.0)


    fun spawnSquare(world: ServerWorld, center: Vec3d) {
        clear()

        val centerRound = Vec3d(floor(center.x) + 0.5, center.y, floor(center.z) + 0.5)

        SquareState.center = centerRound

        DANCER_NAMES.forEach { name ->
            Dancer(world, name)
        }

        goHome()
    }

    fun clear() {
        dancers.values.forEach { dancer ->
            dancer.discard()
        }
        dancers.clear()
    }

    fun moveDancer(dancer: String, x: Float, z: Float, yaw: Float): Int {
        val entity = dancers[dancer.uppercase()] ?: return 0

        entity.moveAbs(x, z, yaw)

        return 1
    }

    fun moveDancerRel(dancer: String, forward: Float, right: Float, turn: Float): Int {
        val entity = dancers[dancer.uppercase()] ?: return 0

        entity.moveRel(forward, right, turn)

        return 1
    }

    fun moveDancer(dancer: String, pos: Vec3d) {
        moveDancer(dancer, pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
    }

    fun moveDancerRel(dancer: String, pos: Vec3d) {
        moveDancerRel(dancer, pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
    }

    fun goHome() {
        val smallDist = GRID / 2
        val largeDist = GRID * 3 / 2

        moveDancer("L1", -smallDist, largeDist, 180f)
        moveDancer("R1", smallDist, largeDist, 180f)

        moveDancer("L2", largeDist, smallDist, 90f)
        moveDancer("R2", largeDist, -smallDist, 90f)

        moveDancer("L3", smallDist, -largeDist, 0f)
        moveDancer("R3", -smallDist, -largeDist, 0f)

        moveDancer("L4", -largeDist, -smallDist, 270f)
        moveDancer("R4", -largeDist, smallDist, 270f)
    }

    fun getFormation() : Formation? {
        val l1 = dancers["L1"] ?: return null
        val l2 = dancers["L2"] ?: return null
        val l3 = dancers["L3"] ?: return null
        val l4 = dancers["L4"] ?: return null
        val r1 = dancers["R1"] ?: return null
        val r2 = dancers["R2"] ?: return null
        val r3 = dancers["R3"] ?: return null
        val r4 = dancers["R4"] ?: return null

        return Formation(
            Vec3d(l1.xSquare, l1.zSquare, l1.yaw.toDouble()),
            Vec3d(l2.xSquare, l2.zSquare, l2.yaw.toDouble()),
            Vec3d(l3.xSquare, l3.zSquare, l3.yaw.toDouble()),
            Vec3d(l4.xSquare, l4.zSquare, l4.yaw.toDouble()),
            Vec3d(r1.xSquare, r1.zSquare, r1.yaw.toDouble()),
            Vec3d(r2.xSquare, r2.zSquare, r2.yaw.toDouble()),
            Vec3d(r3.xSquare, r3.zSquare, r3.yaw.toDouble()),
            Vec3d(r4.xSquare, r4.zSquare, r4.yaw.toDouble()),
        )
    }

    fun setFormation(formation: Formation) {
        moveDancer("L1", formation.l1)
        moveDancer("L2", formation.l2)
        moveDancer("L3", formation.l3)
        moveDancer("L4", formation.l4)
        moveDancer("R1", formation.r1)
        moveDancer("R2", formation.r2)
        moveDancer("R3", formation.r3)
        moveDancer("R4", formation.r4)
    }
}