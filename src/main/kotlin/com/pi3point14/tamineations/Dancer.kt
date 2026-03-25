package com.pi3point14.tamineations

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d

class Dancer (world: ServerWorld, val lark: Boolean, val number: Int) : HostileEntity(
    if (lark) EntityType.ZOMBIE else EntityType.SKELETON, world) {

    val dancerId = "${if (lark) 'L' else 'R'}${number}"

    init {
        setPosition(SquareState.center)

        isInvulnerable = true
        isAiDisabled = true
        isSilent = true
        setPersistent()

        // Name tag visible overhead
        customName = Text.literal(dancerId)
        isCustomNameVisible = true

        setCanPickUpLoot(false)

        val color = SquareState.coupleColors[number] ?: 0xFFFFFF

        for ((slot, stack) in makeColoredArmor(color)) {
            equipStack(slot, stack)
            setEquipmentDropChance(slot, 0f)
        }

        world.spawnEntity(this)
        SquareState.dancers[dancerId] = this
    }

    override fun damage(world: ServerWorld, source: DamageSource, amount: Float): Boolean {
        return false
    }

    val facingVec: Vec2d
        get() {
            val facing3D = this.getRotationVec(1.0F)

            return Vec2d(facing3D.x, facing3D.z).normalize()
        }

    val rightVec: Vec2d
        get() {
            return Vec2d(-facingVec.y, facingVec.x)
        }

    val xSquare: Double
        get () {
            return x - SquareState.center.x
        }

    val zSquare: Double
        get () {
            return z - SquareState.center.z
        }

    fun moveAbs(x: Double, z: Double, yaw: Double) {
        val xAbs = x + SquareState.center.x
        val yAbs = y
        val zAbs =  z + SquareState.center.z

        refreshPositionAndAngles(xAbs, yAbs, zAbs, yaw.toFloat(), 0f)
        this.yaw = yaw.toFloat()
        bodyYaw = yaw.toFloat()
        headYaw = yaw.toFloat()
    }

    fun moveRel(forward: Double, right: Double, turn: Double) {
        val start = Vec2d(x, z)
        val forward = facingVec.multiply(forward)
        val right = rightVec.multiply(right)

        val end = start.add(forward).add(right)

        val x = end.x
        val y = y
        val z = end.y
        val yaw = yaw + turn

        refreshPositionAndAngles(x, y, z, yaw.toFloat(), 0f)
        this.yaw = yaw.toFloat()
        headYaw = yaw.toFloat()
        bodyYaw = yaw.toFloat()
    }

    private fun makeColoredArmor(color: Int): List<Pair<EquipmentSlot, ItemStack>> {
        val dyedColor = DyedColorComponent(color)

        fun dye(item: net.minecraft.item.Item): ItemStack {
            val stack = ItemStack(item)
            stack.set(DataComponentTypes.DYED_COLOR, dyedColor)
            return stack
        }

        return listOf(
            EquipmentSlot.HEAD   to ItemStack(Items.STONE_BUTTON),
            EquipmentSlot.CHEST  to dye(Items.LEATHER_CHESTPLATE),
            EquipmentSlot.LEGS   to dye(Items.LEATHER_LEGGINGS),
            EquipmentSlot.FEET   to dye(Items.LEATHER_BOOTS),
        )
    }

    fun toDancerPosition () : DancerPosition {
        return DancerPosition(Vec3d(xSquare, zSquare, yaw.toDouble()), number, lark)
    }
}