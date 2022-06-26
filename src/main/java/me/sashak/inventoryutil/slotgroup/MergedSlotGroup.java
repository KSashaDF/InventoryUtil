package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.Inventory;

class MergedSlotGroup extends SlotGroup {
	
	private final SlotGroup a;
	private final SlotGroup b;
	
	public MergedSlotGroup(SlotGroup a, SlotGroup b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		return a.contains(inv, slot) || b.contains(inv, slot);
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		int[] a = this.a.getSlots(inv);
		int[] b = this.b.getSlots(inv);
		boolean[] slotSet = new boolean[inv.getSize()];
		
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
