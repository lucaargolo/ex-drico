package io.github.cafeteriaguild.exdrico.utils

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

typealias RenderItemHook = (stack: ItemStack, mode: ModelTransformation.Mode, matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, lightmap: Int, overlay: Int) -> Unit

object RenderItemUtils {

    private val registeredHooks = linkedSetOf<RenderItemHook>()

    fun registerFabricBakedItemHook(hook: RenderItemHook) {
        registeredHooks.add(hook)
    }

    fun renderFabricBakedItemHooks(stack: ItemStack, mode: ModelTransformation.Mode, matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, lightmap: Int, overlay: Int) {
        registeredHooks.forEach {
            it(stack, mode, matrixStack, vertexConsumerProvider, lightmap, overlay)
        }
        registeredHooks.clear()
    }


}

