package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.client.render.color.ColoredBlock
import net.minecraft.block.Block

class ColorBlock(val colorModel: ColoredBlock, val color: Int, settings: Settings): Block(settings) {

    companion object {
        val blockMap = linkedMapOf<ColorBlock, ColoredBlock>()
    }

    init {
        blockMap[this] = colorModel
    }

}