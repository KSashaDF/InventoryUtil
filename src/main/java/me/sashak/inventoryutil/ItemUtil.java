package me.sashak.inventoryutil;

import me.sashak.inventoryutil.itempredicate.ItemPredicates;
import me.sashak.inventoryutil.slotgroup.*;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemUtil {
	
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
	 * <p>
	 * All Spigot item methods should be treated as nullable. Use this method if null values
	 * are not acceptable, but empty item oddities are not problematic. See
	 * {@link #makeEmptyItemAir(ItemStack)} for more information.
	 */
	public static @NotNull ItemStack makeNullItemAir(@Nullable ItemStack item) {
		return item == null ? getAirItem() : item;
	}
	
	/**
	 * Transforms air items or items with a stack size of 0 into null items.
	 */
	public static @Nullable ItemStack makeEmptyItemNull(@Nullable ItemStack item) {
		return isEmptyItem(item) ? null : item;
	}
	
	/**
	 * Transforms null items, air items, and items with a stack size of 0 or less into air items.
	 * This method is useful if non-air items with a stack size of 0, air items with a stack
	 * size greater than 0, or null items are not acceptable. If only null items are problematic,
	 * then consider using {@link #makeNullItemAir(ItemStack)} instead.
	 * <p>
	 * Spigot item methods can return items that violate any of the above conditions. This
	 * method should be used if any of the above conditions are unacceptable.
	 */
	public static @NotNull ItemStack makeEmptyItemAir(@Nullable ItemStack item) {
		return isEmptyItem(item) ? getAirItem() : item;
	}
	
	/**
	 * Duplicates the functionality of {@link #makeEmptyItemAir(ItemStack)}, but
	 * also clones the item if the item is not empty. If the item is empty, a new air item
	 * is created.
	 */
	public static @NotNull ItemStack cloneAndMakeEmptyItemAir(@Nullable ItemStack item) {
		if (isEmptyItem(item)) {
			return getAirItem();
		} else {
			return item.clone();
		}
	}
	
	/**
	 * @return true if the item is null, is air, or has a stack size of 0 or less
	 */
	public static boolean isEmptyItem(@Nullable ItemStack item) {
		return item == null || item.getType() == Material.AIR || item.getAmount() < 1;
	}
	
	/**
	 * @return the item's material. If the item is empty, {@link Material#AIR} is returned
	 */
	@NotNull
	public static Material getItemType(@Nullable ItemStack item) {
		if (item == null || item.getAmount() < 1) {
			return Material.AIR;
		} else {
			return item.getType();
		}
	}
	
	/**
	 * @return true if the items are similar, according to {@link ItemStack#isSimilar(ItemStack)}
	 */
	public static boolean areItemsSimilar(@Nullable ItemStack a, @Nullable ItemStack b) {
		if (isEmptyItem(a) && isEmptyItem(b)) {
			return true;
		}
		
		return a != null && a.isSimilar(b);
	}
	
	/**
	 * @return true if the items are equal, according to {@link ItemStack#equals(Object)}
	 */
	public static boolean areItemsEqual(@Nullable ItemStack a, @Nullable ItemStack b) {
		if (isEmptyItem(a) && isEmptyItem(b)) {
			return true;
		}
		
		return a != null && a.equals(b);
	}
	
	//<editor-fold desc="> combineSimilarStacks methods" defaultstate="collapsed">
	/**
	 * @see #combineSimilarStacks(List, boolean)
	 */
	public static ItemStack[] combineSimilarStacks(ItemStack[] items) {
		return combineSimilarStacks(items, true);
	}
	
	/**
	 * @see #combineSimilarStacks(List, boolean)
	 */
	public static ArrayList<ItemStack> combineSimilarStacks(List<ItemStack> items) {
		return combineSimilarStacks(items, true);
	}
	
	/**
	 * @see #combineSimilarStacks(List, boolean)
	 */
	public static ItemStack[] combineSimilarStacks(ItemStack[] items, boolean respectMaxStackSize) {
		return combineSimilarStacks(items, respectMaxStackSize, true).toArray(new ItemStack[0]);
	}
	
	/**
	 * Consolidates all items into as few stacks as possible. This method will clone all items.
	 * Empty items are ignored.
	 *
	 * @param respectMaxStackSize if false, all similar items will be combined into a single
	 *                            stack, potentially resulting in stack sizes that far exceed
	 *                            the max stack sizes of items
	 * @return a consolidated list of items
	 */
	public static ArrayList<ItemStack> combineSimilarStacks(List<ItemStack> items, boolean respectMaxStackSize) {
		return combineSimilarStacks(items.toArray(new ItemStack[0]), respectMaxStackSize, true);
	}
	
	/**
	 * @see #combineSimilarStacksNoClone(List)
	 */
	public static ItemStack[] combineSimilarStacksNoClone(ItemStack[] items) {
		return combineSimilarStacks(items, false, false).toArray(new ItemStack[0]);
	}
	
	/**
	 * Consolidates all items into as few stacks as possible. This method does not respect max
	 * stack sizes, meaning that all similar items will be combined into a single stack. This will
	 * potentially result in stack sizes that far exceed the max stack sizes of items. Empty items
	 * are ignored.
	 * <p>
	 * No items are cloned. This means that items passed into this method may be modified.
	 *
	 * @return a consolidated list of items
	 */
	public static ArrayList<ItemStack> combineSimilarStacksNoClone(List<ItemStack> items) {
		return combineSimilarStacks(items.toArray(new ItemStack[0]), false, false);
	}
	
	// ----------- private methods -----------
	
	private static ArrayList<ItemStack> combineSimilarStacks(ItemStack[] items, boolean respectMaxStackSize, boolean cloneItems) {
		if (respectMaxStackSize) {
			return combineSimilarStacksRespectMaxSize(items); // Always clone items
		} else {
			return combineSimilarStacksNoRespectMaxSize(items, cloneItems);
		}
	}
	
	private static ArrayList<ItemStack> combineSimilarStacksNoRespectMaxSize(ItemStack[] items, boolean cloneItems) {
		ArrayList<ItemStack> combinedStacks = new ArrayList<>(items.length);
		
		if (items.length == 1) {
			if (cloneItems) {
				combinedStacks.add(items[0].clone());
			} else {
				combinedStacks.add(items[0]);
			}
			
			return combinedStacks;
		}
		
		for (ItemStack item : items) {
			if (isEmptyItem(item)) {
				continue;
			}
			
			for (int combinedIndex = 0; combinedIndex <= combinedStacks.size(); combinedIndex++) {
				if (combinedIndex == combinedStacks.size()) {
					if (cloneItems) {
						item = item.clone();
					}
					
					combinedStacks.add(item);
					break;
				} else {
					ItemStack combinedStack = combinedStacks.get(combinedIndex);
					
					if (combinedStack.isSimilar(item)) {
						combinedStack.setAmount(combinedStack.getAmount() + item.getAmount());
						break;
					}
				}
			}
		}
		
		return combinedStacks;
	}
	
	private static ArrayList<ItemStack> combineSimilarStacksRespectMaxSize(ItemStack[] items) {
		ArrayList<ItemStack> combinedStacks = new ArrayList<>(items.length);
		
		for (ItemStack item : items) {
			if (isEmptyItem(item)) {
				continue;
			}
			
			for (int combinedIndex = 0; combinedIndex <= combinedStacks.size(); combinedIndex++) {
				if (combinedIndex == combinedStacks.size()) {
					combinedStacks.add(item.clone());
					break;
				} else {
					ItemStack combinedStack = combinedStacks.get(combinedIndex);
					
					if (combinedStack.isSimilar(item)) {
						int maxStackSize = item.getMaxStackSize();
						
						if (item.getAmount() <= maxStackSize - combinedStack.getAmount()) {
							// Put all of the item into the combinedStack and stop iterating
							combinedStack.setAmount(combinedStack.getAmount() + item.getAmount());
							break;
						} else {
							// Put only part of the item into the combinedStack and keep iterating
							item.setAmount(item.getAmount() - (maxStackSize - combinedStack.getAmount()));
							combinedStack.setAmount(maxStackSize);
						}
					}
				}
			}
		}
		
		return combinedStacks;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getMatchingItemCount methods" defaultstate="collapsed">
	public static int getItemCount(InventoryHolder holder, SlotGroup group) {
		return getItemCount(holder.getInventory(), group);
	}
	
	public static int getItemCount(Inventory inv, SlotGroup group) {
		ItemStack[] items = group.getSlotItems(inv);
		int itemCount = 0;
		
		for (ItemStack item : items) {
			itemCount += item.getAmount();
		}
		
		return itemCount;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getFirstEmptySlot methods" defaultstate="collapsed">
	/**
	 * @see #getFirstEmptySlot(Inventory, SlotGroup)
	 */
	public static int getFirstEmptySlot(InventoryHolder holder, SlotGroup group) {
		return getFirstEmptySlot(holder.getInventory(), group);
	}
	
	/**
	 * @return the first empty slot in the slot group, -1 if no slots are empty. Whether a slot
	 * is empty is determined by the {@link #isEmptyItem(ItemStack)} method
	 */
	public static int getFirstEmptySlot(Inventory inv, SlotGroup group) {
		for (int slot : group.getSlots(inv)) {
			if (isEmptyItem(inv.getItem(slot))) {
				return slot;
			}
		}
		
		return -1;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getEmptySlotCount methods" defaultstate="collapsed">
	/**
	 * @see #getEmptySlotCount(Inventory, SlotGroup)
	 */
	public static int getEmptySlotCount(InventoryHolder holder, SlotGroup group) {
		return getEmptySlotCount(holder.getInventory(), group);
	}
	
	/**
	 * @return the amount of empty slots in the inventory
	 */
	public static int getEmptySlotCount(Inventory inv, SlotGroup group) {
		int emptySlots = 0;
		
		for (ItemStack slotItem : group.getSlotItems(inv)) {
			if (isEmptyItem(slotItem)) {
				emptySlots++;
			}
		}
		
		return emptySlots;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getItemCount methods" defaultstate="collapsed">
	/**
	 * Adds up the stack sizes of the items in the array. Null and air items are ignored.
	 */
	public static int getItemCount(ItemStack... itemStacks) {
		int itemCount = 0;
		
		for (ItemStack itemStack : itemStacks) {
			if (!isEmptyItem(itemStack)) {
				itemCount += itemStack.getAmount();
			}
		}
		
		return itemCount;
	}
	
	/**
	 * Adds up the stack sizes of the items in the list. Null and air items are ignored.
	 */
	public static int getItemCount(ArrayList<ItemStack> itemStacks) {
		int itemCount = 0;
		
		for (ItemStack itemStack : itemStacks) {
			if (!isEmptyItem(itemStack)) {
				itemCount += itemStack.getAmount();
			}
		}
		
		return itemCount;
	}
	//</editor-fold>
	
	//<editor-fold desc="> hasRoomForItems methods" defaultstate="collapsed">
	/**
	 * @see #hasRoomForItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static boolean playerHasRoomForItems(InventoryHolder holder, ItemStack... itemStacks) {
		return playerHasRoomForItems(holder.getInventory(), itemStacks);
	}
	
	/**
	 * @see #hasRoomForItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static boolean playerHasRoomForItems(Inventory inv, ItemStack... itemStacks) {
		return hasRoomForItems(inv, SlotGroups.PLAYER_MAIN_INV, itemStacks);
	}
	
	/**
	 * @see #hasRoomForItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static boolean hasRoomForItems(InventoryHolder holder, SlotGroup group, ItemStack... itemStacks) {
		return hasRoomForItems(holder.getInventory(), group, itemStacks);
	}
	
	/**
	 * Determines if the items can fit into the inventory. Any slots that don't satisfy the
	 * filter are excluded from the calculation. Any empty items are also ignored.
	 */
	@SuppressWarnings("ConstantConditions")
	public static boolean hasRoomForItems(Inventory inv, SlotGroup group, ItemStack... itemStacks) {
		itemStacks = combineSimilarStacks(itemStacks, false);
		int emptySlotCount = getEmptySlotCount(inv, group);
		
		for (ItemStack itemStack : itemStacks) {
			if (isEmptyItem(itemStack)) {
				continue;
			}
			
			int maxSize = inv.getMaxStackSize();
			int requiredAmount = itemStack.getAmount();
			int[] matchingSlots = group.getSlots(inv, ItemPredicates.requireSimilarity(itemStack));
			
			for (int matchingSlot : matchingSlots) {
				ItemStack slotItem = inv.getItem(matchingSlot);
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
		
		return true;
	}
	//</editor-fold>
}
