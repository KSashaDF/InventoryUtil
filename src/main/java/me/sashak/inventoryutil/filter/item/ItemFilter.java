package me.sashak.inventoryutil.filter.item;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Acts as a wrapper class for ItemFilterType's.
 */
public class ItemFilter implements ItemFilterImpl {
	
	private final ItemFilterType[] itemFilterTypes;
	private boolean isInverted = false;
	
	public ItemFilter(ItemFilterType... itemFilterTypes) {
		this.itemFilterTypes = itemFilterTypes;
	}
	
	public ItemFilter setInverted(boolean inverted) {
		isInverted = inverted;
		return this;
	}
	
	@Override
	public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
		boolean canAffectItem = true;
		
		for (ItemFilterType itemFilterType : itemFilterTypes) {
			if (!itemFilterType.itemFilter.canAffectItem(checkItem, filterItems)) {
				canAffectItem = false;
				break;
			}
		}
		
		if (isInverted) {
			canAffectItem = !canAffectItem;
		}
		
		return canAffectItem;
	}
	
	public int[] filterSlots(Inventory inventory, ItemStack[] filterItems, int[] slots) {
		
		// This array here acts as a sort of "HashSet", storing what slots are present in the slots array.
		boolean[] presentSlots = new boolean[inventory.getSize()];
		int validSlots = 0;
		
		// Logs what slots are present in the slots array.
		for (int slot : slots) {
			if (canAffectItem(inventory.getItem(slot), filterItems)) {
				presentSlots[slot] = true;
				validSlots++;
			}
		}
		
		int[] filteredSlots = new int[validSlots];
		
		{
			int filteredSlotIndex = 0;
			
			for (int presentSlotIndex = 0; presentSlotIndex < presentSlots.length; presentSlotIndex++) {
				
				// If the slot is flagged in the presentSlots array...
				if (presentSlots[presentSlotIndex]) {
					// Add it to the filteredSlots array.
					filteredSlots[filteredSlotIndex] = presentSlotIndex;
					filteredSlotIndex++;
				}
			}
		}
		
		return filteredSlots;
	}
}
