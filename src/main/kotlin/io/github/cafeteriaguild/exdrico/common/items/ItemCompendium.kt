package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ItemCompendium {

    private val itemMap: LinkedHashMap<Identifier, Item> = linkedMapOf()

    private fun <T: Item> register(identifier: Identifier, item: T): T {
        itemMap[identifier] = item
        return item
    }

    val MESH = register(ModIdentifier("mesh"), MeshItem(Item.Settings()))
    val STONE_HAMMER = register(ModIdentifier("stone_hammer"), HammerItem(ToolMaterials.STONE, Item.Settings().maxDamage(128)))
    val WOODEN_CROOK = register(ModIdentifier("wooden_crook"), HammerItem(ToolMaterials.WOOD, Item.Settings().maxDamage(32)))
    val BLAZING_DOLL = register(ModIdentifier("blazing_doll"), Item(Item.Settings()))
    val CREEPING_DOLL = register(ModIdentifier("creeping_doll"), Item(Item.Settings()))
    val FLOATING_DOLL = register(ModIdentifier("floating_doll"), Item(Item.Settings()))
    val PROTECTING_DOLL = register(ModIdentifier("protecting_doll"), Item(Item.Settings()))
    val BUZZING_DOLL = register(ModIdentifier("buzzing_doll"), Item(Item.Settings()))

    fun initItems() {
        itemMap.forEach { (identifier, item) ->
            Registry.register(Registry.ITEM, identifier, item)
        }
    }

}