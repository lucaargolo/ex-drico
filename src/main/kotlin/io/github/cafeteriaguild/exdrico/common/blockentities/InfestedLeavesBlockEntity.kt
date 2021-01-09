package io.github.cafeteriaguild.exdrico.common.blockentities

import io.github.cafeteriaguild.exdrico.common.blocks.InfestedLeavesBlock
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable

class InfestedLeavesBlockEntity(block: InfestedLeavesBlock): SyncedBlockEntity<InfestedLeavesBlock>(block), Tickable {

    var isFinished = false
    var progress = 0f

    override fun tick() {
        if(!isFinished) {
            progress++
            if(progress >= TICKS_TO_COMPLETE) {
                isFinished = true
                if(world?.isClient == true) {
                    MinecraftClient.getInstance().worldRenderer.updateBlock(world, pos, cachedState, cachedState, 0)
                }
            }
        }
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putBoolean("finished", isFinished)
        tag.putFloat("progress", progress)
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        isFinished = tag.getBoolean("finished")
        progress = tag.getFloat("progress")
    }

    companion object {
        const val TICKS_TO_COMPLETE = 2400f
    }

}