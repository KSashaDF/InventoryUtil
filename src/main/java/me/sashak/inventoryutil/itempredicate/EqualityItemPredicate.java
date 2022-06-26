package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class EqualityItemPredicate implements ItemPredicate {
	
	private final ItemStack[] items;
	
	public EqualityItemPredicate(ItemStack[] items) {
		this.items = items;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		for (ItemStack predicateItem : items) {
			if (ItemUtil.areItemsEqual(item, predicateItem)) {
				return true;
			}
		}
		
		return false;
	}
}
