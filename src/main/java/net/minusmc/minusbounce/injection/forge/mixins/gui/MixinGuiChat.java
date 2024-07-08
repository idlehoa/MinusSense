/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.injection.forge.mixins.gui;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.features.module.modules.client.HUD;
import net.minusmc.minusbounce.utils.render.AnimationUtils;
import net.minusmc.minusbounce.utils.render.RenderUtils;
import net.minusmc.minusbounce.utils.render.Stencil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends MixinGuiScreen {
    @Shadow
    protected GuiTextField inputField;

    @Shadow
    private List<String> foundPlayerNames;
    @Shadow
    private boolean waitingOnAutocomplete;
    private float yPosOfInputField;
    private float fade = 0;

    @Shadow
    public abstract void onAutocompleteResponse(String[] p_onAutocompleteResponse_1_);

    @Inject(method = "initGui", at = @At("RETURN"))
    private void init(CallbackInfo callbackInfo) {
        inputField.yPosition = height + 1;
        yPosOfInputField = inputField.yPosition;
    }

    @Inject(method = "keyTyped", at = @At("RETURN"))
    private void updateLength(CallbackInfo callbackInfo) {
        if (!inputField.getText().startsWith(String.valueOf(MinusBounce.commandManager.getPrefix()))) return;
        MinusBounce.commandManager.autoComplete(inputField.getText());

        if (!inputField.getText().startsWith(MinusBounce.commandManager.getPrefix() + "lc"))
            inputField.setMaxStringLength(10000);
        else
            inputField.setMaxStringLength(100);
    }

    @Inject(method = "updateScreen", at = @At("HEAD"))
    private void updateScreen(CallbackInfo callbackInfo) {
        final int delta = RenderUtils.INSTANCE.getDeltaTime();

        if (fade < 14) fade = AnimationUtils.INSTANCE.animate(14F, fade, 0.025F * delta);
        if (fade > 14) fade = 14;

        if (yPosOfInputField > height - 12) yPosOfInputField = AnimationUtils.INSTANCE.animate(height - 12, yPosOfInputField, 0.025F * (12F / 14F) * delta);
        if (yPosOfInputField < height - 12) yPosOfInputField = height - 12;

        inputField.yPosition = (int) yPosOfInputField;
    }

    /**
     * Adds client command auto completion and cancels sending an auto completion request packet
     * to the server if the message contains a client command.
     *
     * @author NurMarvin
     */
    @Inject(method = "sendAutocompleteRequest", at = @At("HEAD"), cancellable = true)
    private void handleClientCommandCompletion(String full, final String ignored, CallbackInfo callbackInfo) {
        if (MinusBounce.commandManager.autoComplete(full)) {
            waitingOnAutocomplete = true;

            String[] latestAutoComplete = MinusBounce.commandManager.getLatestAutoComplete();

            if (full.toLowerCase().endsWith(latestAutoComplete[latestAutoComplete.length - 1].toLowerCase()))
                return;

            this.onAutocompleteResponse(latestAutoComplete);

            callbackInfo.cancel();
        }
    }

    @Inject(method = "autocompletePlayerNames", at = @At("HEAD"))
    private void prioritizeClientFriends(final CallbackInfo callbackInfo) {
        foundPlayerNames.sort(
                Comparator.comparing(s -> !MinusBounce.fileManager.getFriendsConfig().isFriend(s)));
    }

    /**
     * Add this callback, to check if the User complete a Playername or a Liquidbounce command.
     * To fix this bug: https://github.com/CCBlueX/LiquidBounce1.8-Issues/issues/3795
     *
     * @author derech1e
     */
    @Inject(method = "onAutocompleteResponse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;autocompletePlayerNames(F)V", shift = At.Shift.BEFORE), cancellable = true)
    private void onAutocompleteResponse(String[] autoCompleteResponse, CallbackInfo callbackInfo) {
        if (MinusBounce.commandManager.getLatestAutoComplete().length != 0) callbackInfo.cancel();
    }

    /**
     * @author CCBlueX
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final HUD hud = MinusBounce.moduleManager.getModule(HUD.class);
        if (hud.getCmdBorderValue().get() && !inputField.getText().isEmpty() && inputField.getText().startsWith(String.valueOf(MinusBounce.commandManager.getPrefix()))) {
            Stencil.INSTANCE.write(true);
            RenderUtils.INSTANCE.drawRect(2F, this.height - fade, this.width - 2, this.height - fade + 12, Integer.MIN_VALUE);
            Stencil.INSTANCE.erase(false);
            RenderUtils.INSTANCE.drawRect(1F, this.height - fade - 1, this.width - 1, this.height - fade + 13, new Color(20, 110, 255).getRGB());
            Stencil.INSTANCE.dispose();
        } else
            RenderUtils.INSTANCE.drawRect(2F, this.height - fade, this.width - 2, this.height - fade + 12, Integer.MIN_VALUE);
            
        this.inputField.drawTextBox();

        if (MinusBounce.commandManager.getLatestAutoComplete().length > 0 && !inputField.getText().isEmpty() && inputField.getText().startsWith(String.valueOf(MinusBounce.commandManager.getPrefix()))) {
            String[] latestAutoComplete = MinusBounce.commandManager.getLatestAutoComplete();
            String[] textArray = inputField.getText().split(" ");
            String trimmedString = latestAutoComplete[0].replaceFirst("(?i)" + textArray[textArray.length - 1], "");

            mc.fontRendererObj.drawStringWithShadow(trimmedString, inputField.xPosition + mc.fontRendererObj.getStringWidth(inputField.getText()), inputField.yPosition, new Color(165, 165, 165).getRGB());
        }

        IChatComponent ichatcomponent =
                this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (ichatcomponent != null)
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
    }
}
