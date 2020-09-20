package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.items.MeshItem
import io.github.cafeteriaguild.exdrico.utils.VisibleBlockWithEntity
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class SieveBlock(settings: Settings): VisibleBlockWithEntity(settings) {

    override fun createBlockEntity(world: BlockView?) = SieveBlockEntity(this)

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity, hand: Hand, hit: BlockHitResult?): ActionResult {
        val blockEntity = world?.getBlockEntity(pos) as? SieveBlockEntity ?: return ActionResult.PASS

        val holdingStack = player.getStackInHand(hand)
        val holdingItem = holdingStack.item

        if(holdingItem is MeshItem) {
            val meshType = MeshItem.getMeshType(holdingStack)
            if(blockEntity.meshType == null) {
                blockEntity.meshType = meshType
                holdingStack.decrement(1)
                blockEntity.markDirtyAndSyncRender()
                return ActionResult.CONSUME
            }
        }

        return super.onUse(state, world, pos, player, hand, hit)
    }

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape {
        return VoxelShapes.union(
            createCuboidShape(0.0, 11.0, 0.0, 16.0, 14.0, 16.0),
            VoxelShapes.union(
                VoxelShapes.union(
                    createCuboidShape(1.0, 0.0, 1.0, 2.0, 10.0, 2.0),
                    createCuboidShape(14.0, 0.0, 1.0, 15.0, 10.0, 2.0),
                ),
                VoxelShapes.union(
                    createCuboidShape(14.0, 0.0, 14.0, 15.0, 10.0, 15.0),
                    createCuboidShape(1.0, 0.0, 14.0, 2.0, 10.0, 15.0),
                )
            )
        )
    }
}