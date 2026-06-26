package dev.bvengo.mineprevention.mixin.client;

import dev.bvengo.mineprevention.MinePreventionClientMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin {
	@Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
	private void mineprevention$blockActionRestricted(Level level, BlockPos pos, GameType gameType, CallbackInfoReturnable<Boolean> cir) {
		Block block = level.getBlockState(pos).getBlock();
		Identifier itemId = BuiltInRegistries.ITEM.getKey(block.asItem());

		// Prevent mining if the block matches
		if (MinePreventionClientMod.CONFIGS.denyList.contains(itemId.toString())) {
			cir.setReturnValue(true);
		}
	}
}
