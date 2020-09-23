package io.github.cafeteriaguild.exdrico.client.render.blockentities

import io.github.cafeteriaguild.exdrico.common.blockentities.VatBlockEntity
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.fluid.Fluids
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import java.awt.Color

class VatBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<VatBlockEntity>(dispatcher) {

    override fun render(entity: VatBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {

        val fluidInv = entity.fluidInv
        val fluid = fluidInv.getInvFluid(0).rawFluid ?: Fluids.EMPTY

        val fluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid) ?: return
        val fluidColor = fluidRenderHandler.getFluidColor(entity.world, entity.pos, fluid.defaultState)
        val sprite = fluidRenderHandler.getFluidSprites(entity.world, entity.pos, fluid.defaultState)[0]
        val color = Color((fluidColor shr 16 and 255), (fluidColor shr 8 and 255), (fluidColor and 255))

        val bb = vertexConsumers.getBuffer(if(fluid != Fluids.EMPTY) RenderLayers.getFluidLayer(fluid.defaultState) else RenderLayer.getEntityTranslucent(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))
        val entry = matrices.peek()
        val normal = Direction.NORTH.unitVector

        val p = MathHelper.lerp(tickDelta, entity.lastRenderedFluid, fluidInv.getInvFluid(0).amount().asLong(1000L)/1000f)
        entity.lastRenderedFluid = p

        bb.vertex(entry.model, 0.126f, p, 0.874f).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.maxU, sprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, 0.874f, p, 0.874f).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.minU, sprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, 0.874f, p, 0.126f).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.minU, sprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, 0.126f, p, 0.126f).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.maxU, sprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()

    }


}