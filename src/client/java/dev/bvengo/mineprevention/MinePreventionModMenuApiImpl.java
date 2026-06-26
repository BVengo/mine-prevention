package dev.bvengo.mineprevention;

import dev.bvengo.mineprevention.ui.ConfigsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.Minecraft;

public class MinePreventionModMenuApiImpl implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			Minecraft client = Minecraft.getInstance();
			if (client.level == null) {
				return parent;
			}
			return new ConfigsScreen();
		};
	}
}
