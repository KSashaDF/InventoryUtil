package me.sashak.inventoryutil.slotgroup;

import org.bukkit.inventory.Inventory;

import java.util.Arrays;

class ArraySlotGroup extends SlotGroup {
	
	private final int[] array;
	
	public ArraySlotGroup(int[] array) {
		for (int i : array) {
			if (i < 0) {
				throw new IllegalArgumentException("All slots must be >= 0. Given array: " + Arrays.toString(array));
			}
		}
		
		this.array = array;
	}
	
	@Override
	public boolean contains(Inventory inv, int slot) {
		if (slot < 0 || slot >= inv.getSize()) {
			return false;
		}
		
		for (int i : array) {
			if (slot == i) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int[] getSlots(Inventory inv) {
		int[] array = this.array;
		int invSize = inv.getSize();
		
		for (int i : array) {
			if (i >= invSize) {
				return filterSlotArray(array, invSize);
			}
		}
		
		return Arrays.copyOf(array, array.length);
	}
	
	private static int[] filterSlotArray(int[] array, int invSize) {
		int filteredSlotCount = 0;
		
		for (int i : array) {
			if (i >= invSize) {
				filteredSlotCount++;
			}
		}
		
		int[] newArray = new int[array.length - filteredSlotCount];
		int index = 0;
		
		for (int i : array) {
			if (i < invSize) {
				newArray[index] = i;
				index++;
			}
		}
		
		return newArray;
	}
}
