package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.*;

import java.util.function.Function;

public abstract class SlotGroup {
	
	public abstract boolean contains(Inventory inventory, int slot);
	
	public abstract int[] getSlots(Inventory inventory);
	
	public ItemStack[] getSlotItems(Inventory inventory) {
		int[] slots = getSlots(inventory);
		ItemStack[] items = new ItemStack[slots.length];
		
		for (int i = 0; i < slots.length; i++) {
			int slot = slots[i];
			
			items[i] = inventory.getItem(slot);
		}
		
		return items;
	}
	
	
	
	public SlotGroup addGroup(SlotGroup other) {
		return new MergedSlotGroup(this, other);
	}
	
	public SlotGroup subtractGroup(SlotGroup other) {
		return new SubtractedSlotGroup(this, other);
	}
	
	public SlotGroup invertGroup() {
		return new InvertedSlotGroup(this);
	}
	
	
	public static SlotGroup ofSlot(int slot) {
		return new SingleSlotGroup(slot);
	}
	
	public static SlotGroup ofSlot(Function<Inventory, Integer> slotFunction) {
		return new FunctionalSingleSlotGroup(slotFunction);
	}
	
	public static SlotGroup ofRange(int minInclusive, int maxInclusive) {
		return new RangedSlotGroup(minInclusive, maxInclusive);
	}
	
	public static SlotGroup ofSlots(int... slots) {
		if (slots.length == 1) {
			return ofSlot(slots[0]);
		} else {
			return new ArraySlotGroup(slots);
		}
	}
}
