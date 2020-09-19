package io.github.cafeteriaguild.exdrico.utils

import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity

abstract class VisibleBlockWithEntity(settings: Settings): BlockWithEntity(settings) {

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

}