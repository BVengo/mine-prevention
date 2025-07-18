package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionClientMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;
import static dev.bvengo.mineprevention.ui.Constants.*;

public class ConfigsScreen extends Screen {

	ArrayList<ItemWidget> allItems = new ArrayList<>();

	ItemContainerWidget allowedContainer;
	ItemContainerWidget deniedContainer;

	TriggerButtonWidget allowAllButton;
	TriggerButtonWidget denyAllButton;

	TextWidget titleWidget;
	TextFieldWidget searchField;

	public ConfigsScreen() {
		super(Text.translatable(MOD_ID + ".screen.options.title"));
	}

	@Override
	protected void init() {
		// Load all items, sorted for correct display order
		allItems = Stream.concat(
				MinePreventionClientMod.CONFIGS.allowList.stream().map(itemId -> new ItemWidget(client, itemId, true)),
				MinePreventionClientMod.CONFIGS.denyList.stream().map(itemId -> new ItemWidget(client, itemId, false))
		).sorted().collect(Collectors.toCollection(ArrayList::new));

		initTitle();
		initSearchField();
		initMoveButtons();
		initItemContainers();

		refreshItemLists(true); // Populate the containers
	}

	private void initTitle() {
		titleWidget = new TextWidget(0, TEXT_HEIGHT, width, TEXT_HEIGHT, this.getTitle(), textRenderer).alignCenter();
		this.addDrawable(titleWidget);
	}

	private void initSearchField() {
		this.searchField = new TextFieldWidget(textRenderer, HORIZONTAL_PADDING, titleWidget.getBottom() + VERTICAL_PADDING, width - HORIZONTAL_PADDING * 4 - BUTTON_SIZE * 2, SEARCH_HEIGHT, Text.translatable(MOD_ID + ".screen.options.search"));
		this.searchField.setChangedListener((string) -> refreshItemLists(true)); // Update only visible items
		this.searchField.setFocusUnlocked(false);
		this.addSelectableChild(this.searchField);
	}

	private void initMoveButtons() {
		// Buttons to allow/deny all
		this.allowAllButton = new TriggerButtonWidget("left", searchField.getRight() + HORIZONTAL_PADDING, searchField.getY(), BUTTON_SIZE, BUTTON_SIZE, (button) -> {
			allItems.forEach(item -> item.setAllowed(true));
			refreshItemLists(true); // Refresh the visible items only
		});
		this.addSelectableChild(allowAllButton);

		this.denyAllButton = new TriggerButtonWidget("right", allowAllButton.getRight() + HORIZONTAL_PADDING, searchField.getY(), BUTTON_SIZE, BUTTON_SIZE, (button) -> {
			allItems.forEach(item -> item.setAllowed(false));
			refreshItemLists(true); // Refresh the visible items only
		});
		this.addSelectableChild(denyAllButton);
	}

	private void initItemContainers() {
		// Allow container
		int containerWidth = width / 2 - HORIZONTAL_PADDING * 2;  // Padding either side
		int containerHeight = height - searchField.getBottom() - VERTICAL_PADDING - VERTICAL_PADDING;

		allowedContainer = new ItemContainerWidget(textRenderer, HORIZONTAL_PADDING, searchField.getBottom() + VERTICAL_PADDING, containerWidth, containerHeight, Text.translatable(MOD_ID + ".screen.options.allowed"));
		this.addSelectableChild(allowedContainer);

		// Deny container
		deniedContainer = new ItemContainerWidget(textRenderer, width - containerWidth - HORIZONTAL_PADDING, allowedContainer.getY(), containerWidth, containerHeight, Text.translatable(MOD_ID + ".screen.options.denied"));
		this.addSelectableChild(deniedContainer);
	}

	/**
	 * Refresh only the visible items in the containers based on the current filters.
	 */
	private void refreshItemLists(boolean shouldResetScroll) {
		String searchQuery = searchField.getText().toLowerCase();

		// Filter items for allowedContainer
		ArrayList<ItemWidget> allowedItems = allItems.stream()
				.filter(item -> item.isAllowed() && item.getItemStack().getName().getString().toLowerCase().contains(searchQuery))
				.collect(Collectors.toCollection(ArrayList::new));
		allowedContainer.setItems(allowedItems, shouldResetScroll);

		// Filter items for deniedContainer
		ArrayList<ItemWidget> deniedItems = allItems.stream()
				.filter(item -> !item.isAllowed() && item.getItemStack().getName().getString().toLowerCase().contains(searchQuery))
				.collect(Collectors.toCollection(ArrayList::new));
		deniedContainer.setItems(deniedItems, shouldResetScroll);
	}

	private void save() {
		MinePreventionClientMod.CONFIGS.allowList = allItems.stream().filter(ItemWidget::isAllowed).map(ItemWidget::getItemId).collect(Collectors.toCollection(ArrayList::new));
		MinePreventionClientMod.CONFIGS.denyList = allItems.stream().filter((item) -> !item.isAllowed()).map(ItemWidget::getItemId).collect(Collectors.toCollection(ArrayList::new));
		MinePreventionClientMod.CONFIGS.save();
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
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean clicked = super.mouseClicked(mouseX, mouseY, button);

		Element focused  = getFocused();
		if (focused != searchField) {
			searchField.setFocused(false);

			if (focused instanceof ItemContainerWidget) {
				refreshItemLists(false);  // Refresh with moved items
			}
		}

		return clicked;
	}

	@Override
	public void close() {
		save();
		super.close();
	}
}
