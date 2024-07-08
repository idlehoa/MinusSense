/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.patcher.bugfixes;

import net.minecraft.client.multiplayer.ServerAddress;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.IDN;

@Mixin(ServerAddress.class)
public class MixinServerAddress {
    @Shadow @Final private String ipAddress;

    /**
     * @author LlamaLad7
     * @reason Fix crash - MC-89698
     */
    @Overwrite
    public String getIP() {
        try {
            return IDN.toASCII(this.ipAddress);
        } catch (Exception e) {
            return "";
        }
    }
}
