package com.pi3point14.tamineations

import net.minecraft.util.math.Vec3d
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import kotlin.math.round

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.mob.HostileEntity


const val gridSize = 2.0

val coupleColors = mapOf(
    "1" to 0xFF0000,  // red
    "2" to 0x00AA00,  // green
    "3" to 0x4444FF,  // blue
    "4" to 0xFFFF00,  // yellow
)

fun spawnSquare(world: ServerWorld, center: Vec3d) {

    // Despawn existing dancers if any
    SquareState.dancers.values.forEach { uuid ->
        world.getEntity(uuid)?.discard()
    }
    SquareState.clear()

    val centerRound = Vec3d(round(center.x), round(center.y), round(center.z))

    val positions = buildDancerPositions(centerRound, gridSize)

    for ((pos, yaw, name, robin) in positions) {
        spawnDancer(world, pos, yaw, name, robin)
    }

    SquareState.center = centerRound
}

data class DancerSpawn(val pos: Vec3d, val yaw: Float, val name: String, val robin: Boolean)

fun buildDancerPositions(
    center: Vec3d,
    grid: Double,
): List<DancerSpawn> {
    val smallDist = grid / 2
    val largeDist = grid * 3 / 2
    return listOf(
        // Couple 1
        DancerSpawn(Vec3d(center.x - smallDist, center.y, center.z + largeDist), 180f, "1L", false),
        DancerSpawn(Vec3d(center.x + smallDist, center.y, center.z + largeDist), 180f, "1R", true),

        // Couple 2
        DancerSpawn(Vec3d(center.x + largeDist, center.y, center.z + smallDist), 90f, "2L", true),
        DancerSpawn(Vec3d(center.x + largeDist, center.y, center.z - smallDist), 90f, "2R", false),

        // Couple 3
        DancerSpawn(Vec3d(center.x + smallDist, center.y, center.z - largeDist), 0f, "3L", false),
        DancerSpawn(Vec3d(center.x - smallDist, center.y, center.z - largeDist), 0f, "3R", true),

        // Couple 4
        DancerSpawn(Vec3d(center.x - largeDist, center.y, center.z - smallDist), 270f, "4L", false),
        DancerSpawn(Vec3d(center.x - largeDist, center.y, center.z + smallDist), 270f, "4R", true),
    )
}

fun spawnDancer(world: ServerWorld, pos: Vec3d, yaw: Float, name: String, robin: Boolean) {


    val dancer: HostileEntity
    if (robin) {
        dancer = Robin(world)
    } else {
        dancer = Lark(world)
    }

    dancer.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, 0f)

    dancer.yaw = yaw
    dancer.headYaw = yaw
    dancer.bodyYaw = yaw

    // Make it a safe, inert dancer
    dancer.isInvulnerable = true
    dancer.isAiDisabled = true
    dancer.isSilent = true
    dancer.setPersistent()

    // Name tag visible overhead
    dancer.customName = Text.literal(name)
    dancer.isCustomNameVisible = true

    // Suppress the spawn loot/equipment
    dancer.setCanPickUpLoot(false)

    val coupleNum = name.first().toString()  // "1L" -> "1"
    val color = coupleColors[coupleNum] ?: 0xFFFFFF

    for ((slot, stack) in makeColoredArmor(color)) {
        dancer.equipStack(slot, stack)
        dancer.setEquipmentDropChance(slot, 0f)
    }

    world.spawnEntity(dancer)
    SquareState.dancers[name] = dancer.uuid
}

object SquareState {
    val dancers = mutableMapOf<String, java.util.UUID>()
    var center: Vec3d = Vec3d(0.0, 0.0, 0.0)

    fun clear() = dancers.clear()
}

fun makeColoredArmor(color: Int): List<Pair<EquipmentSlot, ItemStack>> {
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