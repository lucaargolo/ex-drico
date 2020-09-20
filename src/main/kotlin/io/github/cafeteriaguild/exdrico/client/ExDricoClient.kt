package io.github.cafeteriaguild.exdrico.client

import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import io.github.cafeteriaguild.exdrico.client.render.block.SieveModel
import io.github.cafeteriaguild.exdrico.client.render.blockentities.SieveBlockEntityRenderer
import io.github.cafeteriaguild.exdrico.client.render.item.MeshModel
import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.ACACIA_SIEVE
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import java.util.function.Consumer

class ExDricoClient: ClientModInitializer {

    val customModelMap = linkedMapOf<ModelIdentifier, UnbakedModel>(
        Pair(ModelIdentifier(ModIdentifier("mesh"), "inventory"), MeshModel()),
        Pair(ModelIdentifier(ModIdentifier("acacia_sieve"), ""), SieveModel())
    )

    override fun onInitializeClient() {
        ClientPacketCompendium.initPackets()

        ModelLoadingRegistry.INSTANCE.registerAppender { _: ResourceManager?, out: Consumer<ModelIdentifier?> ->
            out.accept(ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory"))
        }
        ModelLoadingRegistry.INSTANCE.registerVariantProvider {
            ModelVariantProvider { modelIdentifier, _ ->
                customModelMap.forEach { (modelId, unbakedModel) ->
                    if(modelIdentifier == modelId)
                        return@ModelVariantProvider unbakedModel
                }
                return@ModelVariantProvider null
            }
        }
        BlockEntityRendererRegistry.INSTANCE.register(BlockCompendium.getEntityType(ACACIA_SIEVE) as BlockEntityType<SieveBlockEntity>) { SieveBlockEntityRenderer(it) }
    }

}