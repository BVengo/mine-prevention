package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionClientMod;
import dev.bvengo.mineprevention.config.Configs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;

public class ConfigsScreen extends Screen {

	ItemContainerWidget allowedContainer;
	ItemContainerWidget deniedContainer;

	public ConfigsScreen() {
		super(Text.translatable(MOD_ID + ".screen.options.title"));
	}

	@Override
	protected void init() {
		Configs config = MinePreventionClientMod.CONFIGS;

		allowedContainer = new ItemContainerWidget(
				0, 0, width / 2, height,
				Text.translatable(MOD_ID + ".screen.options.allowed"),
				widgetsFromConfigs(config.allowList)
		);
		deniedContainer = new ItemContainerWidget(
				width / 2, 0, width / 2, height,
				Text.translatable(MOD_ID + ".screen.options.denied"),
				widgetsFromConfigs(config.denyList)
		);
	}

	private ArrayList<ItemWidget> widgetsFromConfigs(ArrayList<String> list) {
		ArrayList<ItemWidget> items = new ArrayList<>();
		for (String id : list) {
			items.add(new ItemWidget(client, new ItemStack(Registries.ITEM.get(Identifier.of(id)))));
		}
		return items;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		allowedContainer.render(context, mouseX, mouseY, delta);
		deniedContainer.render(context, mouseX, mouseY, delta);
	}
}
