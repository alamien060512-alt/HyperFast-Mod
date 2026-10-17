package com.ahmad.hyperfast.mixin;

import com.ahmad.hyperfast.HyperFastConfig;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
	/** Hard distance cap on entity rendering. Players are never culled. Only ever turns "true" into "false". */
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
	private void hyperfast$distanceCull(Entity entity, Frustum frustum, double camX, double camY, double camZ,
	                                    CallbackInfoReturnable<Boolean> cir) {
		if (!HyperFastConfig.entityDistanceCull || entity instanceof Player) return;
		double max = HyperFastConfig.entityMaxDistance;
		if (entity.distanceToSqr(camX, camY, camZ) > max * max) {
			cir.setReturnValue(false);
		}
	}
}
