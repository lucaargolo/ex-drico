package io.github.cafeteriaguild.exdrico.common.blockentities

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.common.recipes.VatRecipe
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ItemScatterer
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList

class VatBlockEntity(block: VatBlock): SyncedBlockEntity(block), Tickable {
    val inv = object : FullFixedItemInv(1) {
        override fun getMaxAmount(slot: Int, stack: ItemStack?): Int = 1
    }
    val fluidInv = SimpleFixedFluidInv(1, FluidAmount.BUCKET)
    private var currentRecipe: VatRecipe? = null
    var progress = 0

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