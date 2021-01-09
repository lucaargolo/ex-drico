package io.github.cafeteriaguild.exdrico.client.render.blockentities

import io.github.cafeteriaguild.exdrico.common.blockentities.InfestedLeavesBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry

class InfestedLeavesBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<InfestedLeavesBlockEntity>(dispatcher) {

    override fun render(entity: InfestedLeavesBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        if(entity.isFinished) return

        val leavesBlock = entity.block.parent
        val leavesBlockIdentifier = Registry.BLOCK.getId(leavesBlock)

        val initialColor = MinecraftClient.getInstance().blockColors.getColor(leavesBlock.defaultState, dispatcher.world, entity.pos, 0)

        val p = (entity.progress/InfestedLeavesBlockEntity.TICKS_TO_COMPLETE).coerceAtMost(1f)

        val red = MathHelper.lerp(p, (initialColor shr 16 and 255).toFloat(), 255f)/255f
        val green = MathHelper.lerp(p, (initialColor shr 8 and 255).toFloat(), 255f)/255f
        val blue = MathHelper.lerp(p, (initialColor and 255).toFloat(), 255f)/255f

        val leavesModel = MinecraftClient.getInstance().bakedModelManager.getModel(ModelIdentifier(leavesBlockIdentifier, "distance=1,persistent=false"))
        MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()), leavesBlock.defaultState, leavesModel, red, green, blue, light, overlay)
    }



}