package io.github.cafeteriaguild.exdrico.utils

import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld

open class SyncedBlockEntity(private val block: Block): BlockEntity(BlockCompendium.getEntityType(block)), BlockEntityClientSerializable {

    fun markDirtyAndSyncRender() {
        if (world?.isClient == true)
            MinecraftClient.getInstance().worldRenderer.updateBlock(world, pos, cachedState, cachedState, 0)
        markDirtyAndSync()
    }

    fun markDirtyAndSync() {
        markDirty()
        (world as? ServerWorld)?.let { sync() }
    }

    override fun fromClientTag(tag: CompoundTag?) = fromTag(block.defaultState, tag)

    override fun toClientTag(tag: CompoundTag?) = toTag(tag)

}