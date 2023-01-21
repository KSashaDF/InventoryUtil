package me.sashak.inventoryutil;

import me.sashak.inventoryutil.itempredicate.*;
import me.sashak.inventoryutil.slotgroup.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class ItemRemover {
	
	/**
	 * @see #clearItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static void clearItems(InventoryHolder holder, SlotGroup slots, ItemStack... items) {
		clearItems(holder.getInventory(), slots, items);
	}
	
	/**
	 * Clears all similar items from the slot group.
	 */
	public static void clearItems(Inventory inv, SlotGroup slots, ItemStack... items) {
		clearSlots(inv, slots.filterSlots(ItemPredicates.requireSimilarity(items)));
	}
	
	/**
	 * @see #clearItems(Inventory, SlotGroup, ItemPredicate)
	 */
	public static void clearItems(InventoryHolder holder, SlotGroup slots, ItemPredicate predicate) {
		clearItems(holder.getInventory(), slots, predicate);
	}
	
	/**
	 * Clears all items matching the predicate from the slot group.
	 */
	public static void clearItems(Inventory inv, SlotGroup slots, ItemPredicate predicate) {
		clearSlots(inv, slots.filterSlots(predicate));
	}
	
	/**
	 * @see #clearSlots(Inventory, SlotGroup)
	 */
	public static void clearSlots(InventoryHolder holder, SlotGroup slots) {
		clearSlots(holder.getInventory(), slots);
	}
	
	/**
	 * Clears all slots in the slot group.
	 */
	public static void clearSlots(Inventory inv, SlotGroup slots) {
		for (int slotToClear : slots.getSlots(inv)) {
			inv.setItem(slotToClear, null);
		}
	}
	
	/**
	 * @see #removeItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static void removeItems(InventoryHolder holder, SlotGroup slots, ItemStack... itemsToRemove) {
		removeItems(holder.getInventory(), slots, itemsToRemove);
	}
	
	/**
	 * Attempts to remove the given items from the slot group.
	 *
	 * Note that this does not mean all similar items will be cleared from the slot group, only
	 * the amount of items given will be removed.
	 */
	public static void removeItems(Inventory inv, SlotGroup slots, ItemStack... itemsToRemove) {
		for (ItemStack itemToRemove : itemsToRemove) {
			removeItem(inv, slots, ItemPredicates.requireSimilarity(itemsToRemove), itemToRemove.getAmount());
		}
	}
	
	/**
	 * @see #removeItem(Inventory, SlotGroup, ItemStack, int)
	 */
	public static int removeItem(InventoryHolder holder, SlotGroup slots, ItemStack item, int amountToRemove) {
		return removeItem(holder.getInventory(), slots, item, amountToRemove);
	}
	
	/**
	 * Removes items that are similar to the given item.
	 *
	 * @see #removeItem(Inventory, SlotGroup, ItemPredicate, int)
	 */
	public static int removeItem(Inventory inv, SlotGroup slots, ItemStack item, int amountToRemove) {
		return removeItem(inv, slots, ItemPredicates.requireSimilarity(item), amountToRemove);
	}
	
	/**
	 * @see #removeItem(Inventory, SlotGroup, ItemPredicate, int)
	 */
	public static int removeItem(InventoryHolder holder, SlotGroup slots, ItemPredicate predicate, int amountToRemove) {
		return removeItem(holder.getInventory(), slots, predicate, amountToRemove);
	}
	
	/**
	 * Removes amountToRemove items that match the item predicate from the slot group. Empty slots
	 * are skipped. Returns the amount of items that were removed.
	 *
	 * @return the amount of items that were removed
	 */
	public static int removeItem(Inventory inv, SlotGroup slots, ItemPredicate predicate, int amountToRemove) {
		int[] affectedSlots = slots.getSlots(inv, predicate);
		int amountRemoved = 0;
		
		for (int affectedSlot : affectedSlots) {
			ItemStack slotItem = inv.getItem(affectedSlot);
			if (ItemUtil.isEmptyItem(slotItem)) {
				continue;
			}
			int stackSize = slotItem.getAmount();
			
			// Check if the full stack can be cleared
			if (amountRemoved + stackSize <= amountToRemove) {
				inv.setItem(affectedSlot, null);
				amountRemoved += stackSize;
				
				// Otherwise, remove only part of the stack and return
			} else {
				if (amountRemoved == amountToRemove) {
					return amountRemoved;
				}
				
				slotItem.setAmount(stackSize - (amountToRemove - amountRemoved));
				amountRemoved -= stackSize;
				inv.setItem(affectedSlot, slotItem);
				
				return amountRemoved;
			}
		}
		
		return amountRemoved;
	}
	
	/**
	 * Clears the player's inventory, cursor, and crafting window.
	 */
	public static void clearPlayerInv(Player player) {
		clearPlayerInv(player, true);
	}
	
	/**
	 * Clears the player's inventory, as well as the cursor and crafting window if the
	 * clearCursorAndCrafting flag is true.
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
