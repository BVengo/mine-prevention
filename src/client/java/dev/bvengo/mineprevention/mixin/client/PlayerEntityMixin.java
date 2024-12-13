package dev.bvengo.mineprevention.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
	private void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		Block block = world.getBlockState(pos).getBlock();

		// Stop if the block is stone
		if (block == Blocks.STONE) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
