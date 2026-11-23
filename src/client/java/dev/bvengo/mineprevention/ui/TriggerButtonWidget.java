package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionMod;
import dev.bvengo.modcore.client.ui.ButtonSprites;
import dev.bvengo.modcore.client.ui.SpriteTriggerButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class TriggerButtonWidget extends SpriteTriggerButton {
    public TriggerButtonWidget(String buttonId, int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height,
                Component.translatable(MinePreventionMod.MOD_ID + ".button." + buttonId + ".tooltip"),
                ButtonSprites.forButton(Identifier.fromNamespaceAndPath(MinePreventionMod.MOD_ID, buttonId)), onPress);
        setTooltip(Tooltip.create(getMessage()));
    }
}
