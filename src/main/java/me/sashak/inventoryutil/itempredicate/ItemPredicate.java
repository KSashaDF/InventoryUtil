package me.sashak.inventoryutil.itempredicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.*;

import java.util.function.Predicate;

public interface ItemPredicate extends Predicate<ItemStack> {
	
	@Override
	boolean test(@Nullable ItemStack item);
	
	@NotNull
	@Override
	default ItemPredicate negate() {
		return new NegatingItemPredicate(this);
	}
	
	default ItemPredicate and(ItemPredicate other) {
		return new AndItemPredicate(this, other);
	}
	
	default ItemPredicate or(ItemPredicate other) {
		return new OrItemPredicate(this, other);
	}
}
