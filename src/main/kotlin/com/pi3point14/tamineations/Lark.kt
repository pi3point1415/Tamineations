package com.pi3point14.tamineations

import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

class Lark(world: World) : ZombieEntity(EntityType.ZOMBIE, world) {
    override fun damage(world: ServerWorld, source: DamageSource, amount: Float): Boolean {
        return false
    }
}