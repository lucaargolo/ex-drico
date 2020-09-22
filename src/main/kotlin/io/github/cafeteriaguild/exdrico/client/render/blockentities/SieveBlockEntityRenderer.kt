package io.github.cafeteriaguild.exdrico.client.render.blockentities

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

class SieveBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<SieveBlockEntity>(dispatcher) {

    override fun render(entity: SieveBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        val block = entity.block ?: return
        matrices.push()
        matrices.translate(0.5, 0.35+((1.0-(entity.progress/100.0))/4), 0.5)
        matrices.scale(3.49f, 1.725f, 3.49f)
        MinecraftClient.getInstance().itemRenderer.renderItem(ItemStack(block), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers)
        matrices.pop()
    }

}