package com.pi3point14.tamineations

import net.minecraft.util.math.Vec3d

class Formation (
    var l1 : Vec3d,
    var l2 : Vec3d,
    var l3 : Vec3d,
    var l4 : Vec3d,
    var r1 : Vec3d,
    var r2 : Vec3d,
    var r3 : Vec3d,
    var r4 : Vec3d) {

    fun lerp(other: Formation, fraction: Double): Formation {
        return Formation(
            l1.lerp(other.l1, fraction),
            l2.lerp(other.l2, fraction),
            l3.lerp(other.l3, fraction),
            l4.lerp(other.l4, fraction),
            r1.lerp(other.r1, fraction),
            r2.lerp(other.r2, fraction),
            r3.lerp(other.r3, fraction),
            r4.lerp(other.r4, fraction),
        )
    }

    fun copy() : Formation {
        return Formation(
            Vec3d(l1.x, l1.y, l1.z),
            Vec3d(l2.x, l2.y, l2.z),
            Vec3d(l3.x, l3.y, l3.z),
            Vec3d(l4.x, l4.y, l4.z),
            Vec3d(r1.x, r1.y, r1.z),
            Vec3d(r2.x, r2.y, r2.z),
            Vec3d(r3.x, r3.y, r3.z),
            Vec3d(r4.x, r4.y, r4.z),
        )
    }
}