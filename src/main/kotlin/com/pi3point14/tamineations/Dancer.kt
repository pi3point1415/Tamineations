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
import net.minecraft.util.math.Vec2f
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

    val facingVec: Vec2f
        get() {
            val facing3D = this.getRotationVec(1.0F)
            return Vec2f(facing3D.x.toFloat(), facing3D.z.toFloat()).normalize()
        }

    val rightVec: Vec2f
        get() {
            return Vec2f(-facingVec.y, facingVec.x)
        }

    val xSquare: Double
        get () {
            return x - SquareState.center.x
        }

    val zSquare: Double
        get () {
            return z - SquareState.center.z
        }

    fun moveAbs(x: Float, z: Float, yaw: Float) {
        val xAbs = x + SquareState.center.x
        val yAbs = y
        val zAbs =  z + SquareState.center.z

        refreshPositionAndAngles(xAbs, yAbs, zAbs, yaw, 0f)
        this.yaw = yaw
        bodyYaw = yaw
        headYaw = yaw
    }

    fun moveRel(forward: Float, right: Float, turn: Float) {
        val start = Vec2f(x.toFloat(), z.toFloat())
        val forward = facingVec.multiply(forward)
        val right = rightVec.multiply(right)

        val end = start.add(forward).add(right)

        print("start: ${start.x}, ${start.y}")
        print("forward: ${forward.x}, ${forward.y}")
        print("right: ${right.x}, ${right.y}")
        print("end: ${end.x}, ${end.y}")

        val x = end.x.toDouble()
        val y = y
        val z = end.y.toDouble()
        val yaw = yaw + turn

        refreshPositionAndAngles(x, y, z, yaw, 0f)
        this.yaw = yaw
        headYaw = yaw
        bodyYaw = yaw
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