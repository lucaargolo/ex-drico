package io.github.cafeteriaguild.exdrico

import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import net.fabricmc.api.ModInitializer

class ExDrico: ModInitializer {

    companion object {
        const val MOD_ID = "exdrico"
    }

    override fun onInitialize() {
        BlockCompendium.initBlocks()
    }

}