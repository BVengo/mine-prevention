package dev.bvengo.mineprevention.ui;

import dev.bvengo.modcore.client.ui.ItemIconButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ItemButtonWidget extends ItemIconButton implements Comparable<ItemButtonWidget> {
    private final String itemId;
    private boolean allowed;

    public ItemButtonWidget(Minecraft minecraft, String itemId, boolean allowed) {
        super(minecraft, BuiltInRegistries.ITEM.getValue(Identifier.parse(itemId)).getDefaultInstance(), Constants.ITEM_SIZE);
        this.itemId = itemId;
        this.allowed = allowed;
    }

    public String getItemId() {
        return itemId;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        allowed = !allowed;
    }

    @Override
    public int compareTo(ItemButtonWidget other) {
        int thisIndex = BuiltInRegistries.ITEM.getId(getItemStack().getItem());
        int otherIndex = BuiltInRegistries.ITEM.getId(other.getItemStack().getItem());

        return Integer.compare(thisIndex, otherIndex);
    }
}
