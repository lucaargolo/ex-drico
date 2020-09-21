package io.github.cafeteriaguild.exdrico.common.material

import io.github.cafeteriaguild.exdrico.client.render.color.ColoredBlock
import io.github.cafeteriaguild.exdrico.client.render.color.ColoredItem
import io.github.cafeteriaguild.exdrico.common.blocks.ColorBlock
import io.github.cafeteriaguild.exdrico.common.items.ColorItem
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
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

    fun initMaterials() {
        materialMap.forEach { (identifier, color) ->
            Registry.register(Registry.ITEM, Identifier(identifier.namespace, identifier.path+"_dust"), ColorItem(ColoredItem.ORE_DUST, color, Item.Settings()))
            Registry.register(Registry.ITEM, Identifier(identifier.namespace, identifier.path+"_pieces"), ColorItem(ColoredItem.ORE_PIECES, color, Item.Settings()))
            Registry.register(Registry.ITEM, Identifier(identifier.namespace, identifier.path+"_chunk"), ColorItem(ColoredItem.ORE_CHUNK, color, Item.Settings()))
            val dustBlock = ColorBlock(ColoredBlock.ORE_DUST, color, FabricBlockSettings.of(Material.SOIL))
            Registry.register(Registry.BLOCK, Identifier(identifier.namespace, identifier.path+"_dust_block"), dustBlock)
            Registry.register(Registry.ITEM, Identifier(identifier.namespace, identifier.path+"_dust_block"), BlockItem(dustBlock, Item.Settings()))
            val sandBlock = ColorBlock(ColoredBlock.ORE_SAND, color, FabricBlockSettings.of(Material.SOIL))
            Registry.register(Registry.BLOCK, Identifier(identifier.namespace, identifier.path+"_sand_block"), sandBlock)
            Registry.register(Registry.ITEM, Identifier(identifier.namespace, identifier.path+"_sand_block"), BlockItem(sandBlock, Item.Settings()))
            val gravelBlock = ColorBlock(ColoredBlock.ORE_GRAVEL, color, FabricBlockSettings.of(Material.SOIL))
            Registry.register(Registry.BLOCK, Identifier(identifier.namespace, identifier.path+"_gravel_block"), gravelBlock)
            Registry.register(Registry.ITEM, Identifier(identifier.namespace, identifier.path+"_gravel_block"), BlockItem(gravelBlock, Item.Settings()))
        }
    }

}