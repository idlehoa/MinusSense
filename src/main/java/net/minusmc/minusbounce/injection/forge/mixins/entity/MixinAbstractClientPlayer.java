/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.entity;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.features.module.modules.misc.NameProtect;
import net.minusmc.minusbounce.features.module.modules.render.NoFOV;
import net.minusmc.minusbounce.features.module.modules.client.Cape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer {

    //private CapeInfo capeInfo;

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void getCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        final Cape capeMod = MinusBounce.moduleManager.getModule(Cape.class);
        if (capeMod.getState() && Objects.equals(getGameProfile().getName(), Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
            callbackInfoReturnable.setReturnValue(capeMod.getCape());
        }
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    private void getFovModifier(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        final NoFOV fovModule = MinusBounce.moduleManager.getModule(NoFOV.class);

        if(fovModule.getState()) {
            float newFOV = fovModule.getFovValue().get();

            if(!this.isUsingItem()) {
                callbackInfoReturnable.setReturnValue(newFOV);
                return;
            }

            if(this.getItemInUse().getItem() != Items.bow) {
                callbackInfoReturnable.setReturnValue(newFOV);
                return;
            }

            int i = this.getItemInUseDuration();
            float f1 = (float) i / 20.0f;
            f1 = f1 > 1.0f ? 1.0f : f1 * f1;
            newFOV *= 1.0f - f1 * 0.15f;
            callbackInfoReturnable.setReturnValue(newFOV);
        }
    }

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void getSkin(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        final NameProtect nameProtect = MinusBounce.moduleManager.getModule(NameProtect.class);

        if(nameProtect.getState() && nameProtect.getSkinProtectValue().get()) {
            if (!nameProtect.getAllPlayersValue().get() && !Objects.equals(getGameProfile().getName(), Minecraft.getMinecraft().thePlayer.getGameProfile().getName()))
                return;

            callbackInfoReturnable.setReturnValue((nameProtect.getCustomSkinValue().get() && nameProtect.skinImage != null) ? nameProtect.skinImage : DefaultPlayerSkin.getDefaultSkin(getUniqueID()));
        }
    }
}
