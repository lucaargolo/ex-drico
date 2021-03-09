package io.github.cafeteriaguild.exdrico.mixin;

import io.github.cafeteriaguild.exdrico.common.meshes.MeshType;
import io.github.cafeteriaguild.exdrico.utils.SievesTableCache;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))

    private void exdrico_onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        MeshType.Companion.syncTypes(player);
        SievesTableCache.INSTANCE.syncCache(player);
    }
}