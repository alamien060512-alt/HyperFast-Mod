package com.ahmad.hyperfast;

import com.ahmad.hyperfast.gui.HyperFastScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** Only loaded when Mod Menu is installed; adds the settings button in the mod list. */
public class HyperFastModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return HyperFastScreen::new;
	}
}
