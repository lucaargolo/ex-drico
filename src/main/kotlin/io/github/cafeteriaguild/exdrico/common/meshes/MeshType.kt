package io.github.cafeteriaguild.exdrico.common.meshes

import net.minecraft.util.Identifier

data class MeshType(val texture: Identifier) {
    companion object {
        val TYPES = mutableMapOf<Identifier, MeshType>()
    }
}