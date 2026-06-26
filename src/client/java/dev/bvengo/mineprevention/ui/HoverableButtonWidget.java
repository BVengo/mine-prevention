package dev.bvengo.mineprevention.ui;

import dev.bvengo.mineprevention.MinePreventionMod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;


/**
 * Custom button widget that is used as a trigger rather than a toggle.
 * i.e. it is only active while the button is being pressed.
 */
@Environment(EnvType.CLIENT)
public class HoverableButtonWidget extends Button {

    private final Identifier ON_TEXTURE;
    private final Identifier OFF_TEXTURE;
    private final Identifier ON_HOVER_TEXTURE;
    private final Identifier OFF_HOVER_TEXTURE;

    protected boolean isPressed = false;

    public HoverableButtonWidget(String buttonId, int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);

        this.setTooltip(Tooltip.create(
            // Explicit import thanks to Button subclass name conflict
            net.minecraft.network.chat.Component.translatable(MinePreventionMod.MOD_ID + ".button." + buttonId + ".tooltip")
        ));

        ON_TEXTURE = Identifier.fromNamespaceAndPath(MinePreventionMod.MOD_ID, buttonId + "_button_on");
        OFF_TEXTURE = Identifier.fromNamespaceAndPath(MinePreventionMod.MOD_ID, buttonId + "_button_off");
        ON_HOVER_TEXTURE = Identifier.fromNamespaceAndPath(MinePreventionMod.MOD_ID, buttonId + "_button_on_hovered");
        OFF_HOVER_TEXTURE = Identifier.fromNamespaceAndPath(MinePreventionMod.MOD_ID, buttonId + "_button_off_hovered");
    }

    protected Identifier getTextureIdentifier() {
        return isPressed ? (isHovered ? ON_HOVER_TEXTURE : ON_TEXTURE)
                : (isHovered ? OFF_HOVER_TEXTURE : OFF_TEXTURE);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        Identifier texture = getTextureIdentifier();
        context.blitSprite(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), width, height);
    }
}
