package com.ahmad.hyperfast.mixin;

import com.ahmad.hyperfast.HyperFastConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.CloudRenderer")
public abstract class CloudRendererMixin {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true, require = 0)
	private void hyperfast$noClouds(CallbackInfo ci) {
		if (HyperFastConfig.disableClouds) ci.cancel();
	}
}
