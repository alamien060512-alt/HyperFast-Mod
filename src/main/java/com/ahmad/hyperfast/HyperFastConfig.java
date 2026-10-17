package com.ahmad.hyperfast;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Properties-file config: config/hyperfast.properties. Edited in game via the Mod Menu settings screen. */
public final class HyperFastConfig {
	// Frame rate
	public static boolean unfocusedFpsCap = true;
	public static int unfocusedFps = 10;
	public static boolean pausedFpsCap = true;
	public static int pausedFps = 30;

	// Particles
	public static boolean particleDistanceCull = true;
	public static int particleMaxDistance = 48;
	public static boolean particleSpawnCap = true;
	public static int particleSpawnsPerTick = 500;
	public static boolean reduceRainParticles = false;

	// Entities
	public static boolean entityDistanceCull = true;
	public static int entityMaxDistance = 96;
	public static boolean entityTickThrottle = false;
	public static int entityTickDistance = 64;
	public static int entityTickInterval = 2;

	// Sky / weather / light
	public static boolean disableClouds = false;
	public static boolean disableWeather = false;
	public static boolean lightmapThrottle = false;
	public static int lightmapInterval = 2;

	private HyperFastConfig() {}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("hyperfast.properties");
	}

	public static void load() {
		Properties p = new Properties();
		if (Files.exists(file())) {
			try (Reader r = Files.newBufferedReader(file())) {
				p.load(r);
			} catch (IOException e) {
				HyperFastClient.LOG.warn("Could not read config, using defaults", e);
			}
		}
		unfocusedFpsCap = bool(p, "unfocusedFpsCap", unfocusedFpsCap);
		unfocusedFps = clamp(integer(p, "unfocusedFps", unfocusedFps), 1, 240);
		pausedFpsCap = bool(p, "pausedFpsCap", pausedFpsCap);
		pausedFps = clamp(integer(p, "pausedFps", pausedFps), 5, 240);

		particleDistanceCull = bool(p, "particleDistanceCull", particleDistanceCull);
		particleMaxDistance = clamp(integer(p, "particleMaxDistance", particleMaxDistance), 8, 256);
		particleSpawnCap = bool(p, "particleSpawnCap", particleSpawnCap);
		particleSpawnsPerTick = clamp(integer(p, "particleSpawnsPerTick", particleSpawnsPerTick), 10, 10000);
		reduceRainParticles = bool(p, "reduceRainParticles", reduceRainParticles);

		entityDistanceCull = bool(p, "entityDistanceCull", entityDistanceCull);
		entityMaxDistance = clamp(integer(p, "entityMaxDistance", entityMaxDistance), 16, 512);
		entityTickThrottle = bool(p, "entityTickThrottle", entityTickThrottle);
		entityTickDistance = clamp(integer(p, "entityTickDistance", entityTickDistance), 16, 256);
		entityTickInterval = clamp(integer(p, "entityTickInterval", entityTickInterval), 2, 8);

		disableClouds = bool(p, "disableClouds", disableClouds);
		disableWeather = bool(p, "disableWeather", disableWeather);
		lightmapThrottle = bool(p, "lightmapThrottle", lightmapThrottle);
		lightmapInterval = clamp(integer(p, "lightmapInterval", lightmapInterval), 2, 10);

		save();
	}

	public static void save() {
		Properties o = new Properties();
		o.setProperty("unfocusedFpsCap", String.valueOf(unfocusedFpsCap));
		o.setProperty("unfocusedFps", String.valueOf(unfocusedFps));
		o.setProperty("pausedFpsCap", String.valueOf(pausedFpsCap));
		o.setProperty("pausedFps", String.valueOf(pausedFps));
		o.setProperty("particleDistanceCull", String.valueOf(particleDistanceCull));
		o.setProperty("particleMaxDistance", String.valueOf(particleMaxDistance));
		o.setProperty("particleSpawnCap", String.valueOf(particleSpawnCap));
		o.setProperty("particleSpawnsPerTick", String.valueOf(particleSpawnsPerTick));
		o.setProperty("reduceRainParticles", String.valueOf(reduceRainParticles));
		o.setProperty("entityDistanceCull", String.valueOf(entityDistanceCull));
		o.setProperty("entityMaxDistance", String.valueOf(entityMaxDistance));
		o.setProperty("entityTickThrottle", String.valueOf(entityTickThrottle));
		o.setProperty("entityTickDistance", String.valueOf(entityTickDistance));
		o.setProperty("entityTickInterval", String.valueOf(entityTickInterval));
		o.setProperty("disableClouds", String.valueOf(disableClouds));
		o.setProperty("disableWeather", String.valueOf(disableWeather));
		o.setProperty("lightmapThrottle", String.valueOf(lightmapThrottle));
		o.setProperty("lightmapInterval", String.valueOf(lightmapInterval));
		try (Writer w = Files.newBufferedWriter(file())) {
			o.store(w, "HyperFast config (distances in blocks)");
		} catch (IOException e) {
			HyperFastClient.LOG.warn("Could not write config", e);
		}
	}

	private static boolean bool(Properties p, String k, boolean d) {
		String v = p.getProperty(k);
		return v == null ? d : Boolean.parseBoolean(v.trim());
	}

	private static int integer(Properties p, String k, int d) {
		try {
			String v = p.getProperty(k);
			return v == null ? d : Integer.parseInt(v.trim());
		} catch (NumberFormatException e) {
			return d;
		}
	}

	private static int clamp(int v, int lo, int hi) {
		return Math.max(lo, Math.min(hi, v));
	}
}
