package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterial
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

    fun initItems() {
        itemMap.forEach { (identifier, item) ->
            Registry.register(Registry.ITEM, identifier, item)
        }
        Registry.register(Registry.ITEM, ModIdentifier("stone_hammer"), HammerItem(ToolMaterials.STONE, Item.Settings().maxDamage(128)))
        Registry.register(Registry.ITEM, ModIdentifier("wooden_crook"), HammerItem(ToolMaterials.WOOD, Item.Settings().maxDamage(32)))
    }

}