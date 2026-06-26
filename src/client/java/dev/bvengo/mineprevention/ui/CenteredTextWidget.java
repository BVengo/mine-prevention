package dev.bvengo.mineprevention.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class CenteredTextWidget extends StringWidget {
	public CenteredTextWidget(int x, int y, int width, int height, final Component message, final Font font) {
		super(x, y, width, height, message, font);
	}

	private int getTextColor() {
		return ARGB.white(1.0f);
	}

	@Override
	public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		int xPos = this.getX() + this.getWidth() / 2;
		int yPos = this.getY() + (this.getHeight() - 9) / 2;

		graphics.centeredText(this.getFont(), this.getMessage().getVisualOrderText(), xPos, yPos, this.getTextColor());
	}
}
