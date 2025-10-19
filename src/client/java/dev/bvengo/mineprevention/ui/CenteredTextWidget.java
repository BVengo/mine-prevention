package dev.bvengo.mineprevention.ui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class CenteredTextWidget extends TextWidget {
	public CenteredTextWidget(int x, int y, int width, int height, net.minecraft.text.Text message, net.minecraft.client.font.TextRenderer textRenderer) {
		super(x, y, width, height, message, textRenderer);
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		Text text = this.getMessage();
		TextRenderer textRenderer = this.getTextRenderer();

		int xPos = this.getX() + this.getWidth() / 2;
		int yPos = this.getY() + (this.getHeight() - 9) / 2;

		context.drawCenteredTextWithShadow(textRenderer, text.asOrderedText(), xPos, yPos, this.getTextColor());
	}
}
