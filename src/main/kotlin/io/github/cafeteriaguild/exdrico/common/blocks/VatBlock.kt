package io.github.cafeteriaguild.exdrico.common.blocks

import alexiil.mc.lib.attributes.fluid.FixedFluidInv
import alexiil.mc.lib.attributes.fluid.FluidInvUtil
import io.github.cafeteriaguild.exdrico.common.blockentities.VatBlockEntity
import io.github.cafeteriaguild.exdrico.utils.VisibleBlockWithEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.ItemScatterer
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class VatBlock(baseBlock: Block, val isBurnable: Boolean, settings: Settings): VisibleBlockWithEntity(settings) {

    companion object {
        val vatMap = linkedMapOf<Block, VatBlock>()

        val sprites: Collection<SpriteIdentifier>
            get() {
                val spriteList = mutableListOf<SpriteIdentifier>()
                vatMap.forEach { (baseBlock, _) ->
                    val blockIdentifier = Registry.BLOCK.getId(baseBlock)
                    spriteList.add(SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("${blockIdentifier.namespace}:block/${blockIdentifier.path}")))
                }
                return spriteList
            }
    }

    val spriteId = vatMap.size

    init {
        vatMap[baseBlock] = this
    }

    override fun createBlockEntity(world: BlockView?) = VatBlockEntity(this)

    @Suppress("DEPRECATION")
    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity, hand: Hand, hit: BlockHitResult?): ActionResult {
        val blockEntity = world?.getBlockEntity(pos) as? VatBlockEntity ?: return ActionResult.PASS

        val vatStack = blockEntity.inv.extract(1)
        if (!vatStack.isEmpty) {
            ItemScatterer.spawn(world, pos?.up(), DefaultedList.ofSize(1, vatStack))
            return ActionResult.SUCCESS
        }

        val result = FluidInvUtil.interactHandWithTank(blockEntity.fluidInv as FixedFluidInv, player, hand)
        if (result.asActionResult().isAccepted) {
            blockEntity.markDirtyAndSync()
            return result.asActionResult()
        }

        val stackInHand = player.getStackInHand(hand)
        if (stackInHand.isEmpty) return ActionResult.PASS

        (world as? ServerWorld)?.let { serverWorld ->
            val insertion = blockEntity.inv.insert(stackInHand)
            val recipe = blockEntity.getRecipe(serverWorld)
            if (recipe != null && recipe.matches(blockEntity.inv, blockEntity.fluidInv)) {
                val stack = blockEntity.inv.extract(1)
                player.setStackInHand(hand, insertion)
                blockEntity.progress -= recipe.input.entries.first { (ing, _) -> ing.test(stack) }.value
            } else blockEntity.inv.extract(1)
            blockEntity.markDirtyAndSync()
        }
        return ActionResult.SUCCESS
    }

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape {
        return createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}