package io.github.cafeteriaguild.exdrico.common.items

import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class MeshItem(settings: Settings) : Item(settings) {

    override fun getTranslationKey(stack: ItemStack): String {
        val meshType = getMeshType(stack)
        val identifier = meshType.identifier
        return "mesh.${identifier.namespace}.${identifier.path}"
    }

    companion object {

        fun getMeshType(stack: ItemStack): MeshType {
            val tag = stack.tag ?: return MeshType.EMPTY
            val type = tag.getString("mesh")
            val id = Identifier.tryParse(type) ?: return MeshType.EMPTY
            return MeshType.TYPES[id] ?: MeshType.EMPTY
        }
    }
}