package io.github.cafeteriaguild.exdrico.mixin;

import io.github.cafeteriaguild.exdrico.utils.SpriteColorCache;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;

@Mixin(SpriteAtlasTexture.class)
public abstract class SpriteAtlasTextureMixin {

    @Shadow protected abstract Identifier getTexturePath(Identifier identifier);

    @Inject(at = @At("HEAD"), method = "loadSprite")
    public void loadSprite(ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<Sprite> infoReturnable) {
        SpriteColorCache.INSTANCE.setLastIdentifier(this.getTexturePath(info.getId()));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;read(Ljava/io/InputStream;)Lnet/minecraft/client/texture/NativeImage;"), method = "loadSprite")
    public NativeImage readNativeImage(InputStream inputStream) throws IOException {
        NativeImage image = NativeImage.read(inputStream);
        if(image.getFormat() == NativeImage.Format.ABGR) {
            long sumr = 0, sumg = 0, sumb = 0;
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    long color = image.getPixelColor(x, y) & 0x00000000ffffffffL;
                    sumb += (color >> 16 & 255);
                    sumg += (color >> 8 & 255);
                    sumr += (color & 255);
                }
            }
            long pixels = image.getWidth()* image.getHeight();
            long red = sumr/pixels, green = sumg/pixels, blue = sumb/pixels;
            SpriteColorCache.INSTANCE.addColor((int) (256*256*red + 256*green + blue));
        }
        return image;
    }

}
