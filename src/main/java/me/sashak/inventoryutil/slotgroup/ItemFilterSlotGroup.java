package me.sashak.inventoryutil.slotgroup;

import me.sashak.inventoryutil.itempredicate.ItemPredicate;
import org.bukkit.inventory.*;

import java.util.Arrays;

public class ItemFilterSlotGroup extends SlotGroup {
	
	private final SlotGroup slotGroup;
	private final ItemPredicate predicate;
	
	public ItemFilterSlotGroup(SlotGroup slotGroup, ItemPredicate predicate) {
		this.slotGroup = slotGroup;
		this.predicate = predicate;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		return slot < inv.getSize() && predicate.test(inv.getItem(slot));
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		return filterSlots(inv, slotGroup.getSlots(inv), predicate);
	}
	
	static int[] filterSlots(Inventory inv, int[] unfilteredSlots, ItemPredicate predicate) {
		int excludedSlots = 0;
		
		for (int i = 0; i < unfilteredSlots.length; i++) {
			ItemStack item = inv.getItem(unfilteredSlots[i]);
			
			if (!predicate.test(item)) {
				unfilteredSlots[i] = -1;
				excludedSlots++;
			}
		}
		
		int[] filteredSlots = new int[unfilteredSlots.length - excludedSlots];
		int filteredSlotIndex = 0;
		
		for (int unfilteredSlot : unfilteredSlots) {
			if (unfilteredSlot != -1) {
				filteredSlots[filteredSlotIndex] = unfilteredSlot;
				filteredSlotIndex++;
			}
		}
		
		return filteredSlots;
	}
	
	@Override
	public ItemStack[] getSlotItems(Inventory inv) {
		return filterSlotItems(inv, slotGroup.getSlots(inv), predicate);
	}
	
	static ItemStack[] filterSlotItems(Inventory inv, int[] slots, ItemPredicate predicate) {
		ItemStack[] items = new ItemStack[slots.length];
		int itemIndex = 0;
		
		for (int slot : slots) {
			ItemStack item = inv.getItem(slot);
			
			if (predicate.test(item)) {
				items[itemIndex] = item;
				itemIndex++;
			}
		}
		
		return Arrays.copyOf(items, itemIndex + 1);
	}
}
