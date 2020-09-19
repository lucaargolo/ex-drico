package io.github.cafeteriaguild.exdrico.common.blockentities

import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.mixin.AccessorLootContextTypes
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextType
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.registry.Registry

class SieveBlockEntity(block: SieveBlock): SyncedBlockEntity(block) {

    var progress = 0

    fun getLoot(world: ServerWorld, block: Block): List<ItemStack> {
        val identifier = Registry.BLOCK.getId(block).path
        val table = world.server.lootManager.getTable(ModIdentifier("sieve/$identifier"))
        val ctx = LootContext.Builder(world).random(world.random).build(SIEVE_LOOT_CONTEXT)
        return table.generateLoot(ctx)
    }

    companion object {
        val SIEVE_LOOT_CONTEXT = LootContextType.Builder().build()
        init {
            AccessorLootContextTypes.getMap()[ModIdentifier("sieve")] = SIEVE_LOOT_CONTEXT
        }
    }
}