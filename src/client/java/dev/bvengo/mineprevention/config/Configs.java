package dev.bvengo.mineprevention.config;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Configs extends ConfigParser implements Serializable {
	// Temporary until further development
	public transient ArrayList<String> allowList = new ArrayList<>();
	public ArrayList<String> denyList = new ArrayList<>();

	public void update() {
		// Load all items in the game
		ArrayList<String> allMineableKeys = Registries.ITEM.stream()
			.filter(item -> item instanceof BlockItem)
			.sorted(Comparator.comparing(Registries.ITEM::getRawId))
			.map(item -> Registries.ITEM.getId(item).toString())
			.collect(Collectors.toCollection(ArrayList::new));

		// Add the missing items to the allow list
		allowList = allMineableKeys.stream()
			.filter(itemId -> !denyList.contains(itemId))
			.collect(Collectors.toCollection(ArrayList::new));

		// Remove invalid items from the lists
		denyList.removeIf(id -> !allMineableKeys.contains(id));

		save();
	}
}
