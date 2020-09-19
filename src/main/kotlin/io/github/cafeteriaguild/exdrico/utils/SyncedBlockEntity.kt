package io.github.cafeteriaguild.exdrico.utils

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag

open class SyncedBlockEntity(val block: Block): BlockEntity(TODO()), BlockEntityClientSerializable {

    override fun fromClientTag(tag: CompoundTag?) = fromTag(block.defaultState, tag)

    override fun toClientTag(tag: CompoundTag?) = toTag(tag)

}