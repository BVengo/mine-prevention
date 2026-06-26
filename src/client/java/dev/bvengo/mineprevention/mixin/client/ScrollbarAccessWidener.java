package dev.bvengo.mineprevention.mixin.client;

import net.minecraft.client.gui.components.AbstractScrollArea;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractScrollArea.class)
public interface ScrollbarAccessWidener {
	@Accessor("scrolling")
	boolean mineprevention$getScrolling();
}
