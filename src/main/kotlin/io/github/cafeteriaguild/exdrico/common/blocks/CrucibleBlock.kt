package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.CrucibleBlockEntity
import io.github.cafeteriaguild.exdrico.utils.VisibleBlockWithEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CrucibleBlock(baseBlock: Block, val isBurnable: Boolean, settings: Settings): VisibleBlockWithEntity(settings) {

    companion object {
        private val RAY_TRACE_SHAPE = createCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)
        private val OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(
            VoxelShapes.fullCube(),
            VoxelShapes.union(
                createCuboidShape(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
                createCuboidShape(4.0, 0.0, 0.0, 12.0, 3.0, 16.0),
                createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
                RAY_TRACE_SHAPE
            ),
            BooleanBiFunction.ONLY_FIRST
        )

        val crucibleMap = linkedMapOf<Block, CrucibleBlock>()

        val sprites: Collection<SpriteIdentifier>
            get() {
                val spriteList = mutableListOf<SpriteIdentifier>()
                crucibleMap.forEach { (baseBlock, _) ->
                    val blockIdentifier = Registry.BLOCK.getId(baseBlock)
                    spriteList.add(SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("${blockIdentifier.namespace}:block/${blockIdentifier.path}")))
                }
                return spriteList
            }
    }

    val spriteId = crucibleMap.size

    init {
        crucibleMap[baseBlock] = this
    }

    override fun createBlockEntity(world: BlockView?) = CrucibleBlockEntity(this)

    @Suppress("DEPRECATION")
    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity, hand: Hand, hit: BlockHitResult?): ActionResult {
        return super.onUse(state, world, pos, player, hand, hit)
    }

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape? {
        return OUTLINE_SHAPE
    }

    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape? {
        return RAY_TRACE_SHAPE
    }

}