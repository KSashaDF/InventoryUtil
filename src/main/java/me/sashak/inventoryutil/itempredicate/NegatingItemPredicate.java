package me.sashak.inventoryutil.itempredicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class NegatingItemPredicate implements ItemPredicate {
	
	private final ItemPredicate predicate;
	
	public NegatingItemPredicate(ItemPredicate predicate) {
		this.predicate = predicate;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		return !predicate.test(item);
	}
}
