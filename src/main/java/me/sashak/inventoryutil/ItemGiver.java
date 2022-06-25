package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import me.sashak.inventoryutil.filter.slot.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemGiver {
	
	public static void setItems(InventoryHolder holder, SlotGroup group, ItemStack... items) {
		setItems(holder.getInventory(), new InventoryFilter(group), items);
	}
	public static void setItems(Inventory inv, SlotGroup group, ItemStack... items) {
		setItems(inv, new InventoryFilter(group), items);
	}
	
	public static void setItems(InventoryHolder holder, InventoryFilter filter, ItemStack... items) {
		setItems(holder.getInventory(), filter, items);
	}
	
	public static void setItems(Inventory inv, InventoryFilter filter, ItemStack... items) {
		int[] modifiedSlots = ItemUtil.getMatchingSlots(filter, inv);
		int itemIndex = 0;
		
		
		// Loops through all slots that should be modified.
		for (int modifiedSlot : modifiedSlots) {
			ItemStack currentSlotItem = inv.getItem(modifiedSlot);
			ItemStack newSlotItem;
			
			// Gets the given item from the item array. If the given index does not exist within the array use null.
			if (itemIndex < items.length) {
				newSlotItem = items[itemIndex].clone();
			} else {
				newSlotItem = null;
			}
			
			// Checks if the old and new slot items are not equal.
			if (newSlotItem != null && newSlotItem.getType() != Material.AIR && !Objects.equals(currentSlotItem, newSlotItem)) {
				inv.setItem(modifiedSlot, newSlotItem);
			}
			
			itemIndex++;
		}
	}
	
	public static void giveItems(InventoryFilter filter, InventoryHolder holder, ItemStack... items) {
		giveItems(filter, holder.getInventory(), items);
	}
	
	public static void giveItems(InventoryFilter filter, Inventory inv, ItemStack... items) {
		for (ItemStack itemStack : items) {
			givePlayerItem(filter, inv, itemStack);
		}
	}
	
	private static void givePlayerItem(InventoryFilter filter, Inventory inv, ItemStack itemStack) {
		if (ItemUtil.isEmptyItem(itemStack)) {
			return;
		}
		
		int[] partialSlots = ItemUtil.getMatchingSlots(itemStack, filter, inv);
		
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
			int[] emptySlots = ItemUtil.getMatchingSlots(null, filter, inv);
			
			// If any empty slots are present...
			if (emptySlots.length > 0) {
				// Put what is remaining of the give item stack in the empty slot.
				ItemStack putItem = itemStack.clone();
				putItem.setAmount(giveableStackSize - givenStackSize);
				
				inv.setItem(emptySlots[0], putItem);
			}
		}
	}
	
	public static void setMainHandItem(InventoryHolder holder, ItemStack item) {
		setMainHandItem(holder.getInventory(), item);
	}
	
	public static void setMainHandItem(Inventory inv, ItemStack item) {
		setItems(inv, SlotGroups.PLAYER_MAIN_HAND, item);
	}
	
	/**
	 * @see #putItemInMainHand(Player, ItemStack, boolean)
	 */
	@Nullable
	public static ItemStack putItemInMainHand(Player player, ItemStack item) {
		return putItemInMainHand(player, item, false);
	}
	
	/**
	 * Sets a player's main hand item.
	 * <p>
	 * If an item is already in the player's main hand, the item is put into the first empty hotbar
	 * slot, and the player's selected slot is changed to the modified slot. If the hotbar is full,
	 * the player's main hand item is moved elsewhere, and the item is put in the main hand slot.
	 * If the main hand item cannot be moved elsewhere, it is replaced by the item and used as the
	 * return value for this method.
	 *
	 * @param checkForSpace if true, nothing will be done if the player's inventory is full. The
	 *                      inputted item will be returned instead
	 * @return the item that was removed from the player's inventory, null if no item was removed.
	 * If the checkForSpace flag is true, the inputted item will be returned
	 */
	@Nullable
	public static ItemStack putItemInMainHand(Player player, ItemStack item, boolean checkForSpace) {
		PlayerInventory inv = player.getInventory();
		ItemStack mainHandItem = inv.getItemInMainHand();
		
		if (ItemUtil.isEmptyItem(mainHandItem)) {
			setMainHandItem(inv, item);
			return null;
		}
		
		int firstEmptyHotbar = ItemUtil.getFirstEmptySlot(inv, SlotGroups.PLAYER_HOTBAR);
		
		if (firstEmptyHotbar != -1) {
			inv.setItem(firstEmptyHotbar, item);
			inv.setHeldItemSlot(firstEmptyHotbar);
			return null;
		}
		
		InventoryFilter mainInvMinusHand = new InventoryFilter(SlotGroups.PLAYER_MAIN_INV.subtractGroup(SlotGroups.PLAYER_MAIN_HAND));
		
		if (ItemUtil.hasRoomForItems(inv, mainInvMinusHand, mainHandItem)) {
			setMainHandItem(inv, item);
			giveItems(mainInvMinusHand, inv, mainHandItem);
			return null;
		} else {
			if (checkForSpace) {
				return item;
			} else {
				setMainHandItem(inv, item);
				return mainHandItem;
			}
		}
	}
}
