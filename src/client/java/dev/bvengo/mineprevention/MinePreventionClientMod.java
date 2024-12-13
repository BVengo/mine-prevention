package dev.bvengo.mineprevention;

import dev.bvengo.mineprevention.config.Configs;
import dev.bvengo.mineprevention.ui.ConfigsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


import static com.mojang.text2speech.Narrator.LOGGER;
import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;

public class MinePreventionClientMod implements ClientModInitializer {
	public final static Configs CONFIGS = Configs.load();
	private static KeyBinding openConfigScreen;

	@Override
	public void onInitializeClient() {
		// Keybinding to open the config screen
		openConfigScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + MOD_ID + ".open_settings",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_M,
				"category." + MOD_ID
		));

		// Open the config screen when the key is pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openConfigScreen.wasPressed()) {
				if (client.currentScreen == null) {
					client.setScreen(new ConfigsScreen());
				}
			}
		});

		CONFIGS.update();

		LOGGER.info("Initialised the '{}' mod!", MOD_ID);
	}
}