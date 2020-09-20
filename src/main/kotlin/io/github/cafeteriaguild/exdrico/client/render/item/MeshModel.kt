package io.github.cafeteriaguild.exdrico.client.render.item

import com.mojang.datafixers.util.Pair
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

class MeshModel: UnbakedModel, BakedModel, FabricBakedModel {

    //Get baked model dependencies
    override fun getModelDependencies() = mutableListOf<Identifier>()
    override fun getTextureDependencies(unbakedModelGetter: Function<Identifier, UnbakedModel>?, unresolvedTextureReferences: MutableSet<Pair<String, String>>?) = mutableListOf<SpriteIdentifier>()

    //Get actual baked model
    override fun bake(loader: ModelLoader?, textureGetter: Function<SpriteIdentifier, Sprite>?, rotationContainer: ModelBakeSettings?, modelId: Identifier?) = this

    //Dummy baked model returns
    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY
    override fun getQuads(state: BlockState?, face: Direction?, random: Random?): MutableList<BakedQuad> = mutableListOf()
    override fun getSprite() = null

    //Dummy baked model configs
    override fun useAmbientOcclusion() = true
    override fun isBuiltin() = false
    override fun isSideLit() = false
    override fun hasDepth() = false

    //Get stone block transformation
    override fun getTransformation() = MinecraftClient.getInstance().bakedModelManager.getModel(ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory")).transformation

    //Set false to use fabric renderer
    override fun isVanillaAdapter() = false

    override fun emitBlockQuads(p0: BlockRenderView?, p1: BlockState?, p2: BlockPos?, p3: Supplier<Random>?, p4: RenderContext?) { }

    override fun emitItemQuads(stack: ItemStack, randSupplier: Supplier<Random>, context: RenderContext) {

        val modelIdentifier = ModelIdentifier(ModIdentifier("sieve_mesh"), "inventory")
        val model = MinecraftClient.getInstance().bakedModelManager.getModel(modelIdentifier)

        val meshType = MeshItem.getMeshType(stack)
        val spriteIdentifier = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, meshType.texture)
        val sprite = spriteIdentifier.sprite

        context.pushTransform { quad ->
            quad.nominalFace(GeometryHelper.lightFace(quad))
            quad.spriteColor(0, -1, -1, -1, -1)
            quad.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV)
            true
        }

        val emitter = context.emitter
        model.getQuads(null, null, randSupplier.get()).forEach(Consumer { q: BakedQuad ->
            emitter.fromVanilla(q.vertexData, 0, false)
            emitter.emit()
        })

        context.popTransform()

    }


}