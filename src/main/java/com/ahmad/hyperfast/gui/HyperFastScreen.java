package com.ahmad.hyperfast.gui;

import com.ahmad.hyperfast.HyperFastConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * Settings screen built only from vanilla Button widgets (click to toggle / cycle),
 * so it avoids the parts of the GUI API that change most between versions.
 */
public class HyperFastScreen extends Screen {
	private final Screen parent;

	public HyperFastScreen(Screen parent) {
		super(Component.literal("HyperFast Settings"));
		this.parent = parent;
	}

	private interface Row {
		String label();
		void click();
	}

	private static Row toggle(String name, BooleanSupplier get, java.util.function.Consumer<Boolean> set) {
		return new Row() {
			public String label() { return name + ": " + (get.getAsBoolean() ? "ON" : "OFF"); }
			public void click() { set.accept(!get.getAsBoolean()); }
		};
	}

	private static Row cycle(String name, String unit, int[] values, IntSupplier get, IntConsumer set) {
		return new Row() {
			public String label() { return name + ": " + get.getAsInt() + unit; }
			public void click() {
				int cur = get.getAsInt();
				int next = values[0];
				for (int i = 0; i < values.length; i++) {
					if (values[i] > cur) { next = values[i]; break; }
				}
				set.accept(next);
			}
		};
	}

	private List<Row> rows() {
		List<Row> r = new ArrayList<>();
		r.add(toggle("Unfocused FPS cap", () -> HyperFastConfig.unfocusedFpsCap, v -> HyperFastConfig.unfocusedFpsCap = v));
		r.add(cycle("Unfocused FPS", "", new int[]{5, 10, 15, 30, 60}, () -> HyperFastConfig.unfocusedFps, v -> HyperFastConfig.unfocusedFps = v));
		r.add(toggle("Paused FPS cap", () -> HyperFastConfig.pausedFpsCap, v -> HyperFastConfig.pausedFpsCap = v));
		r.add(cycle("Paused FPS", "", new int[]{20, 30, 60}, () -> HyperFastConfig.pausedFps, v -> HyperFastConfig.pausedFps = v));

		r.add(toggle("Particle distance cull", () -> HyperFastConfig.particleDistanceCull, v -> HyperFastConfig.particleDistanceCull = v));
		r.add(cycle("Particle distance", "m", new int[]{16, 24, 32, 48, 64, 96}, () -> HyperFastConfig.particleMaxDistance, v -> HyperFastConfig.particleMaxDistance = v));
		r.add(toggle("Particle spawn cap", () -> HyperFastConfig.particleSpawnCap, v -> HyperFastConfig.particleSpawnCap = v));
		r.add(cycle("Spawns per tick", "", new int[]{100, 250, 500, 1000, 2000}, () -> HyperFastConfig.particleSpawnsPerTick, v -> HyperFastConfig.particleSpawnsPerTick = v));
		r.add(toggle("Reduce rain particles", () -> HyperFastConfig.reduceRainParticles, v -> HyperFastConfig.reduceRainParticles = v));

		r.add(toggle("Entity distance cull", () -> HyperFastConfig.entityDistanceCull, v -> HyperFastConfig.entityDistanceCull = v));
		r.add(cycle("Entity distance", "m", new int[]{32, 48, 64, 96, 128, 192, 256}, () -> HyperFastConfig.entityMaxDistance, v -> HyperFastConfig.entityMaxDistance = v));
		r.add(toggle("Far entity tick throttle", () -> HyperFastConfig.entityTickThrottle, v -> HyperFastConfig.entityTickThrottle = v));
		r.add(cycle("Throttle distance", "m", new int[]{32, 48, 64, 96, 128}, () -> HyperFastConfig.entityTickDistance, v -> HyperFastConfig.entityTickDistance = v));
		r.add(cycle("Tick every Nth", "", new int[]{2, 3, 4, 6, 8}, () -> HyperFastConfig.entityTickInterval, v -> HyperFastConfig.entityTickInterval = v));

		r.add(toggle("Disable clouds", () -> HyperFastConfig.disableClouds, v -> HyperFastConfig.disableClouds = v));
		r.add(toggle("Disable weather render", () -> HyperFastConfig.disableWeather, v -> HyperFastConfig.disableWeather = v));
		r.add(toggle("Lightmap throttle", () -> HyperFastConfig.lightmapThrottle, v -> HyperFastConfig.lightmapThrottle = v));
		r.add(cycle("Lightmap every Nth", " frames", new int[]{2, 3, 4, 6, 10}, () -> HyperFastConfig.lightmapInterval, v -> HyperFastConfig.lightmapInterval = v));
		return r;
	}

	@Override
	protected void init() {
		List<Row> rows = rows();
		int colW = 150;
		int gap = 10;
		int left = this.width / 2 - colW - gap / 2;
		int top = 24;
		int rowH = 22;
		int perCol = (rows.size() + 1) / 2;
		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			int col = i / perCol;
			int line = i % perCol;
			int x = left + col * (colW + gap);
			int y = top + line * rowH;
			this.addRenderableWidget(Button.builder(Component.literal(row.label()), b -> {
				row.click();
				b.setMessage(Component.literal(row.label()));
			}).bounds(x, y, colW, 20).build());
		}
		int doneY = Math.min(this.height - 28, top + perCol * rowH + 6);
		this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.onClose())
			.bounds(this.width / 2 - 75, doneY, 150, 20).build());
	}

	@Override
	public void onClose() {
		HyperFastConfig.save();
		this.minecraft.gui.setScreen(parent);
	}
}
