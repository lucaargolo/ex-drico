package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.ExDrico.Companion.CREATIVE_TAB
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
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

    val OAK_SIEVE = register(ModIdentifier("oak_sieve"), SieveBlock(Blocks.OAK_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val SPRUCE_SIEVE = register(ModIdentifier("spruce_sieve"), SieveBlock(Blocks.SPRUCE_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val BIRCH_SIEVE = register(ModIdentifier("birch_sieve"), SieveBlock(Blocks.BIRCH_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val JUNGLE_SIEVE = register(ModIdentifier("jungle_sieve"), SieveBlock(Blocks.JUNGLE_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val DARK_OAK_SIEVE = register(ModIdentifier("dark_oak_sieve"), SieveBlock(Blocks.DARK_OAK_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val ACACIA_SIEVE = register(ModIdentifier("acacia_sieve"), SieveBlock(Blocks.ACACIA_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))

    val OAK_VAT = register(ModIdentifier("oak_vat"), VatBlock(Blocks.OAK_PLANKS, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val SPRUCE_VAT = register(ModIdentifier("spruce_vat"), VatBlock(Blocks.SPRUCE_PLANKS, true,FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val BIRCH_VAT = register(ModIdentifier("birch_vat"), VatBlock(Blocks.BIRCH_PLANKS, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val JUNGLE_VAT = register(ModIdentifier("jungle_vat"), VatBlock(Blocks.JUNGLE_PLANKS, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val DARK_OAK_VAT = register(ModIdentifier("dark_oak_vat"), VatBlock(Blocks.DARK_OAK_PLANKS, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val ACACIA_VAT = register(ModIdentifier("acacia_vat"), VatBlock(Blocks.ACACIA_PLANKS, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val STONE_VAT = register(ModIdentifier("stone_vat"), VatBlock(Blocks.STONE, false, FabricBlockSettings.copyOf(Blocks.STONE)))
    val ANDESITE_VAT = register(ModIdentifier("andesite_vat"), VatBlock(Blocks.ANDESITE, false, FabricBlockSettings.copyOf(Blocks.STONE)))
    val DIORITE_VAT = register(ModIdentifier("diorite_vat"), VatBlock(Blocks.DIORITE, false, FabricBlockSettings.copyOf(Blocks.STONE)))
    val GRANITE_VAT = register(ModIdentifier("granite_vat"), VatBlock(Blocks.GRANITE, false, FabricBlockSettings.copyOf(Blocks.STONE)))

    val OAK_CRUCIBLE = register(ModIdentifier("oak_crucible"), CrucibleBlock(Blocks.OAK_LOG, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val SPRUCE_CRUCIBLE = register(ModIdentifier("spruce_crucible"), CrucibleBlock(Blocks.SPRUCE_LOG, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val BIRCH_CRUCIBLE = register(ModIdentifier("birch_crucible"), CrucibleBlock(Blocks.BIRCH_LOG, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val JUNGLE_CRUCIBLE = register(ModIdentifier("jungle_crucible"), CrucibleBlock(Blocks.JUNGLE_LOG, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val DARK_OAK_CRUCIBLE = register(ModIdentifier("dark_oak_crucible"), CrucibleBlock(Blocks.DARK_OAK_LOG, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))
    val ACACIA_CRUCIBLE = register(ModIdentifier("acacia_crucible"), CrucibleBlock(Blocks.ACACIA_LOG, true, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)))

    val UNFIRED_CRUCIBLE = register(ModIdentifier("unfired_crucible"), CrucibleBlock(Blocks.CLAY, true, FabricBlockSettings.copyOf(Blocks.CLAY)))
    val CRUCIBLE = register(ModIdentifier("fired_crucible"), CrucibleBlock(Blocks.TERRACOTTA, false, FabricBlockSettings.copyOf(Blocks.TERRACOTTA)))

    val INFESTED_OAK_LEAVES = register(ModIdentifier("infested_oak_leaves"), InfestedLeavesBlock(Blocks.OAK_LEAVES as LeavesBlock, FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
    val INFESTED_SPRUCE_LEAVES = register(ModIdentifier("infested_spruce_leaves"), InfestedLeavesBlock(Blocks.SPRUCE_LEAVES as LeavesBlock, FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
    val INFESTED_BIRCH_LEAVES = register(ModIdentifier("infested_birch_leaves"), InfestedLeavesBlock(Blocks.BIRCH_LEAVES as LeavesBlock, FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
    val INFESTED_JUNGLE_LEAVES = register(ModIdentifier("infested_jungle_leaves"), InfestedLeavesBlock(Blocks.JUNGLE_LEAVES as LeavesBlock, FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
    val INFESTED_DARK_OAK_LEAVES = register(ModIdentifier("infested_dark_oak_leaves"), InfestedLeavesBlock(Blocks.DARK_OAK_LEAVES as LeavesBlock, FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
    val INFESTED_ACACIA_LEAVES = register(ModIdentifier("infested_acacia_leaves"), InfestedLeavesBlock(Blocks.ACACIA_LEAVES as LeavesBlock, FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))

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