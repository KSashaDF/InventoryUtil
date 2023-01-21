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
	 * @return an air item with a stack size of 0
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
	 * size that isn't 0, or null items are not acceptable. If only null items are problematic,
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
	
	//<editor-fold desc="> getItemCount methods" defaultstate="collapsed">
	/**
	 * @see #getItemCount(Inventory, SlotGroup)
	 */
	public static int getItemCount(InventoryHolder holder, SlotGroup slots) {
		return getItemCount(holder.getInventory(), slots);
	}
	
	/**
	 * Adds up the stack sizes of the items in the slot group. Null and air items are ignored.
	 */
	public static int getItemCount(Inventory inv, SlotGroup slots) {
		ItemStack[] items = slots.getSlotItems(inv);
		int itemCount = 0;
		
		for (ItemStack item : items) {
			if (!isEmptyItem(item)) {
				itemCount += item.getAmount();
			}
		}
		
		return itemCount;
	}
	
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
	
	//<editor-fold desc="> getFirstEmptySlot methods" defaultstate="collapsed">
	/**
	 * @see #getFirstEmptySlot(Inventory, SlotGroup)
	 */
	public static int getFirstEmptySlot(InventoryHolder holder, SlotGroup slots) {
		return getFirstEmptySlot(holder.getInventory(), slots);
	}
	
	/**
	 * @return the first empty slot in the slot group, and -1 if there are no empty slots. Whether
	 * a slot is empty is determined by the {@link #isEmptyItem(ItemStack)} method
	 */
	public static int getFirstEmptySlot(Inventory inv, SlotGroup slots) {
		for (int slot : slots.getSlots(inv)) {
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
	public static int getEmptySlotCount(InventoryHolder holder, SlotGroup slots) {
		return getEmptySlotCount(holder.getInventory(), slots);
	}
	
	/**
	 * @return the amount of empty slots in the slot group
	 */
	public static int getEmptySlotCount(Inventory inv, SlotGroup slots) {
		int emptySlots = 0;
		
		for (ItemStack slotItem : slots.getSlotItems(inv)) {
			if (isEmptyItem(slotItem)) {
				emptySlots++;
			}
		}
		
		return emptySlots;
	}
	//</editor-fold>
	
	//<editor-fold desc="> combineSimilarItems methods" defaultstate="collapsed">
	/**
	 * @see #combineSimilarItems(List, boolean)
	 */
	public static ItemStack[] combineSimilarItems(ItemStack[] items) {
		return combineSimilarItems(items, true);
	}
	
	/**
	 * @see #combineSimilarItems(List, boolean)
	 */
	public static ArrayList<ItemStack> combineSimilarItems(List<ItemStack> items) {
		return combineSimilarItems(items, true);
	}
	
	/**
	 * @see #combineSimilarItems(List, boolean)
	 */
	public static ItemStack[] combineSimilarItems(ItemStack[] items, boolean respectMaxStackSize) {
		return combineSimilarItems(items, respectMaxStackSize, true).toArray(new ItemStack[0]);
	}
	
	/**
	 * Consolidates all items into as few stacks as possible. This method will clone all items.
	 * Empty items are ignored.
	 *
	 * @param respectMaxStackSize if false, all similar items will be combined, potentially
	 *                            resulting in stack sizes that far exceed the max stack sizes
	 *                            of items
	 * @return a consolidated list of items
	 */
	public static ArrayList<ItemStack> combineSimilarItems(List<ItemStack> items, boolean respectMaxStackSize) {
		return combineSimilarItems(items.toArray(new ItemStack[0]), respectMaxStackSize, true);
	}
	
	/**
	 * @see #combineSimilarItemsNoClone(List)
	 */
	public static ItemStack[] combineSimilarItemsNoClone(ItemStack[] items) {
		return combineSimilarItems(items, false, false).toArray(new ItemStack[0]);
	}
	
	/**
	 * Consolidates all items into as few stacks as possible. This method does not respect max
	 * stack sizes, meaning that all similar items will be combined. This will potentially result
	 * in stack sizes that far exceed the max stack sizes of items. Empty items are ignored.
	 * <p>
	 * No items are cloned. This means that items passed into this method may be modified.
	 *
	 * @return a consolidated list of items
	 */
	public static ArrayList<ItemStack> combineSimilarItemsNoClone(List<ItemStack> items) {
		return combineSimilarItems(items.toArray(new ItemStack[0]), false, false);
	}
	
	//<editor-fold desc="> private methods" defaultstate="collapsed">
	private static ArrayList<ItemStack> combineSimilarItems(ItemStack[] items, boolean respectMaxStackSize, boolean cloneItems) {
		if (respectMaxStackSize) {
			return combineSimilarItemsRespectMaxSize(items); // Always clone items
		} else {
			return combineSimilarItemsNoRespectMaxSize(items, cloneItems);
		}
	}
	
	private static ArrayList<ItemStack> combineSimilarItemsNoRespectMaxSize(ItemStack[] items, boolean cloneItems) {
		ArrayList<ItemStack> combinedItems = new ArrayList<>(items.length);
		
		if (items.length == 1) {
			if (cloneItems) {
				combinedItems.add(items[0].clone());
			} else {
				combinedItems.add(items[0]);
			}
			
			return combinedItems;
		}
		
		for (ItemStack item : items) {
			if (isEmptyItem(item)) {
				continue;
			}
			
			for (int combinedIndex = 0; combinedIndex <= combinedItems.size(); combinedIndex++) {
				if (combinedIndex == combinedItems.size()) {
					if (cloneItems) {
						item = item.clone();
					}
					
					combinedItems.add(item);
					break;
				} else {
					ItemStack combinedItem = combinedItems.get(combinedIndex);
					
					if (combinedItem.isSimilar(item)) {
						combinedItem.setAmount(combinedItem.getAmount() + item.getAmount());
						break;
					}
				}
			}
		}
		
		return combinedItems;
	}
	
	private static ArrayList<ItemStack> combineSimilarItemsRespectMaxSize(ItemStack[] items) {
		ArrayList<ItemStack> combinedItems = new ArrayList<>(items.length);
		
		for (ItemStack item : items) {
			if (isEmptyItem(item)) {
				continue;
			}
			
			for (int combinedIndex = 0; combinedIndex <= combinedItems.size(); combinedIndex++) {
				if (combinedIndex == combinedItems.size()) {
					combinedItems.add(item.clone());
					break;
				} else {
					ItemStack combinedItem = combinedItems.get(combinedIndex);
					
					if (combinedItem.isSimilar(item)) {
						int maxStackSize = item.getMaxStackSize();
						
						if (item.getAmount() <= maxStackSize - combinedItem.getAmount()) {
							// Put all of the item into the combinedItem and stop iterating
							combinedItem.setAmount(combinedItem.getAmount() + item.getAmount());
							break;
						} else {
							// Put only part of the item into the combinedItem and keep iterating
							item.setAmount(item.getAmount() - (maxStackSize - combinedItem.getAmount()));
							combinedItem.setAmount(maxStackSize);
						}
					}
				}
			}
		}
		
		return combinedItems;
	}
	//</editor-fold>
	
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
	public static boolean hasRoomForItems(InventoryHolder holder, SlotGroup slots, ItemStack... itemStacks) {
		return hasRoomForItems(holder.getInventory(), slots, itemStacks);
	}
	
	/**
	 * Determines if the items can fit into the slot group. Empty items are ignored.
	 */
	@SuppressWarnings("ConstantConditions")
	public static boolean hasRoomForItems(Inventory inv, SlotGroup slots, ItemStack... itemStacks) {
		itemStacks = combineSimilarItems(itemStacks, false);
		int emptySlotCount = getEmptySlotCount(inv, slots);
		
		for (ItemStack itemStack : itemStacks) {
			if (isEmptyItem(itemStack)) {
				continue;
			}
			
			int maxSize = inv.getMaxStackSize();
			int requiredAmount = itemStack.getAmount();
			int[] matchingSlots = slots.getSlots(inv, ItemPredicates.requireSimilarity(itemStack));
			
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
	
	//<editor-fold desc="> hasAllItems methods" defaultstate="collapsed">
	/**
	 * @see #hasAllItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static boolean hasAllItems(InventoryHolder holder, SlotGroup slots, List<ItemStack> items) {
		return hasAllItems(holder, slots, items.toArray(new ItemStack[0]));
	}
	
	/**
	 * @see #hasAllItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static boolean hasAllItems(Inventory inv, SlotGroup slots, List<ItemStack> items) {
		return hasAllItems(inv, slots, items.toArray(new ItemStack[0]));
	}
	
	/**
	 * @see #hasAllItems(Inventory, SlotGroup, ItemStack...)
	 */
	public static boolean hasAllItems(InventoryHolder holder, SlotGroup slots, ItemStack... items) {
		return hasAllItems(holder.getInventory(), slots, items);
	}
	
	/**
	 * @return whether the slot group contains all the inputted items. Empty items are ignored
	 */
	public static boolean hasAllItems(Inventory inv, SlotGroup slots, ItemStack... items) {
		ArrayList<ItemStack> combinedItems = combineSimilarItems(items, false, true);
		
		for (ItemStack item : combinedItems) {
			int amountInGroup = getItemCount(inv, slots.filterSlots(ItemPredicates.requireSimilarity(item)));
			
			if (amountInGroup < item.getAmount()) {
				return false;
			}
		}
		
		return true;
	}
	//</editor-fold>
}
