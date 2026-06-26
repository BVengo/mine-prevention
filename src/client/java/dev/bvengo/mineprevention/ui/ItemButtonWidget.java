package dev.bvengo.mineprevention.ui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemButtonWidget extends AbstractButton implements Comparable<ItemButtonWidget> {

	private final Minecraft minecraft;

	private final String itemId;
	private final ItemStack itemStack;
	private boolean allowed;

	public ItemButtonWidget(final Minecraft minecraft, String itemId, boolean allowed) {
		super(0, 0, Constants.ITEM_SIZE, Constants.ITEM_SIZE, Component.empty());
		this.minecraft = minecraft;
		this.itemId = itemId;
		this.itemStack = BuiltInRegistries.ITEM.getValue(Identifier.parse(itemId)).getDefaultInstance();
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
	public void extractContents(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, final float a) {
		if (this.isHovered) {
			graphics.fill(
				this.getX(),
				this.getY(),
				this.getX() + Constants.ITEM_SIZE,
				this.getY() + Constants.ITEM_SIZE,
				-2130706433
			);
		}

		graphics.fakeItem(itemStack, this.getX(), this.getY());

		if (this.isHovered) {
			this.extractTooltip(graphics, mouseX, mouseY);
		}
	}

	protected void extractTooltip(final GuiGraphicsExtractor graphics, final int x, final int y) {
		graphics.setTooltipForNextFrame(this.minecraft.font, this.itemStack, x, y);
	}

	@Override
	protected void updateWidgetNarration(final NarrationElementOutput output) {
		output.add(
			NarratedElementType.TITLE,
			Component.translatable("narration.item", this.itemStack.getHoverName()));
	}

	@Override
	public void onPress(InputWithModifiers input) {
		this.setAllowed(!this.isAllowed());
	}

	@Override
	public int compareTo(@NotNull ItemButtonWidget itemWidget) {
		int aIndex = BuiltInRegistries.ITEM.getId(this.getItemStack().getItem());
		int bIndex = BuiltInRegistries.ITEM.getId(itemWidget.getItemStack().getItem());
		return aIndex - bIndex;
	}
}
