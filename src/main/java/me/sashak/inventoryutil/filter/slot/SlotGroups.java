package me.sashak.inventoryutil.filter.slot;

import org.bukkit.inventory.PlayerInventory;

public interface SlotGroups {
	
	SlotGroup PLAYER_MAIN_HAND = SlotGroup.ofSlot(inventory -> ((PlayerInventory) inventory).getHeldItemSlot());
	SlotGroup PLAYER_OFF_HAND = SlotGroup.ofSlot(40);
	SlotGroup PLAYER_ARMOR = SlotGroup.ofRange(36, 39);
	SlotGroup PLAYER_HOTBAR = SlotGroup.ofRange(0, 8);
	SlotGroup PLAYER_MAIN_INV = SlotGroup.ofRange(0, 35);
	SlotGroup PLAYER_UPPER_INV = SlotGroup.ofRange(9, 35);
	SlotGroup PLAYER_ENTIRE_INV = SlotGroup.ofRange(0, 40); // Slightly faster than ENTIRE_INV
	
	SlotGroup ENTIRE_INV = new EntireInvSlotGroup();
}
