package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionClientMod;
import dev.bvengo.mineprevention.MinePreventionMod;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.bvengo.mineprevention.ui.Constants.*;

public class ItemContainerWidget extends ContainerWidget {
	private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("widget/scroller");
	private static final Identifier SCROLLER_BACKGROUND_TEXTURE = Identifier.ofVanilla("widget/scroller_background");

	private List<ItemWidget> items; // All items
	private final TextWidget titleWidget;
	private final int TOP_ROW_Y;  // Screen y position of the top row of items
	private final int DISPLAY_WIDTH;  // Width of the display area
	private final int DISPLAY_HEIGHT;  // Height of the display area
	private final int CENTERING_OFFSET; // Offset to center items, since they typically won't perfectly fit horizontally.

	public ItemContainerWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text title) {
		super(x, y, width, height, title);
		this.titleWidget = new TextWidget(x, y, width, TEXT_HEIGHT, title, textRenderer).alignCenter();
		this.TOP_ROW_Y = titleWidget.getBottom() + VERTICAL_PADDING;
		this.DISPLAY_WIDTH = width - HORIZONTAL_PADDING * 2 - SCROLLBAR_WIDTH;
		this.DISPLAY_HEIGHT = ((height - titleWidget.getHeight() - VERTICAL_PADDING) / ITEM_SIZE) * ITEM_SIZE;
		this.CENTERING_OFFSET = (DISPLAY_WIDTH % ITEM_SIZE) / 2;
	}

	public void setItems(List<ItemWidget> items, boolean refreshScroll) {
		this.items = items;

		if(refreshScroll) {
			this.setScrollY(0);
		}
	}

	@Override
	public List<ItemWidget> children() {
		return items;
	}

	private int getItemsPerRow() {
		return DISPLAY_WIDTH / ITEM_SIZE;
	}

	private int getNumVisibleRows() {
		return (this.getHeight() - titleWidget.getHeight() - VERTICAL_PADDING) / ITEM_SIZE;
	}

	@Override
	protected int getContentsHeightWithPadding() {
		// Calculates the height of the container, including off-screen items
		return (int)Math.ceil((float)items.size() * ITEM_SIZE / getItemsPerRow());
	}

	@Override
	protected double getDeltaYPerScroll() {
		return ITEM_SIZE;
	}

	@Override
	protected int getScrollbarX() {
		// The x position of the scrollbar. Offset from the right side.
		return this.getRight() - SCROLLBAR_WIDTH - HORIZONTAL_PADDING;
	}

	@Override
	public int getMaxScrollY() {
		// The maximum scroll position
		return Math.max(0, this.getContentsHeightWithPadding() - DISPLAY_HEIGHT);
	}

	@Override
	protected int getScrollbarThumbHeight() {
		// The length of the scrollbar thumb
		return MathHelper.clamp((int)((float)(DISPLAY_HEIGHT * DISPLAY_HEIGHT) / (float)this.getContentsHeightWithPadding()), 16, DISPLAY_HEIGHT);
	}

	@Override
	protected int getScrollbarThumbY() {
		// The position of the scrollbar thumb
		return Math.max(TOP_ROW_Y, (int)this.getScrollY() * (DISPLAY_HEIGHT - this.getScrollbarThumbHeight()) / this.getMaxScrollY() + TOP_ROW_Y);
	}

	@Override
	protected void drawScrollbar(DrawContext context) {
		int x = this.getScrollbarX();

		context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_BACKGROUND_TEXTURE, x, TOP_ROW_Y, SCROLLBAR_WIDTH, DISPLAY_HEIGHT);

		if(this.overflows()) {
			context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_TEXTURE, x, getScrollbarThumbY(), SCROLLBAR_WIDTH, getScrollbarThumbHeight());
		} else {
			context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_TEXTURE, x, TOP_ROW_Y, SCROLLBAR_WIDTH, DISPLAY_HEIGHT);
		}
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		titleWidget.render(context, mouseX, mouseY, delta);
		drawScrollbar(context);

		// Render only the visible children
		int row = (int)Math.ceil(this.getScrollY() / ITEM_SIZE);

		int start = row * getItemsPerRow();
		int end = Math.min(items.size(), start + getItemsPerRow() * getNumVisibleRows());

		// TODO: Update this to render from the scroll position
		for(int i = 0; i < end - start; i++) {
			int x = getX() + HORIZONTAL_PADDING + CENTERING_OFFSET + (i % getItemsPerRow()) * ITEM_SIZE;
			int y = TOP_ROW_Y + (i / getItemsPerRow()) * ITEM_SIZE;

			ItemWidget item = items.get(start + i);
			item.setPosition(x, y);
			item.render(context, mouseX, mouseY, delta);
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}
}




