package me.sashak.inventoryutil.filter;

import me.sashak.inventoryutil.filter.item.*;
import me.sashak.inventoryutil.filter.slot.*;
import org.bukkit.inventory.ItemStack;

public class InventoryFilter {
	
	private ItemFilter itemFilter;
	private ItemStack[] filterItems;
	private SlotGroup slotGroup;
	
	public InventoryFilter() {
		this(SlotGroups.PLAYER_MAIN_INV);
	}
	
	public InventoryFilter(SlotGroup slotGroup) {
		itemFilter = new ItemFilter(ItemFilterType.ALLOW_ALL);
		filterItems = new ItemStack[0];
		this.slotGroup = slotGroup;
	}
	
	public InventoryFilter setItemFilter(ItemFilter itemFilter) {
		this.itemFilter = itemFilter;
		
		return this;
	}
	
	public InventoryFilter setItemFilter(ItemFilter itemFilter, ItemStack... filterItems) {
		this.itemFilter = itemFilter;
		this.filterItems = filterItems;
		
		return this;
	}
	
	public InventoryFilter setFilterItems(ItemStack... filterItems) {
		this.filterItems = filterItems;
		
		return this;
	}
	
	public ItemFilter getItemFilter() {
		return itemFilter;
	}
	
	public ItemStack[] getFilterItems() {
		return filterItems;
	}
	
	public InventoryFilter setSlotGroup(SlotGroup slotGroup) {
		this.slotGroup = slotGroup;
		
		return this;
	}
	
	public SlotGroup getSlotGroup() {
		return slotGroup;
	}
}
