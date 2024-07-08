/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.client;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.features.module.modules.movement.NoSlow;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions {
    
    @ModifyConstant(method = "updatePlayerMoveState", constant = @Constant(doubleValue = 0.3D, ordinal = 0))
    public double noSlowSneakStrafe(double constant) {
        return (MinusBounce.moduleManager != null 
                && MinusBounce.moduleManager.getModule(NoSlow.class) != null 
                && MinusBounce.moduleManager.getModule(NoSlow.class).getState()) ? MinusBounce.moduleManager.getModule(NoSlow.class).getSneakStrafeMultiplier().get() : 0.3D;
    }

    @ModifyConstant(method = "updatePlayerMoveState", constant = @Constant(doubleValue = 0.3D, ordinal = 1))
    public double noSlowSneakForward(double constant) {
        return (MinusBounce.moduleManager != null 
                && MinusBounce.moduleManager.getModule(NoSlow.class) != null 
                && MinusBounce.moduleManager.getModule(NoSlow.class).getState()) ? MinusBounce.moduleManager.getModule(NoSlow.class).getSneakForwardMultiplier().get() : 0.3D;
    }

}
