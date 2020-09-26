package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.items.ItemCompendium
import io.github.cafeteriaguild.exdrico.common.items.MeshItem
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.utils.SpriteColorCache
import io.github.cafeteriaguild.exdrico.utils.VisibleBlockWithEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class SieveBlock(baseBlock: Block, settings: Settings): VisibleBlockWithEntity(settings) {

    val spriteId = sieveMap.size

    init {
        sieveMap[baseBlock] = this
    }

    override fun createBlockEntity(world: BlockView?) = SieveBlockEntity(this)

    @Suppress("DEPRECATION")
    override fun onUse(state: BlockState?, world: World?, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult?): ActionResult {
        val blockEntity = world?.getBlockEntity(pos) as? SieveBlockEntity ?: return ActionResult.PASS

        val holdingStack = player.getStackInHand(hand)
        val holdingItem = holdingStack.item

        (holdingItem as? MeshItem)?.let {
            val meshType = MeshItem.getMeshType(holdingStack)
            if(blockEntity.meshType == null) {
                blockEntity.meshType = meshType
                holdingStack.decrement(1)
                blockEntity.markDirtyAndSyncRender()
                return ActionResult.CONSUME
            }
        }

        (holdingItem as? BlockItem)?.let {
            val block = it.block
            if (blockEntity.block == null && blockEntity.meshType != null) {
                if(world is ServerWorld && SieveBlockEntity.getLoot(world, block, blockEntity.meshType ?: MeshType.EMPTY).isNotEmpty()) {
                    blockEntity.block = block
                    holdingStack.decrement(1)
                    blockEntity.markDirtyAndSync()
                }
                return ActionResult.CONSUME
            }
        }

        if(holdingStack.isEmpty && player.isSneaking && blockEntity.block == null && blockEntity.meshType != null) {
            val meshType = blockEntity.meshType
            blockEntity.meshType = null
            blockEntity.markDirtyAndSyncRender()
            val stack = ItemStack(ItemCompendium.MESH)
            stack.orCreateTag.putString("mesh", meshType!!.identifier.toString())
            player.setStackInHand(hand, stack)
            return ActionResult.CONSUME
        }

        if(blockEntity.block != null && blockEntity.meshType != null) {
            blockEntity.progress += 10
            val block = blockEntity.block ?: Blocks.AIR
            if(blockEntity.progress >= 100) {
                blockEntity.progress = 0
                blockEntity.block = null
                if(world is ServerWorld) {
                    val loot = SieveBlockEntity.getLoot(world, block, blockEntity.meshType ?: MeshType.EMPTY)
                    ItemScatterer.spawn(world, pos, loot)
                }
            }
            if(world.isClient) {
                repeat(4) { x ->
                    repeat(4) { z ->
                        world.addParticle(BlockStateParticleEffect(ParticleTypes.BLOCK, block.defaultState), pos.x+(x/4.0), pos.y.toDouble()+0.5, pos.z+(z/4.0), 0.0, -0.3, 0.0)
                    }
                }
            }
            blockEntity.markDirtyAndSync()
            return ActionResult.SUCCESS
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

    companion object {
        val sieveMap = linkedMapOf<Block, SieveBlock>()

        val sprites: Collection<SpriteIdentifier>
            get() {
                val spriteList = mutableListOf<SpriteIdentifier>()
                sieveMap.forEach { (baseBlock, _) ->
                    val blockIdentifier = Registry.BLOCK.getId(baseBlock)
                    spriteList.add(SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("${blockIdentifier.namespace}:block/${blockIdentifier.path}")))
                }
                return spriteList
            }
    }
}