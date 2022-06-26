package me.sashak.inventoryutil;

import me.sashak.inventoryutil.itempredicate.ItemPredicates;
import me.sashak.inventoryutil.slotgroup.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;

public class ItemGiver {
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(InventoryHolder holder, SlotGroup group, ItemStack... items) {
		setItems(holder, group, true, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(Inventory inv, SlotGroup group, ItemStack... items) {
		setItems(inv, group, true, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(InventoryHolder holder, SlotGroup group, boolean cloneItems, ItemStack... items) {
		setItems(holder.getInventory(), group, cloneItems, items);
	}
	
	/**
	 * Sets the contents of the slots in the slot group. All slots are affected. If the amount of
	 * slots is greater than the length of the item array, null will be used. The slots are set in
	 * the order they appear in in the slot group.
	 */
	public static void setItems(Inventory inv, SlotGroup group, boolean cloneItems, ItemStack... items) {
		int[] slotsToSet = group.getSlots(inv);
		int itemIndex = 0;
		
		for (int slotToSet : slotsToSet) {
			ItemStack currentItem = inv.getItem(slotToSet);
			ItemStack itemToSet;
			
			// Gets the next item from the item array. If there are no more items in the item array, use null instead
			if (itemIndex < items.length) {
				itemToSet = items[itemIndex];
				itemIndex++;
				
				if (cloneItems) {
					itemToSet = itemToSet.clone();
				}
			} else {
				itemToSet = null;
			}
			
			if (!ItemUtil.areItemsEqual(currentItem, itemToSet)) {
				inv.setItem(slotToSet, itemToSet);
			}
		}
	}
	
	public static void giveItems(InventoryHolder holder, SlotGroup group, ItemStack... items) {
		giveItems(holder.getInventory(), group, items);
	}
	
	public static void giveItems(Inventory inv, SlotGroup group, ItemStack... items) {
		for (ItemStack itemStack : items) {
			giveItem(inv, group, itemStack);
		}
	}
	
	private static void giveItem(Inventory inv, SlotGroup group, ItemStack itemStack) {
		if (ItemUtil.isEmptyItem(itemStack)) {
			return;
		}
		
		int[] partialSlots = group.getSlots(inv, ItemPredicates.onlyPartialStacks());
		
		int giveableStackSize = itemStack.getAmount();
		int givenStackSize = 0;
		
		// Loops through all the slots with a partial stack in them.
		for (int checkSlot : partialSlots) {
			// Checks if any more items need to be given.
			if (givenStackSize < giveableStackSize) {
				ItemStack checkSlotStack = inv.getItem(checkSlot);
				
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
					inv.setItem(checkSlot, checkSlotStack);
				}
			} else {
				return;
			}
		}
		
		// Checks if any more items need to be given.
		if (givenStackSize < giveableStackSize) {
			// If not all items have been given yet, attempt to fill empty slots.
			int[] emptySlots = group.getSlots(inv, ItemPredicates.onlyEmptyItems());
			
			// If any empty slots are present...
			if (emptySlots.length > 0) {
				// Put what is remaining of the give item stack in the empty slot.
				ItemStack putItem = itemStack.clone();
				putItem.setAmount(giveableStackSize - givenStackSize);
				
				inv.setItem(emptySlots[0], putItem);
			}
		}
	}
	
	/**
	 * @see #putItemInMainHand(Player, ItemStack, boolean)
	 */
	@Nullable
	public static ItemStack putItemInMainHand(Player player, ItemStack item) {
		return putItemInMainHand(player, item, true);
	}
	
	/**
	 * Sets a player's main hand item.
	 * <p>
	 * If an item is already in the player's main hand, the item is put into the first empty hotbar
	 * slot, and the player's selected slot is changed to the modified slot. If the hotbar is full,
	 * the player's main hand item is moved elsewhere, and the item is put in the main hand slot.
	 * If the main hand item cannot be moved elsewhere, it is replaced by the item and returned by
	 * this method.
	 *
	 * @param allowReplacement if false, nothing will be done if the player's inventory is full. The
	 *                         inputted item will be returned instead
	 * @return null if the item was successfully put into the player's main hand. If an item needed
	 * to be removed from the player's inventory to make space for the new item, the removed item
	 * will be returned. If item removal is forbidden by the allowReplacement flag, the inputted
	 * item will be returned.
	 */
	@Nullable
	public static ItemStack putItemInMainHand(Player player, ItemStack item, boolean allowReplacement) {
		PlayerInventory inv = player.getInventory();
		ItemStack mainHandItem = inv.getItemInMainHand();
		
		// Try putting the item into the current main hand slot
		if (ItemUtil.isEmptyItem(mainHandItem)) {
			inv.setItemInMainHand(item);
			return null;
		}
		
		// Try putting the item into an empty hotbar slot
		int firstEmptyHotbar = ItemUtil.getFirstEmptySlot(inv, SlotGroups.PLAYER_HOTBAR);
		
		if (firstEmptyHotbar != -1) {
			inv.setItem(firstEmptyHotbar, item);
			inv.setHeldItemSlot(firstEmptyHotbar);
			return null;
		}
		
		// Try moving the held item elsewhere and putting the item into the current main hand slot
		SlotGroup mainInvMinusHand = SlotGroups.PLAYER_MAIN_INV.subtractGroup(SlotGroups.PLAYER_MAIN_HAND);
		
		if (ItemUtil.hasRoomForItems(inv, mainInvMinusHand, mainHandItem)) {
			inv.setItemInMainHand(item);
			giveItems(inv, mainInvMinusHand, mainHandItem);
			return null;
		}
		
		// Try replacing the held item with the new item
		if (allowReplacement) {
			inv.setItemInMainHand(item);
			return mainHandItem;
		}
		
		// Unable to put the item in the player's main hand
		return item;
	}
}
