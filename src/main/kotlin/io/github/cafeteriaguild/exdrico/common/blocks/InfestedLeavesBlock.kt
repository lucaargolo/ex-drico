package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.InfestedLeavesBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.LeavesBlock
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class InfestedLeavesBlock(val parent: LeavesBlock, settings: Settings): BlockWithEntity(settings) {

    companion object {
        val infestedLeavesMap = linkedMapOf<LeavesBlock, InfestedLeavesBlock>()

        val sprites: Collection<SpriteIdentifier>
            get() {
                val spriteList = mutableListOf<SpriteIdentifier>()
                infestedLeavesMap.forEach { (baseBlock, _) ->
                    val blockIdentifier = Registry.BLOCK.getId(baseBlock)
                    spriteList.add(SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("${blockIdentifier.namespace}:block/${blockIdentifier.path}")))
                }
                return spriteList
            }
    }

    val spriteId = infestedLeavesMap.size

    init {
        infestedLeavesMap[parent] = this
    }

    override fun getSidesShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = VoxelShapes.empty()

    override fun createBlockEntity(world: BlockView?) = InfestedLeavesBlockEntity(this)

}