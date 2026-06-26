package dev.bvengo.mineprevention.ui;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.bvengo.mineprevention.mixin.client.ScrollbarAccessWidener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static dev.bvengo.mineprevention.ui.Constants.*;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED;

public class ItemContainerWidget extends AbstractContainerWidget {

	private static final Identifier SCROLLER_TEXTURE = Identifier.withDefaultNamespace("widget/scroller");
	private static final Identifier SCROLLER_BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("widget/scroller_background");

	private List<ItemButtonWidget> items; // All items
	private final StringWidget titleWidget;
	private final int TOP_ROW_Y;  // Screen y position of the top row of items
	private final int DISPLAY_WIDTH;  // Width of the display area
	private final int DISPLAY_HEIGHT;  // Height of the display area
	private final int CENTERING_OFFSET; // Offset to center items, since they typically won't perfectly fit horizontally.

	protected final Minecraft minecraft;


	public ItemContainerWidget(final Minecraft minecraft, int x, int y, int width, int height, Component title) {
		super(x, y, width, height, CommonComponents.EMPTY, AbstractScrollArea.defaultSettings(ITEM_SIZE));

		this.minecraft = minecraft;
		this.titleWidget = new CenteredTextWidget(x, y, width, TEXT_HEIGHT, title, minecraft.font);
		this.TOP_ROW_Y = titleWidget.getBottom() + VERTICAL_PADDING;
		this.DISPLAY_WIDTH = width - HORIZONTAL_PADDING * 2 - SCROLLBAR_WIDTH;
		this.DISPLAY_HEIGHT = ((height - titleWidget.getHeight() - VERTICAL_PADDING) / ITEM_SIZE) * ITEM_SIZE;
		this.CENTERING_OFFSET = (DISPLAY_WIDTH % ITEM_SIZE) / 2;
	}

	public void setItems(List<ItemButtonWidget> items, boolean refreshScroll) {
		this.items = items;

		if(refreshScroll) {
			this.setScrollAmount(0);
		}
	}

	@Override
	public @NonNull List<ItemButtonWidget> children() {
		return items;
	}

	private int getItemsPerRow() {
		return DISPLAY_WIDTH / ITEM_SIZE;
	}

	private int getNumVisibleRows() {
		return (this.getHeight() - titleWidget.getHeight() - VERTICAL_PADDING) / ITEM_SIZE;
	}

	@Override
	protected int contentHeight() {
		// Calculates the height of the container, including off-screen items
		return (int)Math.ceil((float)items.size() * ITEM_SIZE / getItemsPerRow());
	}

	@Override
	protected int scrollBarX() {
		// The x position of the scrollbar. Offset from the right side.
		return this.getRight() - SCROLLBAR_WIDTH - HORIZONTAL_PADDING;
	}

	@Override
	public int maxScrollAmount() {
		// The maximum scroll position
		return Math.max(0, this.contentHeight() - DISPLAY_HEIGHT);
	}

	@Override
	protected int scrollerHeight() {
		// The length of the scrollbar thumb
		return Mth.clamp((int)((float)(DISPLAY_HEIGHT * DISPLAY_HEIGHT) / (float)this.contentHeight()), 16, DISPLAY_HEIGHT);
	}

	@Override
	public int scrollBarY() {
		// The position of the scrollbar thumb
		return (
			this.maxScrollAmount() == 0 ?
			TOP_ROW_Y :
			Math.max(TOP_ROW_Y, (int) this.scrollAmount() * (DISPLAY_HEIGHT - this.scrollerHeight()) / this.maxScrollAmount() + TOP_ROW_Y)
		);
	}

	@Override
	protected void extractScrollbar(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY) {
		int scrollerX = this.scrollBarX();
		int scrollerY = this.scrollBarY();
		int scrollbarWidth = this.scrollbarWidth();
		int scrollerHeight = this.scrollerHeight();

		graphics.blitSprite(GUI_TEXTURED, SCROLLER_BACKGROUND_TEXTURE, scrollerX, TOP_ROW_Y, scrollbarWidth, DISPLAY_HEIGHT);

		int thumbY;
		int thumbHeight;
		CursorType cursor;

		if (this.scrollable()) {
			thumbY = scrollerY;
			thumbHeight = scrollerHeight;
			cursor =  ((ScrollbarAccessWidener)(this)).mineprevention$getScrolling() ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND;
		}
		else {
			thumbY = TOP_ROW_Y;
			thumbHeight = DISPLAY_HEIGHT;
			cursor = CursorTypes.NOT_ALLOWED;
		}

		graphics.blitSprite(GUI_TEXTURED, SCROLLER_TEXTURE, scrollerX, thumbY, scrollbarWidth, thumbHeight);

		if (this.isOverScrollbar(mouseX, mouseY)) {
			graphics.requestCursor(cursor);
		}
	}

	@Override
	protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		titleWidget.extractRenderState(graphics, mouseX, mouseY, a);
		extractScrollbar(graphics, mouseX, mouseY);

		// Render only the visible children
		int row = (int)Math.ceil(this.scrollAmount() / ITEM_SIZE);

		int start = row * getItemsPerRow();
		int end = Math.min(items.size(), start + getItemsPerRow() * getNumVisibleRows());

		// TODO: Update this to render from the scroll position
		for(int i = 0; i < end - start; i++) {
			int x = getX() + HORIZONTAL_PADDING + CENTERING_OFFSET + (i % getItemsPerRow()) * ITEM_SIZE;
			int y = TOP_ROW_Y + (i / getItemsPerRow()) * ITEM_SIZE;

			ItemButtonWidget item = items.get(start + i);
			item.setPosition(x, y);
			item.extractRenderState(graphics, mouseX, mouseY, a);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {

	}
}




