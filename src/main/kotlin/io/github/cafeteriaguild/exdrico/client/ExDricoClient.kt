package io.github.cafeteriaguild.exdrico.client

import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import io.github.cafeteriaguild.exdrico.client.render.block.SieveModel
import io.github.cafeteriaguild.exdrico.client.render.blockentities.SieveBlockEntityRenderer
import io.github.cafeteriaguild.exdrico.client.render.item.MeshModel
import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.ACACIA_SIEVE
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.BIRCH_SIEVE
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.DARK_OAK_SIEVE
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.JUNGLE_SIEVE
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.OAK_SIEVE
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium.SPRUCE_SIEVE
import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.registry.Registry
import java.util.function.Consumer

@Suppress("UNCHECKED_CAST")
class ExDricoClient: ClientModInitializer {

    private val sieveModel = SieveModel()
    private val customModelMap = linkedMapOf<ModelIdentifier, UnbakedModel>(
        Pair(ModelIdentifier(ModIdentifier("mesh"), "inventory"), MeshModel()),
    )

    override fun onInitializeClient() {
        ClientPacketCompendium.initPackets()

        //Register the sieve_mesh model before the mesh baked model
        ModelLoadingRegistry.INSTANCE.registerAppender { _: ResourceManager?, out: Consumer<ModelIdentifier?> ->
            out.accept(ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory"))
        }

        //Register custom baked models
        ModelLoadingRegistry.INSTANCE.registerVariantProvider {
            ModelVariantProvider { modelIdentifier, _ ->
                customModelMap.forEach { (modelId, unbakedModel) ->
                    if(modelIdentifier == modelId)
                        return@ModelVariantProvider unbakedModel
                }
                SieveBlock.sieveMap.forEach { (_, sieve) ->
                    val identifier = Registry.BLOCK.getId(sieve)
                    if(modelIdentifier.namespace == identifier.namespace && modelIdentifier.path == identifier.path)
                        return@ModelVariantProvider sieveModel
                }
                return@ModelVariantProvider null
            }
        }

        //Register custom block entity renderers
        SieveBlock.sieveMap.forEach { (_, sieve) ->
            BlockEntityRendererRegistry.INSTANCE.register(BlockCompendium.getEntityType(sieve) as BlockEntityType<SieveBlockEntity>) { SieveBlockEntityRenderer(it) }
        }
    }

}