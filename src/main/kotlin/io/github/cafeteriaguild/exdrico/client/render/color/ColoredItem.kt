package io.github.cafeteriaguild.exdrico.client.render.color

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.util.Identifier

enum class ColoredItem(val identifier: Identifier) {
    ORE_CHUNK(ModIdentifier("ore_chunk")),
    ORE_PIECES(ModIdentifier("ore_pieces")),
    ORE_DUST(ModIdentifier("ore_dust"))
}