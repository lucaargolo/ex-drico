package io.github.cafeteriaguild.exdrico.utils

import net.minecraft.util.Identifier

object SpriteColorCache {

    private val colorMap = linkedMapOf<Identifier, Int>()
    var lastIdentifier = Identifier("")

    fun addColor(color: Int) {
        colorMap[lastIdentifier] = color
    }

    fun getColor(identifier: Identifier) = colorMap[identifier] ?: -1

}