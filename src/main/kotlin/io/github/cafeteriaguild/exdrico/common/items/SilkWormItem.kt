package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.common.blocks.InfestedLeavesBlock
import net.minecraft.block.LeavesBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class SilkWormItem(settings: Settings) : Item(settings) {

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val stack = context.stack
        val world = context.world
        val pos = context.blockPos
        val state = world.getBlockState(pos)
        val block = state.block
        val newBlock = (block as? LeavesBlock)?.let { InfestedLeavesBlock.infestedLeavesMap[it] } ?: return super.useOnBlock(context)
        val newState = newBlock.defaultState.with(LeavesBlock.DISTANCE, state[LeavesBlock.DISTANCE]).with(LeavesBlock.PERSISTENT, state[LeavesBlock.PERSISTENT])
        stack.decrement(1)
        world.setBlockState(pos, newState)
        return ActionResult.CONSUME
    }
}