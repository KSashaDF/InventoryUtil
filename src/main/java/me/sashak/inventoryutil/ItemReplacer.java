package me.sashak.inventoryutil;

import me.sashak.inventoryutil.itempredicate.ItemPredicate;
import me.sashak.inventoryutil.slotgroup.SlotGroup;
import org.bukkit.inventory.*;

public class ItemReplacer {
	
	/*public static void replaceItems(InventoryHolder holder, SlotGroup group, ItemPredicate predicate, int maxOperations, ItemStack replacementItem) {
		replaceItems(holder.getInventory(), group, predicate, maxOperations, replacementItem);
	}
	
	*//**
	 * Replaces all items matching the given condition with another item.
	 * This method accounts for stack sizes, if a given stack is large enough
	 * for two replacement operations, this method will correctly calculate this,
	 * along with dealing with any remainders (stack size of 5, replacement
	 * operation size of 2, 2 operations 1 left over).
	 *
	 * @param group Replaces all items matching the predicate in this slot group
	 * @param inv The inventory
	 * @param replacementItem The item to replace with
	 * @param maxOperations The maximum amount of replacement operations that can be done
	 *//*
	public static void replaceItems(Inventory inv, SlotGroup group, ItemPredicate predicate, int maxOperations, ItemStack replacementItem) {
		int extraItemAmount = 0;
		int totalReplaceOperations = 0;
		
		for (ItemStack filterItem : filter.getFilterItems()) {
			int[] replacementSlots = ItemUtil.getMatchingSlots(filterItem, filter, inv);
			
			for (int replacementSlot : replacementSlots) {
				ItemStack item = inv.getItem(replacementSlot);
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
					inv.setItem(replacementSlot, newReplacementItem);
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
		ItemGiver.giveItems(filter, inv, extraItems); // Give the extra items.
		filter.setFilterItems(filterItems);
	}*/
}
