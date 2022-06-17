package me.sashak.inventoryutil.filter.slot;

public enum SlotFilterType {
	
	HOTBAR(new SlotFilterImpl.HotbarFilter()),
	UPPER_INVENTORY(new SlotFilterImpl.UpperInventoryFilter()),
	MAIN_INVENTORY(new SlotFilterImpl.MainInventoryFilter()),
	ENTIRE_INVENTORY(new SlotFilterImpl.EntireInventoryFilter()),
	
	ARMOR(new SlotFilterImpl.ArmorFilter()),
	MAIN_HAND(new SlotFilterImpl.MainHandFilter()),
	OFF_HAND(new SlotFilterImpl.OffHandFilter()),
	
	CONTAINER_SLOTS(new SlotFilterImpl.ContainerSlotsFilter());
	
	final SlotFilterImpl slotGroup;
	
	SlotFilterType(SlotFilterImpl slotGroup) {
		this.slotGroup = slotGroup;
	}
	
	
	public static SlotFilter getFromTagName(String string) {
		SlotFilterType groupType;
		
		switch (string) {
			case "Entire inventory": groupType = ENTIRE_INVENTORY; break;
			case "Main inventory":   groupType = MAIN_INVENTORY; break;
			case "Upper inventory":  groupType = UPPER_INVENTORY; break;
			case "Hotbar":           groupType = HOTBAR; break;
			case "Armor":            groupType = ARMOR; break;
			
			default: throw new UnsupportedOperationException();
		}
		
		return new SlotFilter(groupType);
	}
}
