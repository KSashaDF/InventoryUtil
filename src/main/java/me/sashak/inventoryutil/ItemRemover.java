package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import me.sashak.inventoryutil.filter.slot.SlotGroups;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class ItemRemover {
	
	public static void clearItems(InventoryFilter filter, InventoryHolder holder) {
		clearItems(filter, holder.getInventory());
	}
	
	public static void clearItems(InventoryFilter filter, Inventory inv) {
		int[] clearableSlots = ItemUtil.getMatchingSlots(filter, inv);
		
		for (int clearableSlot : clearableSlots) {
			inv.setItem(clearableSlot, null);
		}
	}
	
	public static void removeItems(InventoryFilter filter, InventoryHolder holder, ItemStack... clearItems) {
		removeItems(filter, holder.getInventory(), clearItems);
	}
	
	public static void removeItems(InventoryFilter filter, Inventory inv, ItemStack... clearItems) {
		for (ItemStack clearItem : clearItems) {
			removeItem(filter, inv, clearItem, clearItem.getAmount());
		}
	}
	
	public static void removeItem(InventoryFilter filter, Inventory inv, ItemStack clearItem, int clearAmount) {
		int[] clearableSlots = ItemUtil.getMatchingSlots(clearItem, filter, inv);
		int clearedAmount = 0;
		
		for (int clearableSlot : clearableSlots) {
			ItemStack slotItem = inv.getItem(clearableSlot);
			int stackSize = slotItem.getAmount();
			
			// Checks if the full stack may be cleared.
			if (clearedAmount + stackSize <= clearAmount) {
				inv.setItem(clearableSlot, null);
				clearedAmount += stackSize;
				
				// Else attempts to clear part of the stack.
			} else if (clearedAmount < clearAmount) {
				slotItem.setAmount(stackSize - (clearAmount - clearedAmount));
				inv.setItem(clearableSlot, slotItem);
				
				break;
				
				// Otherwise just kill the loop.
			} else {
				break;
			}
		}
	}
	
	/**
	 * Clears the player's inventory, cursor, and crafting window.
	 */
	public static void clearPlayerInv(Player player) {
		clearPlayerInv(player, true);
	}
	
	public static void clearPlayerInv(Player player, boolean clearCursorAndCrafting) {
		removeItems(new InventoryFilter(SlotGroups.PLAYER_ENTIRE_INV), player);
		
		if (clearCursorAndCrafting) {
			player.setItemOnCursor(null);
			
			Inventory topInventory = player.getOpenInventory().getTopInventory();
			if (topInventory instanceof CraftingInventory && topInventory.getHolder() == player) {
				topInventory.clear();
			}
		}
	}
}
