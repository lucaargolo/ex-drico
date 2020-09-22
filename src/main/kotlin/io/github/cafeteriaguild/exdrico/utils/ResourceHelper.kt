package io.github.cafeteriaguild.exdrico.utils

import io.github.cafeteriaguild.exdrico.client.render.color.ColoredBlock
import io.github.cafeteriaguild.exdrico.client.render.color.ColoredItem
import io.github.cafeteriaguild.exdrico.common.blocks.ColorBlock
import io.github.cafeteriaguild.exdrico.common.items.ColorItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.awt.Color

class ResourceHelper(val prefix: Identifier, val color: Color) {
    fun withItems(provider: (String) -> ColorItem, vararg suffixes: String): ResourceHelper {
        suffixes.forEach { suffix ->
            Registry.register(Registry.ITEM, Identifier(prefix.namespace, "${prefix.path}_$suffix"), provider(suffix))
        }
        return this
    }

    fun withItems(vararg suffixes: String) = withItems({ s ->
        val i = when (s) {
            "chunk" -> ColoredItem.ORE_CHUNK
            "pieces" -> ColoredItem.ORE_PIECES
            "dust" -> ColoredItem.ORE_DUST
            else -> throw IllegalArgumentException("unknown item type $s")
        }
        ColorItem(i, color, Item.Settings())
    }, *suffixes)

    fun withGravelBlock(): ResourceHelper {
        val block = ColorBlock(ColoredBlock.ORE_GRAVEL, color, FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.GRAVEL).strength(3f, 3f).breakByTool(FabricToolTags.SHOVELS).requiresTool())
        val blockItem = BlockItem(block, Item.Settings())
        Registry.register(Registry.BLOCK, Identifier(prefix.namespace, "${prefix.path}_gravel_block"), block)
        Registry.register(Registry.ITEM, Identifier(prefix.namespace, "${prefix.path}_gravel_block"), blockItem)
        return this
    }

    fun withSandBlock(): ResourceHelper {
        val block = ColorBlock(ColoredBlock.ORE_SAND, color, FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SAND).strength(3f, 3f).breakByTool(FabricToolTags.SHOVELS).requiresTool())
        val blockItem = BlockItem(block, Item.Settings())
        Registry.register(Registry.BLOCK, Identifier(prefix.namespace, "${prefix.path}_sand_block"), block)
        Registry.register(Registry.ITEM, Identifier(prefix.namespace, "${prefix.path}_sand_block"), blockItem)
        return this
    }

    fun withDustBlock(): ResourceHelper {
        val block = ColorBlock(ColoredBlock.ORE_DUST, color, FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.SNOW).strength(3f, 3f).breakByTool(FabricToolTags.SHOVELS).requiresTool())
        val blockItem = BlockItem(block, Item.Settings())
        Registry.register(Registry.BLOCK, Identifier(prefix.namespace, "${prefix.path}_dust_block"), block)
        Registry.register(Registry.ITEM, Identifier(prefix.namespace, "${prefix.path}_dust_block"), blockItem)
        return this
    }

    fun withBlocks(): ResourceHelper {
        withGravelBlock()
        withSandBlock()
        withDustBlock()
        return this
    }
}