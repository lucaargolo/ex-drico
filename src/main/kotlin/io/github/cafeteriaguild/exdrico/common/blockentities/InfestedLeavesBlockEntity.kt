package io.github.cafeteriaguild.exdrico.common.blockentities

import io.github.cafeteriaguild.exdrico.common.blocks.InfestedLeavesBlock
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable

class InfestedLeavesBlockEntity(block: InfestedLeavesBlock): SyncedBlockEntity<InfestedLeavesBlock>(block), Tickable {

    val isFinished = false

    override fun toTag(tag: CompoundTag): CompoundTag {
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
    }

    override fun tick() {

    }
}