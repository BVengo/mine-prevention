package dev.bvengo.mineprevention.config;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Configs extends ConfigParser implements Serializable {
	// Temporary until further development
	public transient ArrayList<String> allowList = new ArrayList<>();
	public ArrayList<String> denyList = new ArrayList<>();

	private final Comparator<String> itemComparator = (a, b) -> {
		int aIndex = Registries.ITEM.getRawId(Registries.ITEM.get(Identifier.of(a)));
		int bIndex = Registries.ITEM.getRawId(Registries.ITEM.get(Identifier.of(b)));
		return aIndex - bIndex;
	};

	public void allow(String itemId) {
		int index = findInsertIndex(allowList, itemId);
		allowList.add(index, itemId);
		denyList.remove(itemId);
	}

	public void deny(String itemId) {
		int index = findInsertIndex(denyList, itemId);
		denyList.add(index, itemId);
		allowList.remove(itemId);
	}

	public void update() {
		// Load all items in the game
		ArrayList<String> blockItemKeys = Registries.ITEM.stream()
			.filter(item -> item instanceof BlockItem)
			.map(item -> Registries.ITEM.getId(item).toString())
			.collect(Collectors.toCollection(ArrayList::new));

		blockItemKeys.forEach(itemId -> {
			if (!denyList.contains(itemId)) {
				allow(itemId);
			}
		});

		// Remove invalid items from the lists
		denyList.removeIf(id -> !blockItemKeys.contains(id));

		// Final sort to ensure proper ordering
		allowList.sort(itemComparator);
		denyList.sort(itemComparator);

		save();
	}

	public int findInsertIndex(ArrayList<String> list, String item) {
		// Use binary search to find the correct insertion point
		int low = 0;
		int high = list.size() - 1;

		while (low <= high) {
			int mid = low + (high - low) / 2;
			String midItem = list.get(mid);

			// Compare the item with the middle element of the list
			int comparison = itemComparator.compare(midItem, item);
			if (comparison == 0) {
				return mid;  // Item already exists at this position
			} else if (comparison < 0) {
				low = mid + 1;  // Search in the right half
			} else {
				high = mid - 1;  // Search in the left half
			}
		}

		return low;  // Return the insertion point
	}
}
