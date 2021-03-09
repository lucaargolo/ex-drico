package io.github.cafeteriaguild.exdrico.common.blockentities

import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.mixin.AccessorLootContextTypes
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextType
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.registry.Registry

class SieveBlockEntity(block: SieveBlock): SyncedBlockEntity<SieveBlock>(block) {

    var meshType: MeshType? = null
    var processingBlock: Block? = null

    var progress = 0

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        val blockId = Identifier(tag.getString("block"))
        processingBlock = if(Registry.BLOCK.get(blockId) == Blocks.AIR) processingBlock else Registry.BLOCK.get(blockId)
        meshType = MeshType.TYPES[Identifier(tag.getString("mesh"))] ?: meshType
        progress = tag.getInt("progress")
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putString("block", processingBlock?.let {Registry.BLOCK.getId(it).toString()} ?: "")
        tag.putString("mesh", meshType?.identifier?.toString() ?: "")
        tag.putInt("progress", progress)
        return super.toTag(tag)
    }

    companion object {
        val SIEVE_LOOT_CONTEXT: LootContextType = LootContextType.Builder().build()
        init {
            AccessorLootContextTypes.getMap()[ModIdentifier("sieve")] = SIEVE_LOOT_CONTEXT
        }

        fun getLoot(world: ServerWorld, block: Block, meshType: MeshType): DefaultedList<ItemStack> {
            val blockIdentifier = Registry.BLOCK.getId(block).path
            val table = world.server.lootManager.getTable(Identifier(meshType.identifier.namespace, "sieve/${meshType.identifier.path}/$blockIdentifier"))
            val ctx = LootContext.Builder(world).random(world.random).build(SIEVE_LOOT_CONTEXT)
            val stackList = table.generateLoot(ctx)
            val finalList = DefaultedList.ofSize(stackList.size, ItemStack.EMPTY)
            stackList.forEachIndexed { idx, stack ->
                finalList[idx] = stack
            }
            return finalList
        }
    }
}