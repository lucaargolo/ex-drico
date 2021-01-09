package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ItemCompendium {

    private val itemMap: LinkedHashMap<Identifier, Item> = linkedMapOf()

    val itemsStack: List<ItemStack>
        get() {
            val itemStack = mutableListOf<ItemStack>()
            itemMap.values.forEach {
                if(it !is MeshItem) itemStack.add(ItemStack(it))
            }
            return itemStack
        }

    private fun <T: Item> register(identifier: Identifier, item: T): T {
        itemMap[identifier] = item
        return item
    }

    val MESH = register(ModIdentifier("mesh"), MeshItem(Settings()))

    val SILKWORM = register(ModIdentifier("silkworm"), SilkWormItem(Settings()))

    val WOODEN_HAMMER = register(ModIdentifier("wooden_hammer"), HammerItem(ToolMaterials.WOOD, Settings()))
    val STONE_HAMMER = register(ModIdentifier("stone_hammer"), HammerItem(ToolMaterials.STONE, Settings()))
    val IRON_HAMMER = register(ModIdentifier("iron_hammer"), HammerItem(ToolMaterials.IRON, Settings()))
    val GOLD_HAMMER = register(ModIdentifier("gold_hammer"), HammerItem(ToolMaterials.GOLD, Settings()))
    val DIAMOND_HAMMER = register(ModIdentifier("diamond_hammer"), HammerItem(ToolMaterials.DIAMOND, Settings()))
    val NETHERITE_HAMMER = register(ModIdentifier("netherite_hammer"), HammerItem(ToolMaterials.NETHERITE, Settings()))

    val WOODEN_CROOK = register(ModIdentifier("wooden_crook"), CrookItem(ToolMaterials.WOOD, Settings()))
    val STONE_CROOK = register(ModIdentifier("stone_crook"), CrookItem(ToolMaterials.STONE, Settings()))
    val IRON_CROOK = register(ModIdentifier("iron_crook"), CrookItem(ToolMaterials.IRON, Settings()))
    val GOLD_CROOK = register(ModIdentifier("gold_crook"), CrookItem(ToolMaterials.GOLD, Settings()))
    val DIAMOND_CROOK = register(ModIdentifier("diamond_crook"), CrookItem(ToolMaterials.DIAMOND, Settings()))
    val NETHERITE_CROOK = register(ModIdentifier("netherite_crook"), CrookItem(ToolMaterials.NETHERITE, Settings()))

    val BLAZING_DOLL = register(ModIdentifier("blazing_doll"), Item(Settings()))
    val CREEPING_DOLL = register(ModIdentifier("creeping_doll"), Item(Settings()))
    val FLOATING_DOLL = register(ModIdentifier("floating_doll"), Item(Settings()))
    val PROTECTING_DOLL = register(ModIdentifier("protecting_doll"), Item(Settings()))
    val BUZZING_DOLL = register(ModIdentifier("buzzing_doll"), Item(Settings()))
    
    fun initItems() {
        itemMap.forEach { (identifier, item) ->
            Registry.register(Registry.ITEM, identifier, item)
        }
    }

}