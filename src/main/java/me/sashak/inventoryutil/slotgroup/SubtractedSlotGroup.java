package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.Inventory;

class SubtractedSlotGroup extends SlotGroup {
	
	private final SlotGroup a;
	private final SlotGroup b;
	
	public SubtractedSlotGroup(SlotGroup a, SlotGroup b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		return a.contains(inv, slot) && !b.contains(inv, slot);
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		int[] a = this.a.getSlots(inv);
		int[] b = this.b.getSlots(inv);
		boolean[] slotSet = new boolean[inv.getSize()];
		
		for (int i : a) slotSet[i] = true;
		for (int i : b) slotSet[i] = false;
		
		return MergedSlotGroup.getSlotArrayFromSet(slotSet);
	}
}
