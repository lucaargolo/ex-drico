package io.github.cafeteriaguild.exdrico.common.blockentities

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.common.recipes.VatRecipe
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ItemScatterer
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList

class VatBlockEntity(block: VatBlock): SyncedBlockEntity(block), Tickable {

    var lastRenderedFluid = 0f
    val inv = object : FullFixedItemInv(1) {
        override fun getMaxAmount(slot: Int, stack: ItemStack?): Int = 1
    }
    val fluidInv = SimpleFixedFluidInv(1, FluidAmount.BUCKET)
    private var currentRecipe: VatRecipe? = null
    var progress = 0

    override fun toTag(tag: CompoundTag): CompoundTag {
        val invTag = inv.toTag()
        val fluidInvTag = fluidInv.toTag()
        tag.put("inv", invTag)
        tag.put("fluidInv", fluidInvTag)
        tag.putInt("progress", progress)
        tag.putString("currentRecipe", currentRecipe?.id?.toString() ?: "")
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        inv.fromTag(tag.getCompound("inv"))
        fluidInv.fromTag(tag.getCompound("fluidInv"))
        progress = tag.getInt("progress")
        currentRecipe = (world as? ServerWorld)?.recipeManager?.listAllOfType(VatRecipe.TYPE)?.firstOrNull { it.id.toString() == tag.getString("currentRecipe")}
        super.fromTag(state, tag)
    }

    override fun tick() {
        if (world?.isClient == true) return
        if (currentRecipe != null && progress <= 0) {
            if (currentRecipe!!.fluidInput != null)
                fluidInv.extract(currentRecipe!!.fluidInput!!.amount())
            inv.extract(1)
            ItemScatterer.spawn(world, pos.up(), DefaultedList.ofSize(1, currentRecipe!!.out.copy()))
            currentRecipe = null
        }
    }

    fun getRecipe(world: ServerWorld): VatRecipe? {
        if (currentRecipe == null || !currentRecipe!!.matches(inv, fluidInv)) {
            currentRecipe = world.recipeManager.listAllOfType(VatRecipe.TYPE).firstOrNull { it.matches(inv, fluidInv) }
            progress = currentRecipe?.ticks ?: 0
        }
        return currentRecipe
    }
}