package me.sashak.inventoryutil.itempredicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class OrItemPredicate implements ItemPredicate {
	
	private final ItemPredicate[] predicates;
	
	public OrItemPredicate(ItemPredicate... predicates) {
		this.predicates = predicates;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		for (ItemPredicate predicate : predicates) {
			if (predicate.test(item)) {
				return true;
			}
		}
		
		return false;
	}
}
