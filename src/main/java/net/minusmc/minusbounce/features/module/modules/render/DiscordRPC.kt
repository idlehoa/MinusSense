/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minusmc.minusbounce.features.module.Module;
import net.minusmc.minusbounce.features.module.ModuleCategory;
import net.minusmc.minusbounce.features.module.ModuleInfo;
import net.minusmc.minusbounce.value.FloatValue;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

@ModuleInfo(name = "ItemPhysics", spacedName = "Item Physics", description = "newton hits", category = ModuleCategory.RENDER)
public class ItemPhysics extends Module {
    private final FloatValue itemWeight = new FloatValue("Weight", 0.5F, 0F, 1F, "x");

    @Override
    public String getTag() {
        return String.valueOf(itemWeight.get());
    }

    public static void main(String[] args) {
        MinusBounceClient client = new MinusBounceClient();
        client.start();

        // Add your client code here

        Runtime.getRuntime().addShutdownHook(new Thread(client::stop));
    }
}

class MinusBounceClient {
    private static final String DISCORD_TOKEN = "MTIzNzcxNjMzMTUzMzU2NTk5NQ.Gle7qE.bcJkS0RXUbu6dqi1eRIoxg9bq8FEzMoPAe2f2Q";

    private DiscordApi api;

    public void start() {
        String username = Minecraft.getMinecraft().getSession().getUsername();
        api = new DiscordApiBuilder().setToken(DISCORD_TOKEN).login().join();

        api.updateActivity(ActivityType.PLAYING, "MinusBounce Idle");
        api.updateStatus("Version: 1.0 | User: " + username);
    }

    public void stop() {
        if (api != null) {
            api.disconnect();
        }
    }
}

class FloatValue {
    private final String name;
    private final float defaultValue;
    private final float minValue;
    private final float maxValue;
    private final String unit;
    private float value;

    public FloatValue(String name, float defaultValue, float minValue, float maxValue, String unit) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unit = unit;
        this.value = defaultValue;
    }

    public float get() {
        return value;
    }

    public void set(float value) {
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Value out of range");
        }
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public String getUnit() {
        return unit;
    }
}
