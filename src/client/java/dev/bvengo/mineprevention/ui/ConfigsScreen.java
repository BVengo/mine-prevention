package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionClientMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;
import static dev.bvengo.mineprevention.ui.Constants.*;

public class ConfigsScreen extends Screen {

	ArrayList<ItemWidget> allItems = new ArrayList<>();

	ItemContainerWidget allowedContainer;
	ItemContainerWidget deniedContainer;

	TriggerButtonWidget allowAllButton;
	TriggerButtonWidget denyAllButton;

	TextFieldWidget searchField;

	public ConfigsScreen() {
		super(Text.translatable(MOD_ID + ".screen.options.title"));
	}

	@Override
	protected void init() {
		// Load all items
		allItems = widgetsFromConfigs(MinePreventionClientMod.CONFIGS.allowList, true);
		allItems.addAll(widgetsFromConfigs(MinePreventionClientMod.CONFIGS.denyList, false));

		// Title
		TextWidget title = new TextWidget(0, TEXT_HEIGHT, width, TEXT_HEIGHT, this.getTitle(), textRenderer).alignCenter();
		this.addDrawable(title);

		// Search field
		this.searchField = new TextFieldWidget(textRenderer, HORIZONTAL_PADDING, title.getBottom() + VERTICAL_PADDING, width - HORIZONTAL_PADDING * 4 - BUTTON_SIZE * 2, SEARCH_HEIGHT, Text.translatable(MOD_ID + ".screen.options.search"));
		this.searchField.setChangedListener((string) -> refreshVisibleItems()); // Update only visible items
		this.searchField.setFocusUnlocked(false);
		this.addSelectableChild(this.searchField);

		// Buttons to allow/deny all
		this.allowAllButton = new TriggerButtonWidget("reset", searchField.getRight() + HORIZONTAL_PADDING, searchField.getY(), BUTTON_SIZE, BUTTON_SIZE, (button) -> {
			allItems.forEach(item -> item.setAllowed(true));
			refreshVisibleItems(); // Refresh the visible items only
		});
		this.addSelectableChild(allowAllButton);

		this.denyAllButton = new TriggerButtonWidget("filter", allowAllButton.getRight() + HORIZONTAL_PADDING, searchField.getY(), BUTTON_SIZE, BUTTON_SIZE, (button) -> {
			allItems.forEach(item -> item.setAllowed(false));
			refreshVisibleItems(); // Refresh the visible items only
		});
		this.addSelectableChild(denyAllButton);

		// Allow container
		int containerWidth = width / 2 - HORIZONTAL_PADDING * 2;  // Padding either side
		int containerHeight = height - searchField.getBottom() - VERTICAL_PADDING - VERTICAL_PADDING;

		allowedContainer = new ItemContainerWidget(textRenderer, HORIZONTAL_PADDING, searchField.getBottom() + VERTICAL_PADDING, containerWidth, containerHeight, Text.translatable(MOD_ID + ".screen.options.allowed"));
		this.addSelectableChild(allowedContainer);

		// Deny container
		deniedContainer = new ItemContainerWidget(textRenderer, width - containerWidth - HORIZONTAL_PADDING, allowedContainer.getY(), containerWidth, containerHeight, Text.translatable(MOD_ID + ".screen.options.denied"));
		this.addSelectableChild(deniedContainer);

		// Initial refresh to populate the containers
		refreshVisibleItems();
	}

	/**
	 * Refresh only the visible items in the containers based on the current filters.
	 */
	private void refreshVisibleItems() {
		String searchQuery = searchField.getText().toLowerCase();

		// Filter items for allowedContainer
		ArrayList<ItemWidget> allowedItems = allItems.stream()
				.filter(item -> item.isAllowed() && item.getItemStack().getName().getString().toLowerCase().contains(searchQuery))
				.collect(Collectors.toCollection(ArrayList::new));
		allowedContainer.setItems(allowedItems);

		// Filter items for deniedContainer
		ArrayList<ItemWidget> deniedItems = allItems.stream()
				.filter(item -> !item.isAllowed() && item.getItemStack().getName().getString().toLowerCase().contains(searchQuery))
				.collect(Collectors.toCollection(ArrayList::new));
		deniedContainer.setItems(deniedItems);
	}

	private void save() {
		MinePreventionClientMod.CONFIGS.allowList = allItems.stream().filter(ItemWidget::isAllowed).map(ItemWidget::getItemId).collect(Collectors.toCollection(ArrayList::new));
		MinePreventionClientMod.CONFIGS.denyList = allItems.stream().filter((item) -> !item.isAllowed()).map(ItemWidget::getItemId).collect(Collectors.toCollection(ArrayList::new));
		MinePreventionClientMod.CONFIGS.save();
	}

	private ArrayList<ItemWidget> widgetsFromConfigs(ArrayList<String> list, boolean allowed) {
		return list.stream().map((id) -> new ItemWidget(client, id, allowed)).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		searchField.render(context, mouseX, mouseY, delta);
		allowAllButton.render(context, mouseX, mouseY, delta);
		denyAllButton.render(context, mouseX, mouseY, delta);
		allowedContainer.render(context, mouseX, mouseY, delta);
		deniedContainer.render(context, mouseX, mouseY, delta);

		context.drawVerticalLine(width / 2, searchField.getBottom() + VERTICAL_PADDING, height - VERTICAL_PADDING, Colors.LIGHT_GRAY);
	}

	@Override
	public void close() {
		save();
		super.close();
	}
}
