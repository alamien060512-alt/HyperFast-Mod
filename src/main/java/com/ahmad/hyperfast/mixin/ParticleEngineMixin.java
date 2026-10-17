package com.ahmad.hyperfast.mixin;

import com.ahmad.hyperfast.HyperFastConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
	@Unique
	private int hyperfast$spawnedThisTick;

	@Inject(method = "tick", at = @At("HEAD"), require = 0)
	private void hyperfast$resetBudget(CallbackInfo ci) {
		hyperfast$spawnedThisTick = 0;
	}

	/** Skips creating particles we don't want. Cancels only our own decision; never replaces the method. */
	@Inject(method = "createParticle", at = @At("HEAD"), cancellable = true, require = 0)
	private void hyperfast$filter(ParticleOptions options, double x, double y, double z,
	                              double vx, double vy, double vz,
	                              CallbackInfoReturnable<Particle> cir) {
		if (HyperFastConfig.reduceRainParticles && options.getType() == ParticleTypes.RAIN) {
			cir.setReturnValue(null);
			return;
		}
		if (HyperFastConfig.particleSpawnCap && ++hyperfast$spawnedThisTick > HyperFastConfig.particleSpawnsPerTick) {
			cir.setReturnValue(null);
			return;
		}
		if (HyperFastConfig.particleDistanceCull) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.gameRenderer == null) return;
			Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
			double dx = x - cam.x, dy = y - cam.y, dz = z - cam.z;
			double max = HyperFastConfig.particleMaxDistance;
			if (dx * dx + dy * dy + dz * dz > max * max) {
				cir.setReturnValue(null);
			}
		}
	}
}
