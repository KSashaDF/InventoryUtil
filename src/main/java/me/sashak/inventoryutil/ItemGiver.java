package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemGiver {
	
	public static void setItems(InventoryFilter filter, InventoryHolder inventoryHolder, ItemStack... items) {
		Inventory inventory = inventoryHolder.getInventory();
		
		int[] modifiedSlots = ItemManipulator.getMatchingSlots(filter, inventory);
		int itemIndex = 0;
		
		// Loops through all slots that should be modified.
		for (int modifiedSlot : modifiedSlots) {
			ItemStack currentSlotItem = inventory.getItem(modifiedSlot);
			ItemStack newSlotItem;
			
			// Gets the given item from the item array. If the given index does not exist within the array use null.
			if (itemIndex < items.length) {
				newSlotItem = items[itemIndex].clone();
			} else {
				newSlotItem = null;
			}
			
			// Checks if the old and new slot items are not equal.
			if (newSlotItem != null && newSlotItem.getType() != Material.AIR && !Objects.equals(currentSlotItem, newSlotItem)) {
				inventory.setItem(modifiedSlot, newSlotItem);
			}
			
			itemIndex++;
		}
	}
	
	public static void giveItems(InventoryFilter filter, InventoryHolder inventoryHolder, ItemStack... items) {
		giveItems(filter, inventoryHolder.getInventory(), items);
	}
	
	public static void giveItems(InventoryFilter filter, Inventory inventory, ItemStack... items) {
		for (ItemStack itemStack : items) {
			givePlayerItem(filter, inventory, itemStack);
		}
	}
	
	private static void givePlayerItem(InventoryFilter filter, Inventory inventory, ItemStack itemStack) {
		if (ItemManipulator.isEmptyItem(itemStack)) {
			return;
		}
		
		int[] partialSlots = ItemManipulator.getMatchingSlots(itemStack, filter, inventory);
		
		int giveableStackSize = itemStack.getAmount();
		int givenStackSize = 0;
		
		// Loops through all the slots with a partial stack in them.
		for (int checkSlot : partialSlots) {
			// Checks if any more items need to be given.
			if (givenStackSize < giveableStackSize) {
				ItemStack checkSlotStack = inventory.getItem(checkSlot);
				
				int checkSlotSize = checkSlotStack.getAmount();
				int checkSlotMaxSize = checkSlotStack.getMaxStackSize();
				
				// If the checked stack is not a full stack...
				if (checkSlotSize < checkSlotMaxSize) {
					int putAmount = checkSlotMaxSize - checkSlotSize;
					
					if (putAmount > giveableStackSize - givenStackSize) {
						putAmount = giveableStackSize - givenStackSize;
					}
					
					checkSlotStack.setAmount(checkSlotSize + putAmount);
					givenStackSize += putAmount;
					
					// Updates the item in the player's inventory.
					inventory.setItem(checkSlot, checkSlotStack);
				}
			} else {
				return;
			}
		}
		
		// Checks if any more items need to be given.
		if (givenStackSize < giveableStackSize) {
			// If not all items have been given yet, attempt to fill empty slots.
			int[] emptySlots = ItemManipulator.getMatchingSlots(null, filter, inventory);
			
			// If any empty slots are present...
			if (emptySlots.length > 0) {
				// Put what is remaining of the give item stack in the empty slot.
				ItemStack putItem = itemStack.clone();
				putItem.setAmount(giveableStackSize - givenStackSize);
				
				inventory.setItem(emptySlots[0], putItem);
			}
		}
	}
}
