package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.Inventory;

import java.util.Arrays;

class InvertedSlotGroup extends SlotGroup {
	
	private final SlotGroup slotGroup;
	
	public InvertedSlotGroup(SlotGroup slotGroup) {
		this.slotGroup = slotGroup;
	}
	
	@Override
	public boolean contains(Inventory inventory, int slot) {
		return !slotGroup.contains(inventory, slot);
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		int[] slots = slotGroup.getSlots(inventory);
		boolean[] slotSet = new boolean[inventory.getSize()];
		
		Arrays.fill(slotSet, true);
		for (int i : slots) slotSet[i] = false;
		
		return MergedSlotGroup.getSlotArrayFromSet(slotSet);
	}
}
