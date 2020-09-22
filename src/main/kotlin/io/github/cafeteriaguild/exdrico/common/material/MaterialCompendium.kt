package io.github.cafeteriaguild.exdrico.common.material

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.ResourceHelper
import net.minecraft.util.Identifier

object MaterialCompendium {

    private val materialMap: LinkedHashMap<Identifier, Int> = linkedMapOf()

    private fun register(identifier: Identifier, color: Int): Int {
        materialMap[identifier] = color
        return color
    }

    val COPPER = register(ModIdentifier("copper"), 0xfa8d20)
    val LEAD = register(ModIdentifier("lead"), 0x592099)
    val GOLD = register(ModIdentifier("gold"), 0xffdd00)

    fun initMaterials() {
        materialMap.forEach { (identifier, color) ->
            ResourceHelper(identifier, color).withItems("chunk", "pieces", "dust").withBlocks()
        }
    }

}