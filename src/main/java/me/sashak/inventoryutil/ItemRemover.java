package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ItemRemover {
	
	public static void clearItems(InventoryFilter filter, InventoryHolder inventoryHolder) {
		clearItems(filter, inventoryHolder.getInventory());
	}
	
	public static void clearItems(InventoryFilter filter, Inventory inventory) {
		int[] clearableSlots = ItemManipulator.getMatchingSlots(filter, inventory);
		
		for (int clearableSlot : clearableSlots) {
			inventory.setItem(clearableSlot, null);
		}
	}
	
	public static void removeItems(InventoryFilter filter, InventoryHolder inventoryHolder, ItemStack... clearItems) {
		removeItems(filter, inventoryHolder.getInventory(), clearItems);
	}
	
	public static void removeItems(InventoryFilter filter, Inventory inventory, ItemStack... clearItems) {
		for (ItemStack clearItem : clearItems) {
			removeItem(filter, inventory, clearItem, clearItem.getAmount());
		}
	}
	
	public static void removeItem(InventoryFilter filter, Inventory inventory, ItemStack clearItem, int clearAmount) {
		int[] clearableSlots = ItemManipulator.getMatchingSlots(clearItem, filter, inventory);
		int clearedAmount = 0;
		
		for (int clearableSlot : clearableSlots) {
			ItemStack slotItem = inventory.getItem(clearableSlot);
			int stackSize = slotItem.getAmount();
			
			// Checks if the full stack may be cleared.
			if (clearedAmount + stackSize <= clearAmount) {
				inventory.setItem(clearableSlot, null);
				clearedAmount += stackSize;
				
				// Else attempts to clear part of the stack.
			} else if (clearedAmount < clearAmount) {
				slotItem.setAmount(stackSize - (clearAmount - clearedAmount));
				inventory.setItem(clearableSlot, slotItem);
				
				break;
				
				// Otherwise just kill the loop.
			} else {
				break;
			}
		}
	}
}
