package io.github.cafeteriaguild.exdrico.client.render.block

import com.mojang.datafixers.util.Pair
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
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

class VatModel: UnbakedModel, BakedModel, FabricBakedModel {

    private val spriteArray = arrayOfNulls<Sprite>(VatBlock.sprites.size)

    private val modelIdentifierCollection = mutableListOf<Identifier>(
        ModelIdentifier(ModIdentifier("vat"), ""),
    )
    private val modelArray = arrayOfNulls<BakedModel>(1)

    //Get baked model dependencies
    override fun getModelDependencies() = modelIdentifierCollection
    override fun getTextureDependencies(unbakedModelGetter: Function<Identifier, UnbakedModel>?, unresolvedTextureReferences: MutableSet<Pair<String, String>>?) = VatBlock.sprites

    //Get actual baked model
    override fun bake(loader: ModelLoader, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings?, modelId: Identifier?): BakedModel {
        modelIdentifierCollection.forEachIndexed { idx, modelIdentifier ->
            modelArray[idx] = loader.getOrLoadModel(modelIdentifier).bake(loader, textureGetter, rotationContainer, modelId)
        }
        VatBlock.sprites.forEachIndexed { idx, spriteIdentifier ->
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
        val block = worldView.getBlockState(pos).block as? VatBlock ?: return
        emitCommonQuads(context, block, randSupplier)
    }

    override fun emitItemQuads(stack: ItemStack, randSupplier: Supplier<Random>, context: RenderContext) {
        val block = (stack.item as? BlockItem)?.block as? VatBlock ?: return
        emitCommonQuads(context, block, randSupplier)
    }

    @Suppress("DEPRECATION")
    private fun emitCommonQuads(context: RenderContext, block: VatBlock, randSupplier: Supplier<Random>) {
        context.pushTransform { quad ->
            quad.nominalFace(GeometryHelper.lightFace(quad))
            quad.spriteColor(0, -1, -1, -1, -1)
            quad.spriteBake(0, spriteArray[block.spriteId], MutableQuadView.BAKE_LOCK_UV)
            true
        }

        val emitter = context.emitter
        modelArray[0]?.getQuads(null, null, randSupplier.get())?.forEach(Consumer { q: BakedQuad ->
            emitter.fromVanilla(q.vertexData, 0, false)
            emitter.emit()
        })

        context.popTransform()
    }


}