package io.github.cafeteriaguild.exdrico.common.material

import io.github.cafeteriaguild.exdrico.client.render.color.ColoredBlock
import io.github.cafeteriaguild.exdrico.client.render.color.ColoredItem
import io.github.cafeteriaguild.exdrico.common.blocks.ColorBlock
import io.github.cafeteriaguild.exdrico.common.items.ColorItem
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.ResourceHelper
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.awt.Color

object MaterialCompendium {

    private val materialMap: LinkedHashMap<Identifier, Color> = linkedMapOf()

    private fun <T: Color> register(identifier: Identifier, color: T): T {
        materialMap[identifier] = color
        return color
    }

    val COPPER = register(ModIdentifier("copper"), Color.ORANGE)
    val LEAD = register(ModIdentifier("lead"), Color(120, 0, 255))
    val GOLD = register(ModIdentifier("gold"), Color.YELLOW)

    fun initMaterials() {
        materialMap.forEach { (identifier, color) ->
            ResourceHelper(identifier, color).withItems("chunk", "pieces", "dust").withBlocks()
        }
    }

}