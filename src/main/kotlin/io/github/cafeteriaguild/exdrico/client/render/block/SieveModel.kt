package io.github.cafeteriaguild.exdrico.client.render.block

import com.mojang.datafixers.util.Pair
import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.items.MeshItem
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.GeometryHelper
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockRenderView
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

class SieveModel: UnbakedModel, BakedModel, FabricBakedModel {

    private val spriteIdentifierCollection = mutableListOf(
        SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("minecraft:block/acacia_planks"))
    )
    private val spriteArray = arrayOfNulls<Sprite>(1)

    private val modelIdentifierCollection = mutableListOf<Identifier>(
        ModelIdentifier(ModIdentifier("sieve"), ""),
        ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory")
    )
    private val modelArray = arrayOfNulls<BakedModel>(2)

    //Get baked model dependencies
    override fun getModelDependencies() = modelIdentifierCollection
    override fun getTextureDependencies(unbakedModelGetter: Function<Identifier, UnbakedModel>?, unresolvedTextureReferences: MutableSet<Pair<String, String>>?) = spriteIdentifierCollection


    //Get actual baked model
    override fun bake(loader: ModelLoader, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings?, modelId: Identifier?): BakedModel {
        modelIdentifierCollection.forEachIndexed { idx, modelIdentifier ->
            modelArray[idx] = loader.getOrLoadModel(modelIdentifier).bake(loader, textureGetter, rotationContainer, modelId)
        }
        spriteIdentifierCollection.forEachIndexed { idx, spriteIdentifier ->
            spriteArray[idx] = textureGetter.apply(spriteIdentifier)
        }
        return this
    }

    //Dummy baked model returns
    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY
    override fun getQuads(state: BlockState?, face: Direction?, random: Random?): MutableList<BakedQuad> = mutableListOf()
    override fun getSprite() = spriteArray[0]

    //Dummy baked model configs
    override fun useAmbientOcclusion() = true
    override fun isBuiltin() = false
    override fun isSideLit() = false
    override fun hasDepth() = false

    //Get stone block transformation
    override fun getTransformation() = MinecraftClient.getInstance().bakedModelManager.getModel(ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory")).transformation

    //Set false to use fabric renderer
    override fun isVanillaAdapter() = false

    override fun emitBlockQuads(worldView: BlockRenderView, state: BlockState, pos: BlockPos, randSupplier: Supplier<Random>, context: RenderContext) {

        context.pushTransform { quad ->
            quad.nominalFace(GeometryHelper.lightFace(quad))
            quad.spriteColor(0, -1, -1, -1, -1)
            quad.spriteBake(0, spriteArray[0], MutableQuadView.BAKE_LOCK_UV)
            true
        }

        var emitter = context.emitter
        modelArray[0]?.getQuads(null, null, randSupplier.get())?.forEach(Consumer { q: BakedQuad ->
            emitter.fromVanilla(q.vertexData, 0, false)
            emitter.emit()
        })

        context.popTransform()

        (worldView.getBlockEntity(pos) as? SieveBlockEntity)?.meshType?.let {
            val spriteIdentifier = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, it.texture)
            val sprite = spriteIdentifier.sprite

            context.pushTransform { quad ->
                quad.nominalFace(GeometryHelper.lightFace(quad))
                quad.spriteColor(0, -1, -1, -1, -1)
                quad.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV)
                true
            }

            emitter = context.emitter
            modelArray[1]?.getQuads(null, null, randSupplier.get())?.forEach(Consumer { q: BakedQuad ->
                emitter.fromVanilla(q.vertexData, 0, false)
                emitter.emit()
            })

            context.popTransform()
        }

    }

    override fun emitItemQuads(stack: ItemStack, randSupplier: Supplier<Random>, context: RenderContext) { }


}