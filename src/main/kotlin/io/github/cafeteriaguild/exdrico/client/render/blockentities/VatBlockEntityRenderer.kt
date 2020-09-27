package io.github.cafeteriaguild.exdrico.client.render.blockentities

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.item.filter.ConstantItemFilter
import io.github.cafeteriaguild.exdrico.common.blockentities.VatBlockEntity
import io.github.cafeteriaguild.exdrico.utils.SpriteColorCache
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItem
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import java.awt.Color

class VatBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<VatBlockEntity>(dispatcher) {

    override fun render(entity: VatBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {

        val entry = matrices.peek()
        val normal = Direction.UP.unitVector

        val fluidInv = entity.fluidInv
        val fluid = fluidInv.getInvFluid(0).rawFluid ?: Fluids.EMPTY

        FluidRenderHandlerRegistry.INSTANCE.get(fluid)?.let { fluidRenderHandler ->
            val fluidColor = fluidRenderHandler.getFluidColor(entity.world, entity.pos, fluid.defaultState)
            val fluidSprite = fluidRenderHandler.getFluidSprites(entity.world, entity.pos, fluid.defaultState)[0]
            val fluidBuffer = vertexConsumers.getBuffer(if(fluid != Fluids.EMPTY) RenderLayers.getFluidLayer(fluid.defaultState) else RenderLayer.getEntityTranslucent(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))

            val fp = MathHelper.lerp(tickDelta, entity.lastRenderedFluid, fluidInv.getInvFluid(0).amount().asLong(1000L)/1000f)
            entity.lastRenderedFluid = fp

            fluidBuffer.vertex(entry.model, 0.125f, fp-0.0625f, 0.875f).color((fluidColor shr 16 and 255)/255f, (fluidColor shr 8 and 255)/255f, (fluidColor and 255)/255f, 1f).texture(fluidSprite.maxU, fluidSprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
            fluidBuffer.vertex(entry.model, 0.875f, fp-0.0625f, 0.875f).color((fluidColor shr 16 and 255)/255f, (fluidColor shr 8 and 255)/255f, (fluidColor and 255)/255f, 1f).texture(fluidSprite.minU, fluidSprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
            fluidBuffer.vertex(entry.model, 0.875f, fp-0.0625f, 0.125f).color((fluidColor shr 16 and 255)/255f, (fluidColor shr 8 and 255)/255f, (fluidColor and 255)/255f, 1f).texture(fluidSprite.minU, fluidSprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
            fluidBuffer.vertex(entry.model, 0.125f, fp-0.0625f, 0.125f).color((fluidColor shr 16 and 255)/255f, (fluidColor shr 8 and 255)/255f, (fluidColor and 255)/255f, 1f).texture(fluidSprite.maxU, fluidSprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        }

        val inv = entity.inv
        val stack = inv.attemptExtraction(ConstantItemFilter.ANYTHING, 1, Simulation.SIMULATE)

        val block = (entity.finalStack.item as? BlockItem)?.block ?: (stack.item as? BlockItem)?.block ?: return
        val blockIdentifier = Registry.BLOCK.getId(block)
        val blockSpriteIdentifier = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier(blockIdentifier.namespace, "block/"+blockIdentifier.path))
        val blockSprite = blockSpriteIdentifier.sprite

        val currentColor = if(entity.finalStack.item is BlockItem && entity.sumQnt > 0) Color(entity.sumr/entity.sumQnt, entity.sumg/entity.sumQnt, entity.sumb/entity.sumQnt) else Color.WHITE
        val finalColor = Color.WHITE

        val delta = (entity.requiredTicks-entity.remainingTicks)/entity.requiredTicks
        val ir = MathHelper.lerp(delta, currentColor.red.toFloat(), finalColor.red.toFloat())/255f
        val ig = MathHelper.lerp(delta, currentColor.green.toFloat(), finalColor.green.toFloat())/255f
        val ib = MathHelper.lerp(delta, currentColor.blue.toFloat(), finalColor.blue.toFloat())/255f

        val blockColor = Color(ir, ig, ib)

        entity.sumQnt = 1
        entity.sumr = (ir*255).toInt()
        entity.sumg = (ig*255).toInt()
        entity.sumb = (ib*255).toInt()

        val bp = if(entity.finalStack.item is BlockItem) (entity.finalProgress-entity.remainingProgress)/entity.finalProgress else 1f

        val blockBuffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))

        blockBuffer.vertex(entry.model, 0.126f, bp-0.0625f, 0.874f).color(blockColor.red/255f, blockColor.green/255f, blockColor.blue/255f, 1f).texture(blockSprite.maxU, blockSprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        blockBuffer.vertex(entry.model, 0.874f, bp-0.0625f, 0.874f).color(blockColor.red/255f, blockColor.green/255f, blockColor.blue/255f, 1f).texture(blockSprite.minU, blockSprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        blockBuffer.vertex(entry.model, 0.874f, bp-0.0625f, 0.126f).color(blockColor.red/255f, blockColor.green/255f, blockColor.blue/255f, 1f).texture(blockSprite.minU, blockSprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        blockBuffer.vertex(entry.model, 0.126f, bp-0.0625f, 0.126f).color(blockColor.red/255f, blockColor.green/255f, blockColor.blue/255f, 1f).texture(blockSprite.maxU, blockSprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()


    }


}