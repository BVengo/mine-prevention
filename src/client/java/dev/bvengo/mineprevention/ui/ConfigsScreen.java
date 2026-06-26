package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionClientMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;
import static dev.bvengo.mineprevention.ui.Constants.*;

public class ConfigsScreen extends Screen {

	private static final Component TITLE = Component.translatable(MOD_ID + ".screen.options.title");
	private static final Component SEARCH_HINT;

	ArrayList<ItemButtonWidget> allItems = new ArrayList<>();

	ItemContainerWidget allowedContainer;
	ItemContainerWidget deniedContainer;

	TriggerButtonWidget allowAllButton;
	TriggerButtonWidget denyAllButton;

	StringWidget titleWidget;
	EditBox searchField;

	static {
		SEARCH_HINT = Component.translatable(MOD_ID + ".screen.options.search").withStyle(EditBox.SEARCH_HINT_STYLE);
	}

	public ConfigsScreen() {
		super(TITLE);
	}

	@Override
	protected void init() {
		// Load all items, sorted for correct display order
		allItems = Stream.concat(
				MinePreventionClientMod.CONFIGS.allowList.stream().map(itemId -> new ItemButtonWidget(minecraft, itemId, true)),
				MinePreventionClientMod.CONFIGS.denyList.stream().map(itemId -> new ItemButtonWidget(minecraft, itemId, false))
		).sorted().collect(Collectors.toCollection(ArrayList::new));

		initTitle();
		initSearchField();
		initMoveButtons();
		initItemContainers();

		refreshItemLists(true); // Populate the containers
	}

	private void initTitle() {
		titleWidget = new CenteredTextWidget(0, TEXT_HEIGHT, width, TEXT_HEIGHT, this.getTitle(), font);
		this.addRenderableOnly(titleWidget);
	}

	private void initSearchField() {
		this.searchField = new EditBox(
				this.font,
				HORIZONTAL_PADDING,
				titleWidget.getBottom() + VERTICAL_PADDING,
				width - HORIZONTAL_PADDING * 4 - BUTTON_SIZE * 2,
				SEARCH_HEIGHT,
				Component.empty()
		);
		this.searchField.setHint(SEARCH_HINT);
		this.searchField.setResponder((string) -> {
			refreshItemLists(true);  // Update only visible items
		});
		this.searchField.setFocused(true);
		this.addRenderableWidget(this.searchField);
	}

	private void initMoveButtons() {
		// Buttons to allow/deny all
		this.allowAllButton = new TriggerButtonWidget("left", searchField.getRight() + HORIZONTAL_PADDING, searchField.getY(), BUTTON_SIZE, BUTTON_SIZE, (button) -> {
			allItems.forEach(item -> item.setAllowed(true));
			refreshItemLists(true); // Refresh the visible items only
		});
		this.addRenderableWidget(allowAllButton);

		this.denyAllButton = new TriggerButtonWidget("right", allowAllButton.getRight() + HORIZONTAL_PADDING, searchField.getY(), BUTTON_SIZE, BUTTON_SIZE, (button) -> {
			allItems.forEach(item -> item.setAllowed(false));
			refreshItemLists(true); // Refresh the visible items only
		});
		this.addRenderableWidget(denyAllButton);
	}

	private void initItemContainers() {
		// Allow container
		int containerWidth = width / 2 - HORIZONTAL_PADDING * 2;  // Padding either side
		int containerHeight = height - searchField.getBottom() - VERTICAL_PADDING - VERTICAL_PADDING;

		allowedContainer = new ItemContainerWidget(minecraft, HORIZONTAL_PADDING, searchField.getBottom() + VERTICAL_PADDING, containerWidth, containerHeight, Component.translatable(MOD_ID + ".screen.options.allowed"));
		this.addRenderableWidget(allowedContainer);

		// Deny container
		deniedContainer = new ItemContainerWidget(minecraft, width - containerWidth - HORIZONTAL_PADDING, allowedContainer.getY(), containerWidth, containerHeight, Component.translatable(MOD_ID + ".screen.options.denied"));
		this.addRenderableWidget(deniedContainer);
	}

	/**
	 * Refresh only the visible items in the containers based on the current filters.
	 */
	private void refreshItemLists(boolean shouldResetScroll) {
		String searchQuery = searchField.getValue().toLowerCase();

		// Filter items for allowedContainer
		ArrayList<ItemButtonWidget> allowedItems = allItems.stream()
				.filter(item -> item.isAllowed() && item.getItemStack().getItemName().getString().toLowerCase().contains(searchQuery))
				.collect(Collectors.toCollection(ArrayList::new));
		allowedContainer.setItems(allowedItems, shouldResetScroll);

		// Filter items for deniedContainer
		ArrayList<ItemButtonWidget> deniedItems = allItems.stream()
				.filter(item -> !item.isAllowed() && item.getItemStack().getItemName().getString().toLowerCase().contains(searchQuery))
				.collect(Collectors.toCollection(ArrayList::new));
		deniedContainer.setItems(deniedItems, shouldResetScroll);
	}

	private void save() {
		MinePreventionClientMod.CONFIGS.allowList = allItems.stream().filter(ItemButtonWidget::isAllowed).map(ItemButtonWidget::getItemId).collect(Collectors.toCollection(ArrayList::new));
		MinePreventionClientMod.CONFIGS.denyList = allItems.stream().filter((item) -> !item.isAllowed()).map(ItemButtonWidget::getItemId).collect(Collectors.toCollection(ArrayList::new));
		MinePreventionClientMod.CONFIGS.save();
	}

	@Override
	public void extractRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, final float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);

		graphics.verticalLine(width / 2, searchField.getBottom() + VERTICAL_PADDING, height - VERTICAL_PADDING, CommonColors.LIGHT_GRAY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		boolean clicked = super.mouseClicked(event, doubled);

		GuiEventListener focused  = getFocused();
		if (focused != searchField) {
			searchField.setFocused(false);

			if (focused instanceof ItemContainerWidget) {
				refreshItemLists(false);  // Refresh with moved items
			}
		}

		return clicked;
	}

	@Override
	public void removed() {
		save();
		super.removed();
	}
}
