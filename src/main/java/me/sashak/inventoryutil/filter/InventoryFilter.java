package me.sashak.inventoryutil.filter;

import me.sashak.inventoryutil.filter.item.ItemFilter;
import me.sashak.inventoryutil.filter.item.ItemFilterType;
import me.sashak.inventoryutil.filter.slot.SlotFilter;
import me.sashak.inventoryutil.filter.slot.SlotFilterType;
import org.bukkit.inventory.ItemStack;

public class InventoryFilter {
	
	private ItemFilter itemFilter = new ItemFilter(ItemFilterType.ALLOW_ALL);
	private ItemStack[] filterItems = new ItemStack[0];
	private SlotFilter slotFilter = new SlotFilter(SlotFilterType.MAIN_INVENTORY);
	
	// SETTERS
	
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
	
	public InventoryFilter setSlotGroup(SlotFilter slotFilter) {
		this.slotFilter = slotFilter;
		
		return this;
	}
	
	// GETTERS
	
	public ItemFilter getItemFilter() {
		return itemFilter;
	}
	
	public ItemStack[] getFilterItems() {
		return filterItems;
	}
	
	public SlotFilter getSlotGroup() {
		return slotFilter;
	}
}
