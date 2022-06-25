package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.*;

class SingleSlotGroup extends SlotGroup {
	
	private final int slot;
	
	public SingleSlotGroup(int slot) {
		if (slot < 0) throw new IllegalArgumentException("slot must be >= 0. slot is " + slot);
		
		this.slot = slot;
	}
	
	@Override
	public boolean contains(Inventory inventory, int slot) {
		return slot == this.slot && slot < inventory.getSize();
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		if (slot < inventory.getSize()) {
			return new int[] {slot};
		} else {
			return new int[0];
		}
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inventory) {
		if (slot < inventory.getSize()) {
			return new ItemStack[] {inventory.getItem(slot)};
		} else {
			return new ItemStack[0];
		}
	}
}
