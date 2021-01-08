package io.github.cafeteriaguild.exdrico.common.blockentities

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.common.recipes.VatRecipe
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.SpawnReason
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Tickable

class VatBlockEntity(block: VatBlock): SyncedBlockEntity<VatBlock>(block), Tickable {

    var lastRenderedFluid = 0f
    val inv = object : FullFixedItemInv(1) {
        override fun setInvStack(slot: Int, to: ItemStack?, simulation: Simulation?): Boolean {
            return !(requiredTicks > 0 && remainingProgress <= 0) && super.setInvStack(slot, to, simulation)
        }

        override fun getMaxAmount(slot: Int, stack: ItemStack?): Int = 1
    }
    val fluidInv = object : SimpleFixedFluidInv(1, FluidAmount.BUCKET) {
        override fun setInvFluid(tank: Int, to: FluidVolume?, simulation: Simulation?): Boolean {
            return !(requiredTicks > 0 && remainingProgress <= 0) && super.setInvFluid(tank, to, simulation)
        }
    }
    var currentRecipe: VatRecipe? = null

    var finalStack: ItemStack = ItemStack.EMPTY
    var finalFluidVolume: FluidVolume = FluidKeys.EMPTY.withAmount(FluidAmount(0))
    var finalProgress = 0f
    var remainingProgress = 0f
        set(value) {
            field = value.coerceAtLeast(0f)
        }
    var requiredTicks = 0f
    var remainingTicks = 0f

    var sumQnt = 0
    var sumr = 0
    var sumg = 0
    var sumb = 0

    override fun toTag(tag: CompoundTag): CompoundTag {
        val invTag = inv.toTag()
        val fluidInvTag = fluidInv.toTag()
        tag.put("inv", invTag)
        tag.put("fluidInv", fluidInvTag)
        tag.putFloat("progress", remainingProgress)
        tag.putFloat("finalProgress", finalProgress)
        tag.putFloat("remainingTicks", remainingTicks)
        tag.putFloat("requiredTicks", requiredTicks)
        tag.putString("currentRecipe", currentRecipe?.id?.toString() ?: "")
        tag.put("finalStack", finalStack.toTag(CompoundTag()))
        tag.put("finalFluidVolume", finalFluidVolume.toTag(CompoundTag()))
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        inv.fromTag(tag.getCompound("inv"))
        fluidInv.fromTag(tag.getCompound("fluidInv"))
        remainingProgress = tag.getFloat("progress")
        finalProgress = tag.getFloat("finalProgress")
        remainingTicks = tag.getFloat("remainingTicks")
        requiredTicks = tag.getFloat("requiredTicks")
        currentRecipe = (world as? ServerWorld)?.recipeManager?.listAllOfType(VatRecipe.TYPE)?.firstOrNull { it.id.toString() == tag.getString("currentRecipe")}
        finalStack = ItemStack.fromTag(tag.getCompound("finalStack"))
        finalFluidVolume = FluidVolume.fromTag(tag.getCompound("finalFluidVolume"))
        super.fromTag(state, tag)
    }

    override fun tick() {
        if (currentRecipe != null && remainingProgress <= 0 && remainingTicks <= 0) {
            requiredTicks = 0f
            remainingTicks = 0f
            remainingProgress = 0f
            finalProgress = 0f
            if (currentRecipe!!.fluidInput != null)
                fluidInv.extract(currentRecipe!!.fluidInput!!.amount())
            if (currentRecipe!!.fluidOut != null)
                fluidInv.insert(currentRecipe!!.fluidOut)
            if (currentRecipe!!.output.item is SpawnEggItem) {
                val entityType = (currentRecipe!!.output.item as SpawnEggItem).getEntityType(null)
                (world as? ServerWorld)?.let { world ->
                    val entity = entityType.create(world, null, null, null, pos.up(), SpawnReason.MOB_SUMMONED, true, false)
                    world.spawnEntity(entity)
                }
            } else
                inv.setInvStack(0, currentRecipe!!.output.copy(), Simulation.ACTION)
            currentRecipe = null
            finalStack = ItemStack.EMPTY
            finalFluidVolume = FluidKeys.EMPTY.withAmount(FluidAmount(0))
            markDirtyAndSync()
        } else if (remainingProgress <= 0 && remainingTicks > 0) remainingTicks--
    }

    fun getRecipe(world: ServerWorld): VatRecipe? {
        val blockBelow = world.getBlockState(pos.down()).block
        if (currentRecipe == null || !currentRecipe!!.matches(inv, fluidInv, blockBelow)) {
            currentRecipe = world.recipeManager.listAllOfType(VatRecipe.TYPE).firstOrNull { it.matches(inv, fluidInv, blockBelow) }
            finalStack = currentRecipe?.output ?: ItemStack.EMPTY
            remainingProgress = currentRecipe?.cost?.toFloat() ?: 0f
            finalProgress = remainingProgress
            remainingTicks = currentRecipe?.ticks?.toFloat() ?: 0f
            requiredTicks = remainingTicks
        }
        return currentRecipe
    }
}