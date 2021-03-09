package io.github.cafeteriaguild.exdrico.compat

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.items.ItemCompendium
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import me.shedaniel.rei.api.EntryStack
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import uk.me.desert_island.rer.rei_stuff.LootDisplay

class SieveLootDisplay(block: Block, val meshType: MeshType, lootTableId: Identifier): LootDisplay() {

    init {
        this.inputStack = EntryStack.create(block)
        this.lootTableId = lootTableId
        this.contextType = SieveBlockEntity.SIEVE_LOOT_CONTEXT
    }

    override fun getInputEntries(): MutableList<MutableList<EntryStack>> {
        val inputEntries = super.getInputEntries().toMutableList()
        val inputMesh = ItemStack(ItemCompendium.MESH)
        inputMesh.orCreateTag.putString("mesh", meshType.toString())
        inputEntries.add(mutableListOf(EntryStack.create(inputMesh)))
        return inputEntries
    }

    override fun getLocation() = lootTableId

    override fun getRecipeCategory() = REIPlugin.SIEVE



}