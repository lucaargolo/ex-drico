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

    val COPPER = register(ModIdentifier("copper"), 0xCA651F)
    val TIN = register(ModIdentifier("tin"), 0xFFFFFF)
    val IRON = register(ModIdentifier("iron"), 0xD5D5D5)
    val GOLD = register(ModIdentifier("gold"), 0xE7EB56)
    val LEAD = register(ModIdentifier("lead"), 0x705186)
    val SILVER = register(ModIdentifier("silver"), 0xC5F5F0)
    val NICKEL = register(ModIdentifier("nickel"), 0xCBBB6E)

    fun initMaterials() {
        materialMap.forEach { (identifier, color) ->
            ResourceHelper(identifier, color).withItems("chunk", "pieces", "dust").withBlocks()
        }
    }

}