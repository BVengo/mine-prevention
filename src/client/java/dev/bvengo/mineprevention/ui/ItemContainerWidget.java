package dev.bvengo.mineprevention.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;
import java.util.List;

import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;

public class ItemContainerWidget extends ContainerWidget {
	private ArrayList<ItemWidget> items;
	private Text title;

	public ItemContainerWidget(int x, int y, int width, int height, Text title, ArrayList<ItemWidget> items) {
		super(x, y, width, height, title);
		this.title = title;
		this.items = items;
	}

	public ItemContainerWidget(int x, int y, int width, int height, Text title) {
		super(x, y, width, height, title);
	}

	public void setItems(ArrayList<ItemWidget> items) {
		this.items = items;
	}

	public void addItem(ItemWidget item) {
		items.add(item);
	}

	@Override
	public List<ItemWidget> children() {
		return items;
	}

	@Override
	protected int getContentsHeightWithPadding() {
		return 0;
	}

	@Override
	protected double getDeltaYPerScroll() {
		// Scroll 1 item at a time
		return ItemWidget.SIZE;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		// Render the border
		context.drawBorder(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), Colors.RED);

		// Render the items
		for(int i = 0; i < items.size(); i++) {
			ItemWidget item = items.get(i);

			item.setX((i % (width / ItemWidget.SIZE)) * ItemWidget.SIZE);
			item.setY((i / (width / ItemWidget.SIZE)) * ItemWidget.SIZE);

			item.render(context, mouseX, mouseY, delta);
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, this.title);
	}
}
