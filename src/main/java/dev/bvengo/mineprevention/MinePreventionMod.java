package dev.bvengo.mineprevention;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinePreventionMod implements ModInitializer {
	public static final String MOD_ID = "mineprevention";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised the '{}' mod!", MOD_ID);
	}
}