package io.github.cafeteriaguild.exdrico.client

import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import io.github.cafeteriaguild.exdrico.client.render.block.SieveModel
import io.github.cafeteriaguild.exdrico.client.render.block.VatModel
import io.github.cafeteriaguild.exdrico.client.render.blockentities.SieveBlockEntityRenderer
import io.github.cafeteriaguild.exdrico.client.render.color.ColorModel
import io.github.cafeteriaguild.exdrico.client.render.color.ColoredBlock
import io.github.cafeteriaguild.exdrico.client.render.color.ColoredItem
import io.github.cafeteriaguild.exdrico.client.render.item.MeshModel
import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import io.github.cafeteriaguild.exdrico.common.blocks.ColorBlock
import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.common.items.ColorItem
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Consumer

@Suppress("UNCHECKED_CAST")
class ExDricoClient: ClientModInitializer {

    private val sieveModel = SieveModel()
    private val vatModel = VatModel()
    private val customModelMap = linkedMapOf<ModelIdentifier, UnbakedModel>(
        Pair(ModelIdentifier(ModIdentifier("mesh"), "inventory"), MeshModel()),
    )

    override fun onInitializeClient() {
        ClientPacketCompendium.initPackets()

        //Register some models required by baked models
        ModelLoadingRegistry.INSTANCE.registerAppender { _: ResourceManager?, out: Consumer<ModelIdentifier?> ->
            out.accept(ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory"))
            ColoredBlock.values().forEach {
                out.accept(ModelIdentifier(it.identifier, ""))
            }
            ColoredItem.values().forEach {
                out.accept(ModelIdentifier(it.identifier, "inventory"))
                out.accept(ModelIdentifier(Identifier(it.identifier.namespace, it.identifier.path+"_overlay"), "inventory"))
            }
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
                VatBlock.vatMap.forEach { (_, vat) ->
                    val identifier = Registry.BLOCK.getId(vat)
                    if(modelIdentifier.namespace == identifier.namespace && modelIdentifier.path == identifier.path)
                        return@ModelVariantProvider vatModel
                }
                ColorBlock.blockMap.keys.forEach { block ->
                    val identifier = Registry.BLOCK.getId(block)
                    if(modelIdentifier.namespace == identifier.namespace && modelIdentifier.path == identifier.path)
                        return@ModelVariantProvider ColorModel(true, ColoredBlock.values().toList().indexOf(block.colorModel))
                }
                ColorItem.itemMap.keys.forEach { item ->
                    val identifier = Registry.ITEM.getId(item)
                    if(modelIdentifier.namespace == identifier.namespace && modelIdentifier.path == identifier.path)
                        return@ModelVariantProvider ColorModel(false, ColoredBlock.values().size+(ColoredItem.values().toList().indexOf(item.colorModel)*2))
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