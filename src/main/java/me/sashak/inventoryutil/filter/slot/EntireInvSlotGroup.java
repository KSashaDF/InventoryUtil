package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.*;

class EntireInvSlotGroup extends SlotGroup {
	
	@Override
	public boolean contains(Inventory inventory, int slot) {
		return slot >= 0 && slot < inventory.getSize();
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		int[] array = new int[inventory.getSize()];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		
		return array;
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inventory) {
		return inventory.getContents();
	}
}
