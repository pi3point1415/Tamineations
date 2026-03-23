package com.pi3point14.tamineations.formations

import com.pi3point14.tamineations.DancerPosition
import com.pi3point14.tamineations.Formation
import net.minecraft.util.math.Vec3d

class StaticSquare(dancers: List<DancerPosition>) : Formation(dancers) {
    fun isValid() : Boolean {
        if (dancerAtLocation(Vec3d(-1.0, 3.0, 180.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(1.0, 3.0, 180.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(3.0, 1.0, 90.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(3.0, -1.0, 90.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(1.0, -3.0, 0.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(-1.0, -3.0, 0.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(-3.0, -1.0, 270.0)) == null) {
            return false
        }
        if (dancerAtLocation(Vec3d(-3.0, 1.0, 270.0)) == null) {
            return false
        }

        return true
    }
}