package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class ItemManipulator {
	
	private static final ItemStack AIR_ITEM = new ItemStack(Material.AIR, 0);
	
	/**
	 * @return An air item with a stack size of 0
	 */
	public static @NotNull ItemStack getAirItem() {
		return AIR_ITEM.clone();
	}
	
	/**
	 * Transforms null items into air items. This method does not perform any empty item
	 * fixes or transformations.
	 *
	 * All Spigot item methods should be treated as nullable. Use this method value if null
	 * values are not acceptable, but empty item oddities are not problematic.
	 * See {@link ItemManipulator#makeEmptyItemAir(ItemStack)} for more information.
	 */
	public static @NotNull ItemStack makeNullItemAir(@Nullable ItemStack itemStack) {
		return itemStack == null ? getAirItem() : itemStack;
	}
	
	/**
	 * Transforms air items or items with a stack size of 0 into null items.
	 */
	public static @Nullable ItemStack makeEmptyItemNull(@Nullable ItemStack itemStack) {
		return isEmptyItem(itemStack) ? null : itemStack;
	}
	
	/**
	 * Transforms null items, air items, and items with a stack size of 0 into air items.
	 * This method is useful if non-air items with a stack size of 0, air items with a stack
	 * size greater than 0, or null items are not acceptable. If only null items are problematic,
	 * then consider using {@link ItemManipulator#makeNullItemAir(ItemStack)} instead.
	 *
	 * Spigot item methods can return items that violate any of the above conditions. This
	 * method should be used if any of the above conditions is unacceptable.
	 */
	public static @NotNull ItemStack makeEmptyItemAir(@Nullable ItemStack itemStack) {
		return isEmptyItem(itemStack) ? getAirItem() : itemStack;
	}
	
	public static @NotNull ItemStack cloneAndMakeEmptyItemAir(@Nullable ItemStack itemStack) {
		if (isEmptyItem(itemStack)) {
			return getAirItem();
		} else {
			return itemStack.clone();
		}
	}
	
	public static boolean isEmptyItem(@Nullable ItemStack itemStack) {
		return itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() < 1;
	}
	
	public static boolean areItemsSimilar(@Nullable ItemStack a, @Nullable ItemStack b) {
		if (isEmptyItem(a) && isEmptyItem(b)) {
			return true;
		}
		
		return a != null && a.isSimilar(b);
	}
	
	public static boolean areItemsEqual(@Nullable ItemStack a, @Nullable ItemStack b) {
		if (isEmptyItem(a) && isEmptyItem(b)) {
			return true;
		}
		
		return a != null && a.equals(b);
	}
	
	/**
	 * Javadoc
	 */
	//<editor-fold desc="> getMatchingSlots methods" defaultstate="collapsed">
	/**
	 *
	 * @param filter
	 * @param inventory
	 * @return
	 */
	public static int[] getMatchingSlots(InventoryFilter filter, Inventory inventory) {
		int[] slots = filter.getSlotGroup().getSlots(inventory);
		
		return filter.getItemFilter().filterSlots(inventory, filter.getFilterItems(), slots);
	}
	
	static int[] getMatchingSlots(ItemStack matchingStack, InventoryFilter filter, Inventory inventory) {
		int[] slots = filter.getSlotGroup().getSlots(inventory);
		
		return filter.getItemFilter().filterSlots(inventory, new ItemStack[] {matchingStack}, slots);
	}
	//</editor-fold>
	
	//<editor-fold desc="> combineSimilarStacks methods" defaultstate="collapsed">
	/*public static ItemStack[] combineSimilarStacks(ItemStack[] itemStacks) {
	
	}*/
	
	public static ItemStack[] combineSimilarStacks(ItemStack[] itemStacks) {
		return combineSimilarStacks(itemStacks, false);
	}
	
	public static ArrayList<ItemStack> combineSimilarStacks(ItemStack[] itemStacks, boolean respectMaxStackSize) {
		if (itemStacks.length <= 1) {
			return itemStacks.clone();
		}
		
		ArrayList<ItemStack> combinedStacks = new ArrayList<>(itemStacks.length);
		
		for (ItemStack itemStack : itemStacks) {
			for (int combinedIndex = 0; combinedIndex <= combinedStacks.size(); combinedIndex++) {
				if (combinedIndex == combinedStacks.size()) {
					combinedStacks.add(itemStack);
					break;
					
				} else {
					ItemStack combinedStack = combinedStacks.get(combinedIndex);
					
					if (combinedStack == null) {
						if (itemStack == null) {
							break;
						} else {
							continue;
						}
					}
					
					if (combinedStack.isSimilar(itemStack)) {
						combinedStack.setAmount(combinedStack.getAmount() + itemStack.getAmount());
						break;
						
					}
				}
			}
		}
		
		return combinedStacks;
	}
	
	public static ItemStack[] combineSimilarStacksNoClone(ItemStack[] itemStacks) {
	
	}
	//</editor-fold>
	
	//<editor-fold desc="> getMatchingItemCount methods" defaultstate="collapsed">
	public static int getMatchingItemCount(ItemStack matchingStack, InventoryFilter filter, Player player) {
		return getMatchingItemCount(matchingStack, filter, player.getInventory());
	}
	
	@SuppressWarnings("ConstantConditions")
	public static int getMatchingItemCount(ItemStack matchingStack, InventoryFilter filter, Inventory inventory) {
		int[] matchingStackSlots = getMatchingSlots(matchingStack, filter, inventory);
		int itemCount = 0;
		
		if (matchingStack != null) {
			for (int matchingStackSlot : matchingStackSlots) {
				ItemStack inventoryStack = inventory.getItem(matchingStackSlot);
				
				itemCount += inventoryStack.getAmount();
			}
		}
		
		return itemCount;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getSimilarSlotCount methods" defaultstate="collapsed">
	public static int getSimilarSlotCount(ItemStack similarStack, InventoryFilter filter, Inventory inventory) {
		int similarSlots = 0;
		
		for (ItemStack slotItem : filter.getSlotGroup().getSlotItems(inventory)) {
			if (slotItem == similarStack) {
				similarSlots++;
			} else if (slotItem != null && slotItem.isSimilar(similarStack)) {
				similarSlots++;
			}
		}
		
		return similarSlots;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getSimilarSlotCount methods" defaultstate="collapsed">
	public static int getItemCount(ItemStack[] itemStacks) {
		int itemCount = 0;
		
		for (ItemStack itemStack : itemStacks) {
			if (itemStack != null) {
				itemCount += itemStack.getAmount();
			}
		}
		
		return itemCount;
	}
	//</editor-fold>
	
	//<editor-fold desc="> hasRoomForItem methods" defaultstate="collapsed">
	public static boolean hasRoomForItem(ItemStack itemStack, InventoryFilter filter, InventoryHolder inventoryHolder) {
		return hasRoomForItems(new ItemStack[] {itemStack}, filter, inventoryHolder.getInventory());
	}
	
	public static boolean hasRoomForItems(ItemStack[] itemStacks, InventoryFilter filter, InventoryHolder inventoryHolder) {
		return hasRoomForItems(itemStacks, filter, inventoryHolder.getInventory());
	}
	
	@SuppressWarnings("ConstantConditions")
	public static boolean hasRoomForItems(ItemStack[] itemStacks, InventoryFilter filter, Inventory inventory) {
		itemStacks = combineSimilarStacks(itemStacks);
		int emptySlotCount = getSimilarSlotCount(null, filter, inventory);
		
		for (ItemStack itemStack : itemStacks) {
			if (itemStack == null) {
				if (emptySlotCount < 1) {
					return false;
				}
			} else if (itemStack.getType() == Material.AIR) {
				return emptySlotCount > 0;
				
			} else {
				int maxSize = inventory.getMaxStackSize();
				int requiredAmount = itemStack.getAmount();
				int[] matchingSlots = getMatchingSlots(itemStack, filter, inventory);
				
				for (int matchingSlot : matchingSlots) {
					ItemStack slotItem = inventory.getItem(matchingSlot);
					int availableSpace = maxSize - slotItem.getAmount();
					
					requiredAmount -= availableSpace;
					
					if (requiredAmount <= 0) {
						break;
					}
				}
				
				if (requiredAmount > 0) {
					int requiredStacks = Util.ceilingDivide(requiredAmount, maxSize);
					emptySlotCount -= requiredStacks;
					
					if (emptySlotCount < 0) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	//</editor-fold>
	
	// This will attempt to give the player an item in their hotbar.
	// If the player has no space, it will favor their main hand and set their main hand item in an empty slot.
	// If there is no room at all, an error appears.
	/*public static boolean giveItemInHandHotBar(Player player, ItemStack giveItem) {
		PlayerInventory inventory = player.getInventory();
		int emptySlot = inventory.firstEmpty();
		
		if (emptySlot == -1) {
			MessageSender.sendMessage(player, Message.CMD_ERROR_NO_SPACE);
			return false;
		}
		if (emptySlot >= 0 && emptySlot <= 8) {
			if (makeAirItemNull(inventory.getItemInMainHand()) != null) {
				inventory.setItem(emptySlot, giveItem);
			}
			
			inventory.setHeldItemSlot(emptySlot);
		} else {
			inventory.setItem(emptySlot, inventory.getItemInMainHand());
			inventory.setItemInMainHand(giveItem);
		}
		
		return true;
	}*/
}
