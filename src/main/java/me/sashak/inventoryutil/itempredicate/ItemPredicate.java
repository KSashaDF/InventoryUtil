package me.sashak.inventoryutil.itempredicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemPredicate {
	
	boolean test(@Nullable ItemStack item);
	
	
	default ItemPredicate invert() {
		return new InvertingItemPredicate(this);
	}
	
	default ItemPredicate and(ItemPredicate other) {
		return new AndItemPredicate(this, other);
	}
	
	default ItemPredicate or(ItemPredicate other) {
		return new OrItemPredicate(this, other);
	}
}
