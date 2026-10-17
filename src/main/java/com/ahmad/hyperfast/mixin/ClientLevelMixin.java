package com.ahmad.hyperfast.mixin;

import com.ahmad.hyperfast.HyperFastConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** String target so a renamed/moved class is skipped instead of failing. */
@Mixin(targets = "net.minecraft.client.multiplayer.ClientLevel")
public abstract class ClientLevelMixin {
	/** Far entities tick on a fraction of ticks. Players, vehicles and passengers' mounts are never throttled. */
	@Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true, require = 0)
	private void hyperfast$throttle(Entity entity, CallbackInfo ci) {
		if (!HyperFastConfig.entityTickThrottle || entity instanceof Player || entity.isVehicle()) return;
		Player me = Minecraft.getInstance().player;
		if (me == null) return;
		double max = HyperFastConfig.entityTickDistance;
		if (entity.distanceToSqr(me) <= max * max) return;
		long t = entity.level().getGameTime();
		if ((t + entity.getId()) % HyperFastConfig.entityTickInterval != 0) {
			ci.cancel();
		}
	}
}
