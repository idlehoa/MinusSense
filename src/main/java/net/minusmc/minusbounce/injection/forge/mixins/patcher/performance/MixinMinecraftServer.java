/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.patcher.performance;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @ModifyVariable(
        method = "addFaviconToStatusResponse",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ServerStatusResponse;setFavicon(Ljava/lang/String;)V", shift = At.Shift.AFTER),
        ordinal = 1
    )
    private ByteBuf patcher$releaseByteBuf(ByteBuf buf1) {
        buf1.release();
        return buf1;
    }
}
