package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.*;

import java.util.function.Function;

class FunctionalSingleSlotGroup extends SlotGroup {
	
	private final Function<Inventory, Integer> function;
	
	public FunctionalSingleSlotGroup(Function<Inventory, Integer> function) {
		this.function = function;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		int groupSlot = function.apply(inv);
		
		return slot == groupSlot && groupSlot >= 0 && groupSlot < inv.getSize();
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		int slot = function.apply(inv);
		
		if (slot >= 0 && slot < inv.getSize()) {
			return new int[] {slot};
		} else {
			return new int[0];
		}
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inv) {
		int slot = function.apply(inv);
		
		if (slot >= 0 && slot < inv.getSize()) {
			return new ItemStack[] {inv.getItem(slot)};
		} else {
			return new ItemStack[0];
		}
	}
}
