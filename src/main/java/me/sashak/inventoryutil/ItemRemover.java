package me.sashak.inventoryutil;

import me.sashak.inventoryutil.itempredicate.*;
import me.sashak.inventoryutil.slotgroup.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class ItemRemover {
	
	public static void clearItems(InventoryHolder holder, SlotGroup group, ItemStack... items) {
		clearItems(holder.getInventory(), group, items);
	}
	
	public static void clearItems(Inventory inv, SlotGroup group, ItemStack... items) {
		clearSlots(inv, group.filterSlots(ItemPredicates.requireSimilarity(items)));
	}
	
	public static void clearItems(InventoryHolder holder, SlotGroup group, ItemPredicate predicate) {
		clearItems(holder.getInventory(), group, predicate);
	}
	
	public static void clearItems(Inventory inv, SlotGroup group, ItemPredicate predicate) {
		clearSlots(inv, group.filterSlots(predicate));
	}
	
	public static void clearSlots(InventoryHolder holder, SlotGroup group) {
		clearSlots(holder.getInventory(), group);
	}
	
	public static void clearSlots(Inventory inv, SlotGroup group) {
		for (int slotToClear : group.getSlots(inv)) {
			inv.setItem(slotToClear, null);
		}
	}
	
	public static void removeItems(InventoryHolder holder, SlotGroup group, ItemStack... itemsToRemove) {
		removeItems(holder.getInventory(), group, itemsToRemove);
	}
	
	public static void removeItems(Inventory inv, SlotGroup group, ItemStack... itemsToRemove) {
		for (ItemStack itemToRemove : itemsToRemove) {
			removeItem(inv, group, ItemPredicates.requireSimilarity(itemsToRemove), itemToRemove.getAmount());
		}
	}
	
	public static void removeItem(InventoryHolder holder, SlotGroup group, ItemPredicate predicate, int amountToRemove) {
		removeItem(holder.getInventory(), group, predicate, amountToRemove);
	}
	
	public static void removeItem(Inventory inv, SlotGroup group, ItemPredicate predicate, int amountToRemove) {
		int[] affectedSlots = group.getSlots(inv, predicate);
		int amountRemoved = 0;
		
		for (int affectedSlot : affectedSlots) {
			ItemStack slotItem = inv.getItem(affectedSlot);
			if (ItemUtil.isEmptyItem(slotItem)) {
				return;
			}
			int stackSize = slotItem.getAmount();
			
			// Check if the full stack can be cleared
			if (amountRemoved + stackSize <= amountToRemove) {
				inv.setItem(affectedSlot, null);
				amountRemoved += stackSize;
				
				// Otherwise, remove only part of the stack and return
			} else {
				if (amountRemoved == amountToRemove) {
					return;
				}
				
				slotItem.setAmount(stackSize - (amountToRemove - amountRemoved));
				inv.setItem(affectedSlot, slotItem);
				
				return;
			}
		}
	}
	
	/**
	 * Clears the player's inventory, cursor, and crafting window.
	 */
	public static void clearPlayerInv(Player player) {
		clearPlayerInv(player, true);
	}
	
	/**
	 * Clears the player's inventory, as well as the cursor and crafting window if
	 * the clearCursorAndCrafting flag is true.
	 */
	public static void clearPlayerInv(Player player, boolean clearCursorAndCrafting) {
		clearSlots(player, SlotGroups.PLAYER_ENTIRE_INV);
		
		if (clearCursorAndCrafting) {
			player.setItemOnCursor(null);
			
			Inventory topInventory = player.getOpenInventory().getTopInventory();
			if (topInventory instanceof CraftingInventory && topInventory.getHolder() == player) {
				topInventory.clear();
			}
		}
	}
}
