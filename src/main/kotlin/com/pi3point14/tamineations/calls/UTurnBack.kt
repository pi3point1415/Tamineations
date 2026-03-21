package com.pi3point14.tamineations.calls

import com.pi3point14.tamineations.Call
import com.pi3point14.tamineations.Formation
import com.pi3point14.tamineations.SquareState
import net.minecraft.util.math.Vec3d

class UTurnBack : Call() {
    override val length = 20

    val startFormation : Formation = SquareState.getFormation() ?: Formation(
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
        Vec3d(0.0, 0.0, 0.0),
    )

    var currentFormation : Formation = startFormation.copy()

    val endFormation : Formation = startFormation.copy()

    init {
        endFormation.l1 = startFormation.l1.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.l2 = startFormation.l2.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.l3 = startFormation.l3.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.l4 = startFormation.l4.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.r1 = startFormation.r1.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.r2 = startFormation.r2.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.r3 = startFormation.r3.add(Vec3d(0.0, 0.0, 180.0))
        endFormation.r4 = startFormation.r4.add(Vec3d(0.0, 0.0, 180.0))
    }

    override fun onTick() {
        currentFormation = startFormation.lerp(endFormation, tick.toDouble() / length.toDouble())

        SquareState.setFormation(currentFormation)
    }

    override fun end() {
        SquareState.setFormation(endFormation)
    }

    override fun callback(): String {
        return "U turn back!"
    }
}