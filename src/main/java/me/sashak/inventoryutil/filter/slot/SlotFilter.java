package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Acts as a wrapper class for SlotGroupType's.
 */
public class SlotFilter implements SlotFilterImpl {
	
	private static final int INVENTORY_SIZE = 41;
	
	private final SlotFilterType[] groupTypes;
	private boolean isInverted = false;
	
	public SlotFilter(SlotFilterType... groupTypes) {
		this.groupTypes = groupTypes;
	}
	
	public SlotFilter setInverted(boolean inverted) {
		isInverted = inverted;
		return this;
	}
	
	@Override
	public boolean isWithinGroup(int slot, Inventory inventory) {
		boolean isWithinGroup = false;
		
		for (SlotFilterType groupType : groupTypes) {
			if (groupType.slotGroup.isWithinGroup(slot, inventory)) {
				isWithinGroup = true;
				break;
			}
		}
		
		if (isInverted) {
			isWithinGroup = !isWithinGroup;
		}
		
		return isWithinGroup;
	}
	
	public ItemStack[] getSlotItems(Inventory inventory) {
		int[] slots = getSlots(inventory);
		ItemStack[] items = new ItemStack[slots.length];
		
		for (int index = 0; index < slots.length; index++) {
			int slot = slots[index];
			
			items[index] = inventory.getItem(slot);
		}
		
		return items;
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		int[][] groupSlots = new int[groupTypes.length][];
		
		for (int groupIndex = 0; groupIndex < groupTypes.length; groupIndex++) {
			groupSlots[groupIndex] = groupTypes[groupIndex].slotGroup.getSlots(inventory);
		}
		
		int[] mergedSlots = mergeInventorySlots(groupSlots);
		
		// Checks if the slot group has been inverted. If so, the group slot array also needs to be inverted.
		if (isInverted) {
			mergedSlots = invertInventorySlots(mergedSlots);
		}
		
		return mergedSlots;
	}
	
	private int[] mergeInventorySlots(int[][] slotGroups) {
		
		// Small little optimization.
		if (slotGroups.length == 1) {
			return slotGroups[0];
		}
		
		// This array here acts as a sort of "HashSet", storing what slots are present in
		// all of the the slotGroup arrays.
		boolean[] presentSlots = new boolean[INVENTORY_SIZE];
		int slotCount = 0;
		
		// Logs what slots are present in the slotGroup arrays.
		for (int[] slotGroup : slotGroups) {
			for (int slot : slotGroup) {
				if (!presentSlots[slot]) {
					presentSlots[slot] = true;
					slotCount++;
				}
			}
		}
		
		int[] mergedSlots = new int[slotCount];
		int mergedSlotIndex = 0;
		
		for (int presentSlotIndex = 0; presentSlotIndex < presentSlots.length; presentSlotIndex++) {
			
			// If the slot is present in the slotGroup arrays...
			if (presentSlots[presentSlotIndex]) {
				// Add it to the mergedSlots array.
				mergedSlots[mergedSlotIndex] = presentSlotIndex;
				mergedSlotIndex++;
			}
		}
		
		return mergedSlots;
	}
	
	private int[] invertInventorySlots(int[] inventorySlots) {
		
		// This array here acts as a sort of "HashSet", storing what slots are present in the inventorySlots array.
		boolean[] presentSlots = new boolean[INVENTORY_SIZE];
		
		// Logs what slots are present in the inventorySlots array.
		for (int groupSlot : inventorySlots) {
			presentSlots[groupSlot] = true;
		}
		
		int[] invertedSlots = new int[INVENTORY_SIZE - inventorySlots.length];
		int invertedSlotIndex = 0;
		
		for (int presentSlotIndex = 0; presentSlotIndex < presentSlots.length; presentSlotIndex++) {
			
			// If the slot is not present in the inventorySlots array...
			if (!presentSlots[presentSlotIndex]) {
				// Add it to the invertedSlots array.
				invertedSlots[invertedSlotIndex] = presentSlotIndex;
				invertedSlotIndex++;
			}
		}
		
		return invertedSlots;
	}
}
