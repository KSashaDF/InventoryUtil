package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.Inventory;

import java.util.Arrays;

class InvertedSlotGroup extends SlotGroup {
	
	private final SlotGroup slotGroup;
	
	public InvertedSlotGroup(SlotGroup slotGroup) {
		this.slotGroup = slotGroup;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		return !slotGroup.contains(inv, slot);
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		int[] slots = slotGroup.getSlots(inv);
		boolean[] slotSet = new boolean[inv.getSize()];
		
		Arrays.fill(slotSet, true);
		for (int i : slots) slotSet[i] = false;
		
		return MergedSlotGroup.getSlotArrayFromSet(slotSet);
	}
}
