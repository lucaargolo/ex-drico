package io.github.cafeteriaguild.exdrico.client.render.blockentities

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import java.awt.Color

class SieveBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher): BlockEntityRenderer<SieveBlockEntity>(dispatcher) {

    class UV(var minU: Float, var minV: Float, var maxU: Float, var maxV: Float) {
        constructor(sprite: Sprite): this(sprite.minU, sprite.minV, sprite.maxU, sprite.maxV)
    }

    override fun render(entity: SieveBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        val block = entity.block ?: return
        val blockIdentifier = Registry.BLOCK.getId(block)
        val spriteIdentifier = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier(blockIdentifier.namespace, "block/"+blockIdentifier.path))
        val sprite = spriteIdentifier.sprite
        val color = Color.WHITE

        matrices.push()
        matrices.translate(0.0, 0.75, 0.0)

        var p = 4f*((100-entity.progress)/100f)

        val bb = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE))
        val entry = matrices.peek()

        val partUv = UV(sprite)
        partUv.maxV -= (sprite.maxV - sprite.minV)*((16f-p)/16f)
        
        p /= 16f
        p = MathHelper.lerp(p, 0.0626f, 0.9374f)

        renderVertices(bb, entry, Direction.SOUTH.unitVector, color, overlay, light, partUv, 0.0626f, 0.9374f, 0.0626f, p, 0.9374f, 0.9374f, 0.9374f, 0.9374f) //Direction.SOUTH
        renderVertices(bb, entry, Direction.NORTH.unitVector, color, overlay, light, partUv, 0.0626f, 0.9374f, p, 0.0626f, 0.0626f, 0.0626f, 0.0626f, 0.0626f) //Direction.NORTH
        renderVertices(bb, entry, Direction.EAST.unitVector, color, overlay, light, partUv, 0.9374f, 0.9374f, p, 0.0626f, 0.0626f, 0.9374f, 0.9374f, 0.0626f) //Direction.EAST
        renderVertices(bb, entry, Direction.WEST.unitVector, color, overlay, light, partUv, 0.0626f, 0.0626f, 0.0626f, p, 0.0626f, 0.9374f, 0.9374f, 0.0626f) //Direction.WEST

        val fullUv = UV(sprite)

        renderVertices(bb, entry, Direction.DOWN.unitVector, color, overlay, light, fullUv, 0.0626f, 0.9374f, 0.0626f, 0.0626f, 0.0626f, 0.0626f, 0.9374f, 0.9374f) //Direction.DOWN
        renderVertices(bb, entry, Direction.UP.unitVector, color, overlay, light, fullUv, 0.0626f, 0.9374f, p, p, 0.9374f, 0.9374f, 0.0626f, 0.0626f) //Direction.UP

        matrices.pop()
    }

    private fun renderVertices(bb: VertexConsumer, entry: MatrixStack.Entry, normal: Vector3f, color: Color, overlay: Int, light: Int, uv: UV, f: Float, g: Float, h: Float, i: Float, j: Float, k: Float, l: Float, m: Float) {
        bb.vertex(entry.model, f, h, j).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(uv.maxU, uv.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, g, h, k).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(uv.minU, uv.minV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, g, i, l).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(uv.minU, uv.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
        bb.vertex(entry.model, f, i, m).color(color.red/255f, color.green/255f, color.blue/255f, 1f).texture(uv.maxU, uv.maxV).overlay(overlay).light(light).normal(entry.normal, normal.x, normal.y, normal.z).next()
    }

}