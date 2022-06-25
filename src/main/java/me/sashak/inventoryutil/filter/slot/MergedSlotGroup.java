package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.Inventory;

class MergedSlotGroup extends SlotGroup {
	
	private final SlotGroup a;
	private final SlotGroup b;
	
	public MergedSlotGroup(SlotGroup a, SlotGroup b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean contains(Inventory inventory, int slot) {
		return a.contains(inventory, slot) || b.contains(inventory, slot);
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		int[] a = this.a.getSlots(inventory);
		int[] b = this.b.getSlots(inventory);
		boolean[] slotSet = new boolean[inventory.getSize()];
		
		for (int i : a) slotSet[i] = true;
		for (int i : b) slotSet[i] = true;
		
		return getSlotArrayFromSet(slotSet);
	}
	
	static int[] getSlotArrayFromSet(boolean[] slotSet) {
		int mergedSlotCount = 0;
		
		for (boolean isSlotIncluded : slotSet) {
			if (isSlotIncluded) {
				mergedSlotCount++;
			}
		}
		
		int[] mergedSlots = new int[mergedSlotCount];
		int mergedSlotIndex = 0;
		
		for (int i = 0; i < slotSet.length; i++) {
			if (slotSet[i]) {
				mergedSlots[mergedSlotIndex] = i;
				mergedSlotIndex++;
			}
		}
		
		return mergedSlots;
	}
}
