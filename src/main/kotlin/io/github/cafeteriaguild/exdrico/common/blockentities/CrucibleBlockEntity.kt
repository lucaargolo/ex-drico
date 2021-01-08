package io.github.cafeteriaguild.exdrico.common.blockentities

import io.github.cafeteriaguild.exdrico.common.blocks.CrucibleBlock
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable

class CrucibleBlockEntity(block: CrucibleBlock): SyncedBlockEntity<CrucibleBlock>(block), Tickable {

    val wooden = block.isBurnable

    override fun toTag(tag: CompoundTag): CompoundTag {
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
    }

    override fun tick() {

    }
}