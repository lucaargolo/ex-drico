package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.utils.VisibleBlockWithEntity
import net.minecraft.world.BlockView

class SieveBlock(settings: Settings): VisibleBlockWithEntity(settings) {

    override fun createBlockEntity(world: BlockView?) = SieveBlockEntity(this)

}