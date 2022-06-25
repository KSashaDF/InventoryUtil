package me.sashak.inventoryutil;

import me.sashak.inventoryutil.filter.InventoryFilter;
import me.sashak.inventoryutil.filter.slot.SlotGroup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
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
	 * Transforms null items, air items, and items with a stack size of 0 or less into air items.
	 * This method is useful if non-air items with a stack size of 0, air items with a stack
	 * size greater than 0, or null items are not acceptable. If only null items are problematic,
	 * then consider using {@link #makeNullItemAir(ItemStack)} instead.
	 * <p>
	 * Spigot item methods can return items that violate any of the above conditions. This
	 * method should be used if any of the above conditions are unacceptable.
	 */
	public static @NotNull ItemStack makeEmptyItemAir(@Nullable ItemStack itemStack) {
		return isEmptyItem(itemStack) ? getAirItem() : itemStack;
	}
	
	/**
	 * Duplicates the functionality of {@link #makeEmptyItemAir(ItemStack)}, but
	 * also clones the item if the item is not empty. If the item is empty, a new air item
	 * is created.
	 */
	public static @NotNull ItemStack cloneAndMakeEmptyItemAir(@Nullable ItemStack itemStack) {
		if (isEmptyItem(itemStack)) {
			return getAirItem();
		} else {
			return itemStack.clone();
		}
	}
	
	/**
	 * @return true if the item is null, is air, or has a stack size of 0 or less
	 */
	public static boolean isEmptyItem(@Nullable ItemStack itemStack) {
		return itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() < 1;
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
	
	//<editor-fold desc="> getMatchingSlots methods" defaultstate="collapsed">
	public static int[] getMatchingSlots(InventoryFilter filter, Inventory inv) {
		int[] slots = filter.getSlotGroup().getSlots(inv);
		
		return filter.getItemFilter().filterSlots(inv, filter.getFilterItems(), slots);
	}
	
	static int[] getMatchingSlots(ItemStack matchingStack, InventoryFilter filter, Inventory inv) {
		int[] slots = filter.getSlotGroup().getSlots(inv);
		
		return filter.getItemFilter().filterSlots(inv, new ItemStack[] {matchingStack}, slots);
	}
	//</editor-fold>
	
	//<editor-fold desc="> combineSimilarStacks methods" defaultstate="collapsed">
	public static ItemStack[] combineSimilarStacks(ItemStack[] items) {
		return combineSimilarStacks0(items).toArray(new ItemStack[0]);
	}
	
	public static ArrayList<ItemStack> combineSimilarStacks(List<ItemStack> items) {
		return combineSimilarStacks0(items.toArray(new ItemStack[0]));
	}
	
	private static ArrayList<ItemStack> combineSimilarStacks0(ItemStack[] items) {
		ArrayList<ItemStack> combinedStacks = new ArrayList<>(items.length);
		
		if (items.length == 1) {
			combinedStacks.add(items[0]);
			return combinedStacks;
		}
		
		if (items.length == 0) {
			return combinedStacks;
		}
		
		for (ItemStack itemStack : items) {
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
	
	/*public static ItemStack[] combineSimilarStacksNoClone(ItemStack[] itemStacks) {
	
	}*/
	//</editor-fold>
	
	//<editor-fold desc="> getMatchingItemCount methods" defaultstate="collapsed">
	public static int getMatchingItemCount(ItemStack matchingStack, InventoryFilter filter, Player player) {
		return getMatchingItemCount(matchingStack, filter, player.getInventory());
	}
	
	@SuppressWarnings("ConstantConditions")
	public static int getMatchingItemCount(ItemStack matchingStack, InventoryFilter filter, Inventory inv) {
		int[] matchingStackSlots = getMatchingSlots(matchingStack, filter, inv);
		int itemCount = 0;
		
		if (matchingStack != null) {
			for (int matchingStackSlot : matchingStackSlots) {
				ItemStack inventoryStack = inv.getItem(matchingStackSlot);
				
				itemCount += inventoryStack.getAmount();
			}
		}
		
		return itemCount;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getFirstEmptySlot methods" defaultstate="collapsed">
	/**
	 * @return the first empty slot in the slot group, -1 if no slots are empty. Whether a slot
	 * is empty is determined by the {@link #isEmptyItem(ItemStack)} method
	 */
	public static int getFirstEmptySlot(Inventory inv, InventoryFilter filter) {
		for (int slot : filter.getSlotGroup().getSlots(inv)) {
			if (isEmptyItem(inv.getItem(slot))) {
				return slot;
			}
		}
		
		return -1;
	}
	
	/**
	 * @see #getFirstEmptySlot(Inventory, InventoryFilter)
	 */
	public static int getFirstEmptySlot(InventoryHolder holder) {
		return getFirstEmptySlot(holder.getInventory());
	}
	
	/**
	 * @see #getFirstEmptySlot(Inventory, InventoryFilter)
	 */
	public static int getFirstEmptySlot(Inventory inv) {
		return getFirstEmptySlot(inv, new InventoryFilter());
	}
	
	/**
	 * @see #getFirstEmptySlot(Inventory, InventoryFilter)
	 */
	public static int getFirstEmptySlot(InventoryHolder holder, SlotGroup group) {
		return getFirstEmptySlot(holder.getInventory(), group);
	}
	
	/**
	 * @see #getFirstEmptySlot(Inventory, InventoryFilter)
	 */
	public static int getFirstEmptySlot(Inventory inv, SlotGroup group) {
		return getFirstEmptySlot(inv, new InventoryFilter().setSlotGroup(group));
	}
	
	/**
	 * @see #getFirstEmptySlot(Inventory, InventoryFilter)
	 */
	public static int getFirstEmptySlot(InventoryHolder holder, InventoryFilter filter) {
		return getFirstEmptySlot(holder.getInventory(), filter);
	}
	//</editor-fold>
	
	//<editor-fold desc="> getEmptySlotCount methods" defaultstate="collapsed">
	/**
	 * @return the amount of empty slots in the inventory
	 */
	public static int getEmptySlotCount(Inventory inv, InventoryFilter filter) {
		int emptySlots = 0;
		
		for (ItemStack slotItem : filter.getSlotGroup().getSlotItems(inv)) {
			if (isEmptyItem(slotItem)) {
				emptySlots++;
			}
		}
		
		return emptySlots;
	}
	
	/**
	 * @see #getEmptySlotCount(Inventory, InventoryFilter)
	 */
	public static int getEmptySlotCount(InventoryHolder holder) {
		return getEmptySlotCount(holder.getInventory());
	}
	
	/**
	 * @see #getEmptySlotCount(Inventory, InventoryFilter)
	 */
	public static int getEmptySlotCount(Inventory inv) {
		return getEmptySlotCount(inv, new InventoryFilter());
	}
	
	/**
	 * @see #getEmptySlotCount(Inventory, InventoryFilter)
	 */
	public static int getEmptySlotCount(InventoryHolder holder, SlotGroup group) {
		return getEmptySlotCount(holder.getInventory(), group);
	}
	
	/**
	 * @see #getEmptySlotCount(Inventory, InventoryFilter)
	 */
	public static int getEmptySlotCount(Inventory inv, SlotGroup group) {
		return getEmptySlotCount(inv, new InventoryFilter(group));
	}
	
	/**
	 * @see #getEmptySlotCount(Inventory, InventoryFilter)
	 */
	public static int getEmptySlotCount(InventoryHolder holder, InventoryFilter filter) {
		return getEmptySlotCount(holder.getInventory(), filter);
	}
	//</editor-fold>
	
	//<editor-fold desc="> getSimilarSlotCount methods" defaultstate="collapsed">
	public static int getSimilarSlotCount(ItemStack similarStack, InventoryFilter filter, Inventory inv) {
		int similarSlots = 0;
		
		for (ItemStack slotItem : filter.getSlotGroup().getSlotItems(inv)) {
			if (areItemsSimilar(similarStack, slotItem)) {
				similarSlots++;
			}
		}
		
		return similarSlots;
	}
	//</editor-fold>
	
	//<editor-fold desc="> getItemCount methods" defaultstate="collapsed">
	/**
	 * Adds up the stack sizes of the items in the array. Null and air items are ignored.
	 */
	public static int getItemCount(ItemStack... itemStacks) {
		int itemCount = 0;
		
		for (ItemStack itemStack : itemStacks) {
			if (itemStack != null && itemStack.getType() != Material.AIR) {
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
			if (itemStack != null && itemStack.getType() != Material.AIR) {
				itemCount += itemStack.getAmount();
			}
		}
		
		return itemCount;
	}
	//</editor-fold>
	
	//<editor-fold desc="> hasRoomForItem methods" defaultstate="collapsed">
	/**
	 * Determines if the items can fit into the inventory. Any slots that don't satisfy the
	 * filter are excluded from the calculation. Any empty items are also ignored.
	 */
	@SuppressWarnings("ConstantConditions")
	public static boolean hasRoomForItems(Inventory inv, InventoryFilter filter, ItemStack... itemStacks) {
		itemStacks = combineSimilarStacks(itemStacks);
		int emptySlotCount = getEmptySlotCount(inv, filter.getSlotGroup());
		
		for (ItemStack itemStack : itemStacks) {
			if (isEmptyItem(itemStack)) {
				continue;
			}
			
			int maxSize = inv.getMaxStackSize();
			int requiredAmount = itemStack.getAmount();
			int[] matchingSlots = getMatchingSlots(itemStack, filter, inv);
			
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
	
	/**
	 * @see #hasRoomForItems(Inventory, InventoryFilter, ItemStack...)
	 */
	public static boolean hasRoomForItems(InventoryHolder holder, ItemStack... itemStacks) {
		return hasRoomForItems(holder.getInventory(), itemStacks);
	}
	
	/**
	 * @see #hasRoomForItems(Inventory, InventoryFilter, ItemStack...)
	 */
	public static boolean hasRoomForItems(Inventory inv, ItemStack... itemStacks) {
		return hasRoomForItems(inv, new InventoryFilter(), itemStacks);
	}
	
	/**
	 * @see #hasRoomForItems(Inventory, InventoryFilter, ItemStack...)
	 */
	public static boolean hasRoomForItems(InventoryHolder holder, SlotGroup group, ItemStack... itemStacks) {
		return hasRoomForItems(holder.getInventory(), group, itemStacks);
	}
	
	/**
	 * @see #hasRoomForItems(Inventory, InventoryFilter, ItemStack...)
	 */
	public static boolean hasRoomForItems(Inventory inv, SlotGroup group, ItemStack... itemStacks) {
		return hasRoomForItems(inv, new InventoryFilter(group), itemStacks);
	}
	
	/**
	 * @see #hasRoomForItems(Inventory, InventoryFilter, ItemStack...)
	 */
	public static boolean hasRoomForItems(InventoryHolder holder, InventoryFilter filter, ItemStack... itemStacks) {
		return hasRoomForItems(holder.getInventory(), filter, itemStacks);
	}
	//</editor-fold>
}
