package io.github.cafeteriaguild.exdrico.compat

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import me.shedaniel.rei.api.EntryStack
import net.minecraft.block.Block
import net.minecraft.util.Identifier
import uk.me.desert_island.rer.rei_stuff.LootDisplay

class SieveLootDisplay(block: Block, lootTableId: Identifier): LootDisplay() {

    init {
        this.inputStack = EntryStack.create(block)
        this.lootTableId = lootTableId
        this.contextType = SieveBlockEntity.SIEVE_LOOT_CONTEXT
    }

    override fun getLocation() = lootTableId

    override fun getRecipeCategory() = REIPlugin.SIEVE



}