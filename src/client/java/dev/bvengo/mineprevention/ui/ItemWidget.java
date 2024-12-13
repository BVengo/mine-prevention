package dev.bvengo.mineprevention.ui;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;


import static dev.bvengo.mineprevention.MinePreventionMod.MOD_ID;
import static net.minecraft.client.gui.screen.Screen.getTooltipFromItem;

public class ItemWidget extends ClickableWidget {

	public static final int SIZE = 16;
	private final ItemStack itemStack;

	private MinecraftClient client;

	public ItemWidget(MinecraftClient client, ItemStack itemStack) {
		super(0, 0, SIZE, SIZE, null);
		this.client = client;
		this.itemStack = itemStack;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		context.getMatrices().push();
		context.getMatrices().translate(0.0F, 0.0F, 100.0F);

		if (this.hovered) {
			context.fill(this.getX(), this.getY(), this.getX() + SIZE, this.getY() + SIZE, -2130706433);
		}

		context.drawItemWithoutEntity(itemStack, this.getX(), this.getY());
		context.getMatrices().pop();

		if (hovered) {
			// In case it ever gets removed, getTooltipFromItem is a wrapper for the below:
			// stack.getTooltip(TooltipContext.create(client.world), client.player, client.options.advancedItemTooltips ? Default.ADVANCED : Default.BASIC);
			context.drawTooltip(client.textRenderer, getTooltipFromItem(client, itemStack), itemStack.getTooltipData(), this.getX(), this.getY(), itemStack.get(DataComponentTypes.TOOLTIP_STYLE));
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, Text.translatable(MOD_ID + ".narration.item", this.itemStack.getName()));
		builder.put(NarrationPart.USAGE, Text.translatable(MOD_ID + ".narration.item.usage", this.itemStack.getName()));
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		System.out.println("Clicked on item: " + itemStack.getName().getString());
	}
}
