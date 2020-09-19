package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.utils.VisibleBlockWithEntity
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class SieveBlock(settings: Settings): VisibleBlockWithEntity(settings) {

    override fun createBlockEntity(world: BlockView?) = SieveBlockEntity(this)

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hit: BlockHitResult?): ActionResult {
        //this is just for testing
        if (world?.isClient == true) return ActionResult.PASS
        val blockEntity = world?.getBlockEntity(pos) as? SieveBlockEntity ?: return ActionResult.PASS
        ItemScatterer.spawn(world, pos, DefaultedList.copyOf(ItemStack.EMPTY, *blockEntity.getLoot(world as ServerWorld, Blocks.SAND).toTypedArray()))
        return super.onUse(state, world, pos, player, hand, hit)
    }
}