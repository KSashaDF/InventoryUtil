package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.*;

class SingleSlotGroup extends SlotGroup {
	
	private final int slot;
	
	public SingleSlotGroup(int slot) {
		if (slot < 0) throw new IllegalArgumentException("slot must be >= 0. slot is " + slot);
		
		this.slot = slot;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		return slot == this.slot && slot < inv.getSize();
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		if (slot < inv.getSize()) {
			return new int[] {slot};
		} else {
			return new int[0];
		}
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inv) {
		if (slot < inv.getSize()) {
			return new ItemStack[] {inv.getItem(slot)};
		} else {
			return new ItemStack[0];
		}
	}
}
