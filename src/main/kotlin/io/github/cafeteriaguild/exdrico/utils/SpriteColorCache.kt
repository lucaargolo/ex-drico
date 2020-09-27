package io.github.cafeteriaguild.exdrico.utils

import net.minecraft.util.Identifier

object SpriteColorCache {

    private val colorMap = linkedMapOf<Identifier, Int>()
    var lastIdentifier = Identifier("")

    fun addColor(color: Int) {
        colorMap[lastIdentifier] = color
    }

    fun getColor(spriteIdentifier: Identifier): Int {
        val identifier = Identifier(spriteIdentifier.namespace, "textures/${spriteIdentifier.path}.png")
        return colorMap[identifier] ?: -1
    }

}