/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NBTTagCompound.class)
public class MixinNBTTagCompound {

    @Inject(method = "setTag", at = @At("HEAD"))
    private void failFast(String key, NBTBase value, CallbackInfo ci) {
        if (value == null) throw new IllegalArgumentException("Invalid null NBT value with key " + key);
    }
}
