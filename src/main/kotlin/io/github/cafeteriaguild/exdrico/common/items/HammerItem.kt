package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterial

class HammerItem(material: ToolMaterial, settings: Settings) : MiningToolItem(7f, -4.5f, material, EFFECTIVE_BLOCKS, settings) {

    companion object {
        val EFFECTIVE_BLOCKS = setOf<Block>(Blocks.STONE, Blocks.GRAVEL, Blocks.SAND, BlockCompendium.DUST)
    }

}