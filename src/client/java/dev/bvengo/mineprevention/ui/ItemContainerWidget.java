package dev.bvengo.mineprevention.ui;

import dev.bvengo.modcore.client.ui.ScrollableGridPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class ItemContainerWidget extends ScrollableGridPanel<ItemButtonWidget> {
    public ItemContainerWidget(Minecraft minecraft, int x, int y, int width, int height, Component title) {
        super(minecraft.font, x, y, width, height, Constants.ITEM_SIZE, title);
    }
}
