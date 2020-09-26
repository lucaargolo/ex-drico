package io.github.cafeteriaguild.exdrico.common.blockentities

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv
import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.common.recipes.VatRecipe
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Tickable

class VatBlockEntity(block: VatBlock): SyncedBlockEntity(block), Tickable {

    var lastRenderedFluid = 0f
    val inv = object : FullFixedItemInv(1) {
        override fun getMaxAmount(slot: Int, stack: ItemStack?): Int = 1
    }
    val fluidInv = SimpleFixedFluidInv(1, FluidAmount.BUCKET)

    private var currentRecipe: VatRecipe? = null

    var finalStack: ItemStack = ItemStack.EMPTY
    var finalProgress = 0f
    var remainingProgress = 0f
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
        tag.putString("currentRecipe", currentRecipe?.id?.toString() ?: "")
        tag.put("finalStack", finalStack.toTag(CompoundTag()))
        return super.toTag(tag)
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        inv.fromTag(tag.getCompound("inv"))
        fluidInv.fromTag(tag.getCompound("fluidInv"))
        remainingProgress = tag.getFloat("progress")
        finalProgress = tag.getFloat("finalProgress")
        currentRecipe = (world as? ServerWorld)?.recipeManager?.listAllOfType(VatRecipe.TYPE)?.firstOrNull { it.id.toString() == tag.getString("currentRecipe")}
        finalStack = ItemStack.fromTag(tag.getCompound("finalStack"))
        super.fromTag(state, tag)
    }

    override fun tick() {
        if (world?.isClient == true) return
        if (currentRecipe != null && remainingProgress <= 0 && remainingTicks <= 0) {
            if (currentRecipe!!.fluidInput != null)
                fluidInv.extract(currentRecipe!!.fluidInput!!.amount())
            inv.setInvStack(0, currentRecipe!!.output.copy(), Simulation.ACTION)
            currentRecipe = null
            world?.players?.forEach {
                (it as? ServerPlayerEntity)?.let { serverPlayerEntity ->
                    val attachedData = PacketByteBuf(Unpooled.buffer())
                    attachedData.writeBlockPos(pos)
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(serverPlayerEntity, ClientPacketCompendium.CLEAR_VAT_COLOR_S2C, attachedData)
                }
            }
            sync()
        } else if (remainingProgress <= 0 && remainingTicks > 0) remainingTicks--
    }

    fun getRecipe(world: ServerWorld): VatRecipe? {
        if (currentRecipe == null || !currentRecipe!!.matches(inv, fluidInv)) {
            currentRecipe = world.recipeManager.listAllOfType(VatRecipe.TYPE).firstOrNull { it.matches(inv, fluidInv) }
            finalStack = currentRecipe?.output ?: ItemStack.EMPTY
            finalProgress = currentRecipe?.cost?.toFloat() ?: 0f
            remainingProgress = currentRecipe?.cost?.toFloat() ?: 0f
            remainingTicks = currentRecipe?.ticks?.toFloat() ?: 0f
        }
        return currentRecipe
    }
}