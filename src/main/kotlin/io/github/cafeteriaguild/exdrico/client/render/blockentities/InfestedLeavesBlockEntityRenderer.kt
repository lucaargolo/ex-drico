package io.github.cafeteriaguild.exdrico.client.render.blockentities

import io.github.cafeteriaguild.exdrico.common.blockentities.InfestedLeavesBlockEntity
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import java.awt.Color

class InfestedLeavesBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<InfestedLeavesBlockEntity>(dispatcher) {

    override fun render(entity: InfestedLeavesBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {

        val leavesBlock = entity.block.parent
        val leavesBlockIdentifier = Registry.BLOCK.getId(leavesBlock)

        val spriteIdentifier = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier(leavesBlockIdentifier.namespace, "block/"+leavesBlockIdentifier.path))
        val sprite = spriteIdentifier.sprite
        val color = Color.WHITE

        matrices.push()

        val bb = vertexConsumers.getBuffer(RenderLayer.getCutoutMipped())
        val entry = matrices.peek()

        renderVertices(bb, entry, Direction.SOUTH.unitVector, color, overlay, light, sprite, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 1f) //Direction.SOUTH
        renderVertices(bb, entry, Direction.NORTH.unitVector, color, overlay, light, sprite, 0f, 1f, 1f, 0f, 0f, 0f, 0f, 0f) //Direction.NORTH
        renderVertices(bb, entry, Direction.EAST.unitVector, color, overlay, light, sprite, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 0f) //Direction.EAST
        renderVertices(bb, entry, Direction.WEST.unitVector, color, overlay, light, sprite, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f) //Direction.WEST
        renderVertices(bb, entry, Direction.DOWN.unitVector, color, overlay, light, sprite, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 1f) //Direction.DOWN
        renderVertices(bb, entry, Direction.UP.unitVector, color, overlay, light, sprite, 0f, 1f, 1f, 1f, 1f, 1f, 0f, 0f) //Direction.UP

        matrices.pop()
    }

    private fun renderVertices(bb: VertexConsumer, entry: MatrixStack.Entry, normal: Vector3f, color: Color, overlay: Int, light: Int, sprite: Sprite, f: Float, g: Float, h: Float, i: Float, j: Float, k: Float, l: Float, m: Float) {
        bb.vertex(entry.model, f, h, j).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.maxU, sprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, g, h, k).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.minU, sprite.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, g, i, l).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.minU, sprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, f, i, m).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(sprite.maxU, sprite.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
    }


}