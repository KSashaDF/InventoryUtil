package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.Inventory;

class SubtractedSlotGroup extends SlotGroup {
	
	private final SlotGroup a;
	private final SlotGroup b;
	
	public SubtractedSlotGroup(SlotGroup a, SlotGroup b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean contains(Inventory inventory, int slot) {
		return a.contains(inventory, slot) && !b.contains(inventory, slot);
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		int[] a = this.a.getSlots(inventory);
		int[] b = this.b.getSlots(inventory);
		boolean[] slotSet = new boolean[inventory.getSize()];
		
		for (int i : a) slotSet[i] = true;
		for (int i : b) slotSet[i] = false;
		
		return MergedSlotGroup.getSlotArrayFromSet(slotSet);
	}
}
