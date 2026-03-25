package com.pi3point14.tamineations

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import kotlin.math.floor


object SquareState {
    val coupleColors = mapOf(
        1 to 0xFF0000,  // red
        2 to 0x00FF00,  // green
        3 to 0x0000FF,  // blue
        4 to 0xFFFF00,  // yellow
    )

    val dancers = mutableMapOf<String, Dancer>()
    var center: Vec3d = Vec3d(0.0, 0.0, 0.0)


    fun spawnSquare(world: ServerWorld, center: Vec3d) {
        clear()

        val centerRound = Vec3d(floor(center.x) + 0.5, center.y, floor(center.z) + 0.5)

        SquareState.center = centerRound

        coupleColors.keys.forEach { number ->
            Dancer(world, true, number)
            Dancer(world, false, number)
        }

        goHome()
    }

    fun clear() {
        dancers.values.forEach { dancer ->
            dancer.discard()
        }
        dancers.clear()
    }

    fun moveDancer(dancer: String, x: Double, z: Double, yaw: Double): Int {
        val entity = dancers[dancer.uppercase()] ?: return 0

        entity.moveAbs(x, z, yaw)

        return 1
    }

    fun moveDancerRel(dancer: String, forward: Double, right: Double, turn: Double): Int {
        val entity = dancers[dancer.uppercase()] ?: return 0

        entity.moveRel(forward, right, turn)

        return 1
    }

    fun moveDancer(dancer: String, pos: Vec3d) {
        moveDancer(dancer, pos.x, pos.y, pos.z)
    }

    fun moveDancerRel(dancer: String, pos: Vec3d) {
        moveDancerRel(dancer, pos.x, pos.y, pos.z)
    }

    fun goHome() {
        val formation = FormationManager.toFormation("static square") ?: return
        setFormation(formation)
    }

    fun getFormation() : Formation {
        return Formation(
            dancers.values.map { it.toDancerPosition() }
        )
    }

    fun setFormation(formation: Formation) {
        formation.dancers.forEach { dancer ->
            moveDancer(dancer.id, dancer.vec)
        }
    }
}