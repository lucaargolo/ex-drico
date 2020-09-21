package io.github.cafeteriaguild.exdrico.client.render.color

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.util.Identifier

enum class ColoredBlock(val identifier: Identifier) {
    ORE_GRAVEL(ModIdentifier("ore_gravel")),
    ORE_SAND(ModIdentifier("ore_sand")),
    ORE_DUST(ModIdentifier("ore_dust"))
}