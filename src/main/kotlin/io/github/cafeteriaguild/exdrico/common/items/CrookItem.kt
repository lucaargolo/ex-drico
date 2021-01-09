package io.github.cafeteriaguild.exdrico.common.items

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.LeavesBlock
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterial

class CrookItem(material: ToolMaterial, settings: Settings) : MiningToolItem( 1.5f, -2f, material, EFFECTIVE_BLOCKS, settings) {

    override fun isEffectiveOn(state: BlockState) = state.block is LeavesBlock

    companion object {
        val EFFECTIVE_BLOCKS = setOf<Block>(Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES)
    }
}