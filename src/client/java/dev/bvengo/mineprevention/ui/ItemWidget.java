package dev.bvengo.mineprevention.ui;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.gui.screen.Screen.getTooltipFromItem;

public class ItemWidget extends ClickableWidget implements Comparable<ItemWidget> {

	private final String itemId;
	private final ItemStack itemStack;
	private final MinecraftClient client;
	private boolean allowed;

	public ItemWidget(MinecraftClient client, String itemId, boolean allowed) {
		super(0, 0, Constants.ITEM_SIZE, Constants.ITEM_SIZE, null);
		this.client = client;
		this.itemId = itemId;
		this.itemStack = Registries.ITEM.get(Identifier.of(itemId)).getDefaultStack();
		this.allowed = allowed;
	}

	public String getItemId() {
		return itemId;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.hovered) {
			context.fill(this.getX(), this.getY(), this.getX() + Constants.ITEM_SIZE, this.getY() + Constants.ITEM_SIZE, -2130706433);
		}

		context.drawItemWithoutEntity(itemStack, this.getX(), this.getY());

		if (hovered) {
			// In case it ever gets removed, getTooltipFromItem is a wrapper for the below:
			// stack.getTooltip(TooltipContext.create(client.world), client.player, client.options.advancedItemTooltips ? Default.ADVANCED : Default.BASIC);
			context.drawTooltip(client.textRenderer, getTooltipFromItem(client, itemStack), itemStack.getTooltipData(), this.getX(), this.getY(), itemStack.get(DataComponentTypes.TOOLTIP_STYLE));
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, this.itemStack.getName());
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.setAllowed(!this.isAllowed());
	}

	@Override
	public int compareTo(@NotNull ItemWidget itemWidget) {
		int aIndex = Registries.ITEM.getRawId(this.getItemStack().getItem());
		int bIndex = Registries.ITEM.getRawId(itemWidget.getItemStack().getItem());
		return aIndex - bIndex;
	}
}
