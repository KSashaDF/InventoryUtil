package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ItemReplacer {
	
	public static void replaceItems(InventoryFilter filter, InventoryHolder inventoryHolder, ItemStack replacementItem, int maxOperations) {
		replaceItems(filter, inventoryHolder.getInventory(), replacementItem, maxOperations);
	}
	
	/**
	 * Replaces all items matching the given condition with another item.
	 * This method accounts for stack sizes, if a given stack is large enough
	 * for two replacement operations, this method will correctly calculate this,
	 * along with dealing with any remainders (stack size of 5, replacement
	 * operation size of 2, 2 operations 1 left over).
	 *
	 * @param filter Replaces all items matching this filter
	 * @param inventory The inventory
	 * @param replacementItem The item to replace with
	 * @param maxOperations The maximum amount of replacement operations that can be done
	 */
	public static void replaceItems(InventoryFilter filter, Inventory inventory, ItemStack replacementItem, int maxOperations) {
		int extraItemAmount = 0;
		int totalReplaceOperations = 0;
		
		for (ItemStack filterItem : filter.getFilterItems()) {
			int[] replacementSlots = ItemManipulator.getMatchingSlots(filterItem, filter, inventory);
			
			for (int replacementSlot : replacementSlots) {
				ItemStack item = inventory.getItem(replacementSlot);
				if (item == null) continue;
				
				// Finds the amount of replacement operations that should be done to this item.
				int replaceOperations = item.getAmount() / filterItem.getAmount();
				if (replaceOperations == 0) {
					continue;
				}
				
				// Prevents the total amount of operations from exceeding the max.
				replaceOperations = Math.min(maxOperations, totalReplaceOperations + replaceOperations) - totalReplaceOperations;
				totalReplaceOperations += replaceOperations;
				
				if (replaceOperations == 0) { // If the max has been exceeded.
					break;
				}
				
				int replaceAmount = replaceOperations * replacementItem.getAmount(); // Finds the amount of items that these replacement operations yield.
				int remainingAmount = item.getAmount() - replaceAmount; // Finds the remaining inventory item stack size.
				
				// The amount of replacement items that must go in other slots.
				if (remainingAmount > 0) { // If he full inventory stack has not been replaced, add all the replacement items to the extra items.
					extraItemAmount += replaceAmount;
				} else {
					if (replaceAmount > replacementItem.getMaxStackSize()) { // Else, if the full inventory stack has not been replaced, find the amount exceeding this slot.
						extraItemAmount += replaceAmount - replacementItem.getMaxStackSize();
						replaceAmount = replacementItem.getMaxStackSize();
					}
					
					// Replace this inventory slot with the replaced items, if all items in this slot have been replaced.
					ItemStack newReplacementItem = replacementItem.clone();
					newReplacementItem.setAmount(replaceAmount);
					inventory.setItem(replacementSlot, newReplacementItem);
				}
			}
		}
		
		ItemStack[] extraItems = new ItemStack[Util.ceilingDivide(extraItemAmount, replacementItem.getMaxStackSize())];
		
		for (int i = 0; extraItemAmount > 0; i++) {
			int thisExtraItemAmount = Math.min(extraItemAmount, replacementItem.getMaxStackSize());
			extraItemAmount -= thisExtraItemAmount;
			
			ItemStack extraItem = replacementItem.clone();
			extraItem.setAmount(thisExtraItemAmount);
			extraItems[i] = extraItem;
		}
		
		ItemStack[] filterItems = filter.getFilterItems(); // Avoid cloning the filter.
		filter.setFilterItems();
		ItemGiver.giveItems(filter, inventory, extraItems); // Give the extra items.
		filter.setFilterItems(filterItems);
	}
}
