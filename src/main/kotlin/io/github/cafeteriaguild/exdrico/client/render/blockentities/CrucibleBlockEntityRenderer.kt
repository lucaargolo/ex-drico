package io.github.cafeteriaguild.exdrico.client.render.blockentities

import io.github.cafeteriaguild.exdrico.common.blockentities.CrucibleBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack

class CrucibleBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<CrucibleBlockEntity>(dispatcher) {

    override fun render(entity: CrucibleBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {

    }


}