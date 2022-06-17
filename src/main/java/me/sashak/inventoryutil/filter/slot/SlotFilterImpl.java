package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

interface SlotFilterImpl {
	
	boolean isWithinGroup(int slot, Inventory inventory);
	
	int[] getSlots(Inventory inventory);
	
	
	
	
	class HotbarFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot >= 0 && slot <= 8;
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
		}
	}
	
	class UpperInventoryFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot >= 9 && slot <= 35;
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {
					9 , 10, 11, 12, 13, 14, 15, 16, 17,
					18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35};
		}
	}
	
	class MainInventoryFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot >= 0 && slot <= 35;
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {
					0 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 ,
					9 , 10, 11, 12, 13, 14, 15, 16, 17,
					18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35};
		}
	}
	
	class EntireInventoryFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot >= 0 && slot <= 40;
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {
					0 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 ,
					9 , 10, 11, 12, 13, 14, 15, 16, 17,
					18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35,
					36, 37, 38, 39, 40};
		}
	}
	
	class ArmorFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot >= 36 && slot <= 39;
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {36, 37, 38, 39};
		}
	}
	
	class MainHandFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot == ((PlayerInventory) inventory).getHeldItemSlot();
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {((PlayerInventory) inventory).getHeldItemSlot()};
		}
	}
	
	class OffHandFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot == 40;
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			return new int[] {40};
		}
	}
	
	
	class ContainerSlotsFilter implements SlotFilterImpl {
		
		@Override
		public boolean isWithinGroup(int slot, Inventory inventory) {
			return slot >= 0 && slot < inventory.getSize();
		}
		
		@Override
		public int[] getSlots(Inventory inventory) {
			int[] slots = new int[inventory.getSize()];
			
			for (int slot = 0; slot < slots.length; slot++) {
				slots[slot] = slot;
			}
			
			return slots;
		}
	}
}
