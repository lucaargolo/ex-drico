package io.github.cafeteriaguild.exdrico

import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import io.github.cafeteriaguild.exdrico.common.meshes.MeshResource
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

class ExDrico: ModInitializer {

    companion object {
        const val MOD_ID = "exdrico"
    }

    override fun onInitialize() {
        BlockCompendium.initBlocks()

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MeshResource())
    }

}