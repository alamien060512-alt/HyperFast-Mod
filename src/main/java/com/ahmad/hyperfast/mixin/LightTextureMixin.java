package com.ahmad.hyperfast.mixin;

import com.ahmad.hyperfast.HyperFastConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LightTexture")
public abstract class LightTextureMixin {
	@Unique
	private int hyperfast$frame;

	/** Recomputes the lightmap only every Nth frame. Brightness/gamma changes show up within a few frames. */
	@Inject(method = "updateLightTexture", at = @At("HEAD"), cancellable = true, require = 0)
	private void hyperfast$throttle(CallbackInfo ci) {
		if (!HyperFastConfig.lightmapThrottle) return;
		if (++hyperfast$frame % HyperFastConfig.lightmapInterval != 0) {
			ci.cancel();
		}
	}
}
