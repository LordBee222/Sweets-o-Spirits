package net.beelabs.sos.common.keybind;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.networking.UnleashedDashPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class UnleashedDashKeybind {
    private static KeyBinding UnleashedDashBind;

    public static void registerBind() {
        UnleashedDashBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sos.unleashed_dash", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding
                GLFW.GLFW_KEY_TAB, // The keycode of the key (G key in this case)
                "key.sos.category" // The translation key of the keybinding's category
        ));


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (UnleashedDashBind.wasPressed()) {
                onPressed();
            }
        });
    }

    private static void onPressed() {
        UnleashedDashPacket.send();

    }
}
