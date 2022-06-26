package me.sashak.inventoryutil.itempredicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class AndItemPredicate implements ItemPredicate {
	
	private final ItemPredicate[] predicates;
	
	public AndItemPredicate(ItemPredicate... predicates) {
		this.predicates = predicates;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		for (ItemPredicate predicate : predicates) {
			if (!predicate.test(item)) {
				return false;
			}
		}
		
		return true;
	}
}
