package dev.bvengo.mineprevention;

import com.mojang.blaze3d.platform.InputConstants;
import dev.bvengo.mineprevention.config.Configs;
import dev.bvengo.mineprevention.ui.ConfigsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;


import static com.mojang.text2speech.Narrator.LOGGER;
import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;

public class MinePreventionClientMod implements ClientModInitializer {
	public final static Configs CONFIGS = Configs.load();

	private static KeyMapping openConfigScreen;
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "category"));

	@Override
	public void onInitializeClient() {
		// Keybinding to open the config screen
		openConfigScreen = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				MOD_ID + ".key.open_settings",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_M,
				CATEGORY
		));

		// Open the config screen when the key is pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openConfigScreen.consumeClick()) {
				if (client.gui.screen() == null) {
					client.gui.setScreen(new ConfigsScreen());
				}
			}
		});

		CONFIGS.update();

		LOGGER.info("Initialised the '{}' mod!", MOD_ID);
	}
}