package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.client.render.color.ColoredItem
import net.minecraft.item.Item

class ColorItem(val colorModel: ColoredItem, val color: Int, settings: Settings): Item(settings) {

    companion object {
        val itemMap = linkedMapOf<ColorItem, ColoredItem>()
    }

    init {
        itemMap[this] = colorModel
    }

}