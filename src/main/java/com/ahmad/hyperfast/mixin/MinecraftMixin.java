package com.ahmad.hyperfast.mixin;

import com.ahmad.hyperfast.HyperFastConfig;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	/** Lowers (never raises) the frame cap when unfocused or paused. Runs at RETURN so other mods' values are respected. */
	@Inject(method = "getFramerateLimit", at = @At("RETURN"), cancellable = true, require = 0)
	private void hyperfast$fpsCaps(CallbackInfoReturnable<Integer> cir) {
		Minecraft mc = (Minecraft) (Object) this;
		int limit = cir.getReturnValueI();
		if (HyperFastConfig.unfocusedFpsCap && !mc.isWindowActive()) {
			limit = Math.min(limit, HyperFastConfig.unfocusedFps);
		} else if (HyperFastConfig.pausedFpsCap && mc.isPaused()) {
			limit = Math.min(limit, HyperFastConfig.pausedFps);
		}
		cir.setReturnValue(limit);
	}
}
