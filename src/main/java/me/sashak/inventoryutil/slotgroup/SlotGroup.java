package me.sashak.inventoryutil.slotgroup;

import me.sashak.inventoryutil.itempredicate.ItemPredicate;
import org.bukkit.inventory.*;

import java.util.function.Function;

public abstract class SlotGroup {
	
	public abstract boolean contains(Inventory inv, int slot);
	
	public abstract int[] getSlots(Inventory inv);
	
	public ItemStack[] getSlotItems(Inventory inv) {
		int[] slots = getSlots(inv);
		ItemStack[] items = new ItemStack[slots.length];
		
		for (int i = 0; i < slots.length; i++) {
			int slot = slots[i];
			
			items[i] = inv.getItem(slot);
		}
		
		return items;
	}
	
	public final int[] getSlots(Inventory inv, ItemPredicate itemPredicate) {
		return ItemFilterSlotGroup.filterSlots(inv, getSlots(inv), itemPredicate);
	}
	
	public final ItemStack[] getSlotItems(Inventory inv, ItemPredicate itemPredicate) {
		return ItemFilterSlotGroup.filterSlotItems(inv, getSlots(inv), itemPredicate);
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
	
	public SlotGroup filterSlots(ItemPredicate predicate) {
		return new ItemFilterSlotGroup(this, predicate);
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
