package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.ExDrico.Companion.CREATIVE_TAB
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.Blocks
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

@Suppress("unused")
object BlockCompendium {

    private val blockMap: LinkedHashMap<Identifier, Block> = linkedMapOf()
    private val blockEntityMap: LinkedHashMap<Block, BlockEntityType<*>> = linkedMapOf()

    val blocksStack: List<ItemStack>
        get() {
            val blockStack = mutableListOf<ItemStack>()
            blockMap.values.forEach {
                blockStack.add(ItemStack(it))
            }
            return blockStack
        }

    private fun <T: Block> register(identifier: Identifier, block: T): T {
        blockMap[identifier] = block
        return block
    }

    val DUST = register(ModIdentifier("dust"), Block(FabricBlockSettings.copyOf(Blocks.SAND)))

    val OAK_SIEVE = register(ModIdentifier("oak_sieve"), SieveBlock(Blocks.OAK_PLANKS, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val SPRUCE_SIEVE = register(ModIdentifier("spruce_sieve"), SieveBlock(Blocks.SPRUCE_PLANKS, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val BIRCH_SIEVE = register(ModIdentifier("birch_sieve"), SieveBlock(Blocks.BIRCH_PLANKS, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val JUNGLE_SIEVE = register(ModIdentifier("jungle_sieve"), SieveBlock(Blocks.JUNGLE_PLANKS, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val DARK_OAK_SIEVE = register(ModIdentifier("dark_oak_sieve"), SieveBlock(Blocks.DARK_OAK_PLANKS, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val ACACIA_SIEVE = register(ModIdentifier("acacia_sieve"), SieveBlock(Blocks.ACACIA_PLANKS, FabricBlockSettings.of(Material.WOOD).nonOpaque()))

    val OAK_VAT = register(ModIdentifier("oak_vat"), VatBlock(Blocks.OAK_PLANKS, true, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val SPRUCE_VAT = register(ModIdentifier("spruce_vat"), VatBlock(Blocks.SPRUCE_PLANKS, true,FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val BIRCH_VAT = register(ModIdentifier("birch_vat"), VatBlock(Blocks.BIRCH_PLANKS, true, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val JUNGLE_VAT = register(ModIdentifier("jungle_vat"), VatBlock(Blocks.JUNGLE_PLANKS, true, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val DARK_OAK_VAT = register(ModIdentifier("dark_oak_vat"), VatBlock(Blocks.DARK_OAK_PLANKS, true, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val ACACIA_VAT = register(ModIdentifier("acacia_vat"), VatBlock(Blocks.ACACIA_PLANKS, true, FabricBlockSettings.of(Material.WOOD).nonOpaque()))
    val STONE_VAT = register(ModIdentifier("stone_vat"), VatBlock(Blocks.STONE, false, FabricBlockSettings.of(Material.STONE).nonOpaque()))
    val ANDESITE_VAT = register(ModIdentifier("andesite_vat"), VatBlock(Blocks.ANDESITE, false, FabricBlockSettings.of(Material.STONE).nonOpaque()))
    val DIORITE_VAT = register(ModIdentifier("diorite_vat"), VatBlock(Blocks.DIORITE, false, FabricBlockSettings.of(Material.STONE).nonOpaque()))
    val GRANITE_VAT = register(ModIdentifier("granite_vat"), VatBlock(Blocks.GRANITE, false, FabricBlockSettings.of(Material.STONE).nonOpaque()))

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