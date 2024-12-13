package dev.bvengo.mineprevention.config;

import com.google.gson.*;
import dev.bvengo.mineprevention.MinePreventionMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigParser {
	private static final File file = new File(FabricLoader.getInstance().getConfigDir().toFile(),
			MinePreventionMod.MOD_ID + ".json");
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


	/**
	 * Loads the data into a Config object from a JSON file.
	 */
	public static Configs load() {
		if (!file.exists()) {
			MinePreventionMod.LOGGER.info("Config file not found. Creating a new one.");
			return createNewConfig();
		}

		try (Reader reader = new FileReader(file)) {
			return gson.fromJson(reader, Configs.class);
		} catch (Exception e) {
			MinePreventionMod.LOGGER.error("Error reading config file, creating a new one. Original error: ", e);
			moveOldConfig();
			return createNewConfig();
		}
	}

	/**
	 * Saves the provided Configs to a JSON file.
	 *
	 */
	public void save() {
		try (Writer writer = new FileWriter(file)) {
			gson.toJson(this, writer);
		} catch (IOException e) {
			MinePreventionMod.LOGGER.error("Unable to save sound config to file.", e);
		}
	}

	/**
	 * Moves the old config file to a new file with a prefix of `old.`.
	 */
	private static void moveOldConfig() {
		File oldFile = new File(file.getParentFile(), "old." + file.getName());
		if (file.renameTo(oldFile)) {
			MinePreventionMod.LOGGER.info("Renamed old config file to {}", oldFile.getName());
		} else {
			MinePreventionMod.LOGGER.error("Failed to rename old config file.");
		}
	}

	/**
	 * Creates a new Config object because an old one was missing or broken.
	 *
	 * @return The JsonObject.
	 */
	private static Configs createNewConfig() {
		Configs config = new Configs();
		config.save();
		return config;
	}
}