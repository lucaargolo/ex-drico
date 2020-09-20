package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.item.Item
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
    }

}