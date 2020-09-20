package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.ExDrico.Companion.CREATIVE_TAB
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object BlockCompendium {

    private val blockMap: LinkedHashMap<Identifier, Block> = linkedMapOf()
    private val blockEntityMap: LinkedHashMap<Block, BlockEntityType<*>> = linkedMapOf()

    private fun <T: Block> register(identifier: Identifier, block: T): T {
        blockMap[identifier] = block
        return block
    }

    val ACACIA_SIEVE = register(ModIdentifier("acacia_sieve"), SieveBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque()))

    @Suppress("UNCHECKED_CAST")
    fun getEntityType(block: Block): BlockEntityType<*>? = blockEntityMap[block]

    fun initBlocks() {
        blockMap.forEach { (identifier, block) ->
            Registry.register(Registry.BLOCK, identifier, block)
            Registry.register(Registry.ITEM, identifier, BlockItem(block, Item.Settings().group(CREATIVE_TAB)))
            (block as? BlockEntityProvider)?.let {
                BlockEntityType.Builder.create({it.createBlockEntity(null)}, block).build(null) as BlockEntityType<*>
            }?.let {
                blockEntityMap[block] = Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, it)
            }
        }
    }

}