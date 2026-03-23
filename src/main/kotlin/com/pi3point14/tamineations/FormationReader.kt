package com.pi3point14.tamineations

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.minecraft.util.math.Vec3d

enum class Gender {
    LARK,
    ROBIN,
    ANY,
}

data class FormationDancer (
    val x: Double,
    val y: Double,
    val angle: Double,
    val gender: Gender,
    val number: Int,
)

data class FormationFormat(
    val name: String,
    val symmetric: Boolean,
    val formations: List<List<FormationDancer>>
)

object FormationManager {
    val formations : List<FormationFormat> = FormationReader.load()

    val formationDict = formations.associateBy { it.name }

    fun toFormation(name : String) : Formation? {
        val positions = mutableListOf<DancerPosition>()

        val formation = formationDict[name] ?: return null

        val selected = formation.formations[0]

        selected.forEach { dancer ->
            positions.add(
                DancerPosition (
                    vec = Vec3d(dancer.x, dancer.y, dancer.angle),
                    number = dancer.number,
                    lark = dancer.gender == Gender.LARK
                )
            )
            if (formation.symmetric) {
                positions.add(
                    DancerPosition(
                        vec = Vec3d(-dancer.x, -dancer.y, dancer.angle + 180),
                        number = dancer.number + selected.size / 2,
                        lark = dancer.gender == Gender.LARK
                    )
                )
            }
        }

        println(positions.size)
        positions.forEach { position ->
            println("\t${position.id}")
        }

        return Formation(positions)
    }
}

object FormationReader {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun load() : List<FormationFormat> {
        val stream = FormationReader::class.java.getResourceAsStream("/formations.json")
        val type = TypeToken.getParameterized(List::class.java, FormationFormat::class.java).type
        return gson.fromJson(stream?.reader(), type)
    }
}