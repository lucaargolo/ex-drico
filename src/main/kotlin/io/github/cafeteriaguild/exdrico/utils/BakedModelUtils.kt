package io.github.cafeteriaguild.exdrico.utils

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.util.math.Direction
import java.util.*
import java.util.function.Supplier

@Suppress("DEPRECATION")
fun BakedModel.emitFromVanilla(context: RenderContext, randSupplier: Supplier<Random>, shouldEmit: (BakedQuad) -> Boolean) {
    val emitter = context.emitter
    Direction.values().forEach { dir ->
        getQuads(null, dir, randSupplier.get()).forEach { quad ->
            if (shouldEmit(quad)) {
                emitter.fromVanilla(quad.vertexData, 0, false)
                emitter.emit()
            }
        }
    }
    getQuads(null, null, randSupplier.get()).forEach { quad ->
        if (shouldEmit(quad)) {
            emitter.fromVanilla(quad.vertexData, 0, false)
            emitter.emit()
        }
    }
}