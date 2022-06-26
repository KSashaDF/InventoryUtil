package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class SimilarityItemPredicate implements ItemPredicate {
	
	final ItemStack[] items;
	
	public SimilarityItemPredicate(ItemStack[] items) {
		this.items = items;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		for (ItemStack predicateItem : items) {
			if (ItemUtil.areItemsSimilar(item, predicateItem)) {
				return true;
			}
		}
		
		return false;
	}
}
