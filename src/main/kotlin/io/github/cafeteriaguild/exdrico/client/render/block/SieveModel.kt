package io.github.cafeteriaguild.exdrico.client.render.block

import com.mojang.datafixers.util.Pair
import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.emitFromVanilla
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.GeometryHelper
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockRenderView
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

class SieveModel: UnbakedModel, BakedModel, FabricBakedModel {

    private val spriteArray = arrayOfNulls<Sprite>(SieveBlock.sprites.size)

    private val modelIdentifierCollection = mutableListOf<Identifier>(
        ModelIdentifier(ModIdentifier("sieve"), ""),
        ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory")
    )
    private val modelArray = arrayOfNulls<BakedModel>(2)

    //Get baked model dependencies
    override fun getModelDependencies() = modelIdentifierCollection
    override fun getTextureDependencies(unbakedModelGetter: Function<Identifier, UnbakedModel>?, unresolvedTextureReferences: MutableSet<Pair<String, String>>?) = SieveBlock.sprites

    //Get actual baked model
    override fun bake(loader: ModelLoader, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings?, modelId: Identifier?): BakedModel {
        modelIdentifierCollection.forEachIndexed { idx, modelIdentifier ->
            modelArray[idx] = loader.getOrLoadModel(modelIdentifier).bake(loader, textureGetter, rotationContainer, modelId)
        }
        SieveBlock.sprites.forEachIndexed { idx, spriteIdentifier ->
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
    override fun isSideLit() = true
    override fun hasDepth() = false

    //Get stone block transformation
    override fun getTransformation(): ModelTransformation = MinecraftClient.getInstance().bakedModelManager.getModel(ModelIdentifier(Identifier("stone"), "")).transformation

    //Set false to use fabric renderer
    override fun isVanillaAdapter() = false

    override fun emitBlockQuads(worldView: BlockRenderView, state: BlockState, pos: BlockPos, randSupplier: Supplier<Random>, context: RenderContext) {
        val block = worldView.getBlockState(pos).block as? SieveBlock ?: return
        val meshType = (worldView.getBlockEntity(pos) as? SieveBlockEntity)?.meshType
        emitCommonQuads(context, block, randSupplier, meshType)
    }

    override fun emitItemQuads(stack: ItemStack, randSupplier: Supplier<Random>, context: RenderContext) {
        val block = (stack.item as? BlockItem)?.block as? SieveBlock ?: return
        val tag = stack.orCreateTag
        val meshType = MeshType.TYPES[Identifier.tryParse(tag.getString("mesh"))]
        emitCommonQuads(context, block, randSupplier, meshType)
    }

    @Suppress("DEPRECATION")
    private fun emitCommonQuads(context: RenderContext, block: SieveBlock, randSupplier: Supplier<Random>, meshType: MeshType? = null) {
        context.pushTransform { quad ->
            quad.nominalFace(GeometryHelper.lightFace(quad))
            quad.spriteColor(0, -1, -1, -1, -1)
            quad.spriteBake(0, spriteArray[block.spriteId], MutableQuadView.BAKE_LOCK_UV)
            true
        }

        modelArray[0]?.emitFromVanilla(context, randSupplier) { true }

        context.popTransform()

        meshType?.let {
            context.pushTransform { quad ->
                quad.nominalFace(GeometryHelper.lightFace(quad))
                quad.spriteColor(0, -1, -1, -1, -1)
                quad.spriteBake(0, it.sprite, MutableQuadView.BAKE_LOCK_UV)
                true
            }

            modelArray[1]?.emitFromVanilla(context, randSupplier) { true }

            context.popTransform()
        }
    }


}