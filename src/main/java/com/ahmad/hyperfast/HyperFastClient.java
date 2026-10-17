package com.ahmad.hyperfast;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperFastClient implements ClientModInitializer {
	public static final Logger LOG = LoggerFactory.getLogger("HyperFast");

	@Override
	public void onInitializeClient() {
		HyperFastConfig.load();

		FabricLoader loader = FabricLoader.getInstance();
		// Compatibility: log what we're running alongside. Our hooks are all
		// non-destructive @Inject at HEAD with low priority, so nothing here is
		// required to be disabled, but we avoid doing redundant work.
		if (loader.isModLoaded("entityculling") || loader.isModLoaded("moreculling")) {
			LOG.info("Culling mod detected; HyperFast distance culling stays additive only.");
		}
		if (loader.isModLoaded("dynamic_fps")) {
			HyperFastConfig.unfocusedFpsCap = false;
			LOG.info("Dynamic FPS detected; disabling HyperFast unfocused FPS cap.");
		}
		LOG.info("HyperFast loaded (unfocusedCap={}, particleCull={}, entityCull={})",
			HyperFastConfig.unfocusedFpsCap, HyperFastConfig.particleDistanceCull, HyperFastConfig.entityDistanceCull);
	}
}
