package io.github.cafeteriaguild.exdrico.mixin;

import io.github.cafeteriaguild.exdrico.utils.RenderItemUtils;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderContext.class, remap = false)
public class ItemRenderContextMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/api/renderer/v1/model/FabricBakedModel;emitItemQuads(Lnet/minecraft/item/ItemStack;Ljava/util/function/Supplier;Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;)V"), method = "renderModel")
    public void renderItem(ItemStack itemStack, ModelTransformation.Mode transformMode, boolean invert, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int lightmap, int overlay, FabricBakedModel model, ItemRenderContext.VanillaQuadHandler vanillaHandler, CallbackInfo info) {
        RenderItemUtils.INSTANCE.renderFabricBakedItemHooks(itemStack, transformMode, matrixStack, vertexConsumerProvider, lightmap, overlay);
    }

}
