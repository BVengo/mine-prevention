package dev.bvengo.mineprevention.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;

/**
 * Custom button widget that is used as a trigger rather than a toggle.
 * i.e. it is only active while the button is being pressed.
 */
@Environment(EnvType.CLIENT)
public class TriggerButtonWidget extends HoverableButtonWidget {

    public TriggerButtonWidget(String buttonId, int x, int y, int width, int height, OnPress onPress) {
        super(buttonId, x, y, width, height, onPress);
    }

    @Override
    public void onPress(final InputWithModifiers input) {
        isPressed = true;
    }

    @Override
    public void onRelease(final MouseButtonEvent event) {
        // Release toggle texture on release, and perform press action.
        // Only perform press action if button remains hovered.
        if(isHovered) {
            this.onPress.onPress(this);
        }

        isPressed = false;
    }
}
