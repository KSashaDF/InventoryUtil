package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.*;

import java.util.function.Function;

class FunctionalSingleSlotGroup extends SlotGroup {
	
	private final Function<Inventory, Integer> function;
	
	public FunctionalSingleSlotGroup(Function<Inventory, Integer> function) {
		this.function = function;
	}
	
	@Override
	public boolean contains(Inventory inventory, int slot) {
		int groupSlot = function.apply(inventory);
		
		return slot == groupSlot && groupSlot >= 0 && groupSlot < inventory.getSize();
	}
	
	@Override
	public int[] getSlots(Inventory inventory) {
		int slot = function.apply(inventory);
		
		if (slot >= 0 && slot < inventory.getSize()) {
			return new int[] {slot};
		} else {
			return new int[0];
		}
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inventory) {
		int slot = function.apply(inventory);
		
		if (slot >= 0 && slot < inventory.getSize()) {
			return new ItemStack[] {inventory.getItem(slot)};
		} else {
			return new ItemStack[0];
		}
	}
}
