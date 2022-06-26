package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.*;

class EntireInvSlotGroup extends SlotGroup {
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		return slot >= 0 && slot < inv.getSize();
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		int[] array = new int[inv.getSize()];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		
		return array;
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inv) {
		return inv.getContents();
	}
}
