package me.sashak.inventoryutil;

import me.sashak.inventoryutil.itempredicate.ItemPredicates;
import me.sashak.inventoryutil.slotgroup.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemGiver {
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(InventoryHolder holder, SlotGroup slots, List<ItemStack> items) {
		setItems(holder, slots, true, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(Inventory inv, SlotGroup slots, List<ItemStack> items) {
		setItems(inv, slots, true, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(InventoryHolder holder, SlotGroup slots, boolean cloneItems, List<ItemStack> items) {
		setItems(holder.getInventory(), slots, cloneItems, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(Inventory inv, SlotGroup slots, boolean cloneItems, List<ItemStack> items) {
		setItems(inv, slots, cloneItems, items.toArray(new ItemStack[0]));
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(InventoryHolder holder, SlotGroup slots, ItemStack... items) {
		setItems(holder, slots, true, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(Inventory inv, SlotGroup slots, ItemStack... items) {
		setItems(inv, slots, true, items);
	}
	
	/**
	 * @see #setItems(Inventory, SlotGroup, boolean, ItemStack...)
	 */
	public static void setItems(InventoryHolder holder, SlotGroup slots, boolean cloneItems, ItemStack... items) {
		setItems(holder.getInventory(), slots, cloneItems, items);
	}
	
	/**
	 * Sets the contents of the slots in the slot group. All slots are affected. If the amount of
	 * slots is greater than the length of the item array, null will be used instead. The slots
	 * are set in the order they appear in in the slot group.
	 */
	public static void setItems(Inventory inv, SlotGroup slots, boolean cloneItems, ItemStack... items) {
		int[] slotsToSet = slots.getSlots(inv);
		int itemIndex = 0;
		
		for (int slotToSet : slotsToSet) {
			ItemStack currentItem = inv.getItem(slotToSet);
			ItemStack itemToSet;
			
			// Gets the next item from the item array. If there are no more items in the item array, use null instead
			if (itemIndex < items.length) {
				itemToSet = items[itemIndex];
				itemIndex++;
				
				if (cloneItems && !ItemUtil.isEmptyItem(itemToSet)) {
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
	
	/**
	 * @see #giveItems(Inventory, SlotGroup, List)
	 */
	@NotNull
	public static ArrayList<ItemStack> giveItems(InventoryHolder holder, SlotGroup slots, ItemStack... items) {
		return giveItems(holder.getInventory(), slots, items);
	}
	
	/**
	 * @see #giveItems(Inventory, SlotGroup, List)
	 */
	@NotNull
	public static ArrayList<ItemStack> giveItems(Inventory inv, SlotGroup slots, ItemStack... items) {
		ArrayList<ItemStack> leftoverItems = new ArrayList<>(0);
		
		for (ItemStack item : items) {
			ItemStack leftoverItem = giveItem(inv, slots, item);
			
			if (leftoverItem != null) {
				leftoverItems.add(leftoverItem);
			}
		}
		
		return leftoverItems;
	}
	
	/**
	 * @see #giveItems(Inventory, SlotGroup, List)
	 */
	@NotNull
	public static ArrayList<ItemStack> giveItems(InventoryHolder holder, SlotGroup slots, List<ItemStack> items) {
		return giveItems(holder.getInventory(), slots, items);
	}
	
	/**
	 * Gives the items to the inventory. Prioritizes partial stacks in the inventory. Items with
	 * stack sizes above their max stack sizes are acceptable and will be handled appropriately.
	 * Any items that could not be given are returned.
	 *
	 * @return a list of items that could not be given because the inventory is full. If the
	 * inputted item list is compacted with no respect to max stack size, then so will be outputted
	 * list of leftover items. Otherwise, no guarantees are given to compactness
	 */
	@NotNull
	public static ArrayList<ItemStack> giveItems(Inventory inv, SlotGroup slots, List<ItemStack> items) {
		ArrayList<ItemStack> leftoverItems = new ArrayList<>(0);
		
		for (ItemStack item : items) {
			ItemStack leftoverItem = giveItem(inv, slots, item);
			
			if (leftoverItem != null) {
				leftoverItems.add(leftoverItem);
			}
		}
		
		return leftoverItems;
	}
	
	@SuppressWarnings("ConstantConditions")
	private static ItemStack giveItem(Inventory inv, SlotGroup slots, ItemStack item) {
		if (ItemUtil.isEmptyItem(item)) {
			return null;
		}
		
		int maxStackSize = item.getMaxStackSize();
		int amountToGive = item.getAmount();
		
		// Prioritize slots with partial stacks
		int[] partialStackSlots = slots.getSlots(inv, ItemPredicates.onlyPartialStacks());
		
		for (int slot : partialStackSlots) {
			ItemStack slotItem = inv.getItem(slot);
			
			if (ItemUtil.areItemsSimilar(item, slotItem)) {
				int slotAmount = slotItem.getAmount();
				int transferAmount = Math.min(maxStackSize - slotAmount, amountToGive);
				
				slotItem.setAmount(slotAmount + transferAmount);
				amountToGive -= transferAmount;
				inv.setItem(slot, slotItem);
				
				if (amountToGive == 0) {
					return null;
				}
			}
		}
		
		// All partial slots are full, fill empty slots instead
		int[] emptySlots = slots.getSlots(inv, ItemPredicates.onlyEmptyItems());
		
		if (emptySlots.length > 0) {
			if (amountToGive <= maxStackSize) {
				item = item.clone();
				item.setAmount(amountToGive);
				inv.setItem(emptySlots[0], item);
				
				return null;
			} else {
				for (int emptySlot : emptySlots) {
					int amountToSet = Math.min(maxStackSize, amountToGive);
					amountToGive -= amountToSet;
					
					ItemStack slotItem = item.clone();
					slotItem.setAmount(amountToSet);
					inv.setItem(emptySlot, slotItem);
					
					if (amountToGive == 0) {
						return null;
					}
				}
			}
		}
		
		item = item.clone();
		item.setAmount(amountToGive);
		return item;
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
