package com.pi3point14.tamineations

import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

class Robin(world: World) : SkeletonEntity(EntityType.SKELETON, world) {
    override fun damage(world: ServerWorld, source: DamageSource, amount: Float): Boolean {
        return false
    }
}