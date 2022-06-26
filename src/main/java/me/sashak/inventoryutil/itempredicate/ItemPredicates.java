package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemPredicates {
	
	public static ItemPredicate allowAll() {
		return item -> true;
	}
	
	public static ItemPredicate both(ItemPredicate... predicates) {
		return new AndItemPredicate(predicates);
	}
	
	public static ItemPredicate either(ItemPredicate... predicates) {
		return new OrItemPredicate(predicates);
	}
	
	public static ItemPredicate not(ItemPredicate predicate) {
		return new InvertingItemPredicate(predicate);
	}
	
	public static ItemPredicate materialMatches(Material... materials) {
		return new MaterialItemPredicate(materials);
	}
	
	public static ItemPredicate materialMatches(ItemStack... items) {
		return new MaterialItemPredicate(items);
	}
	
	public static ItemPredicate requireSimilarity(ItemStack... items) {
		return new SimilarityItemPredicate(items);
	}
	
	public static ItemPredicate requireSimilarityIgnoreDamage(ItemStack... items) {
		return new SimilarityNoDurabilityItemPredicate(items);
	}
	
	public static ItemPredicate requireEquality(ItemStack... items) {
		return new EqualityItemPredicate(items);
	}
	
	public static ItemPredicate onlyEmptyItems() {
		return ItemUtil::isEmptyItem;
	}
	
	public static ItemPredicate noEmptyItems() {
		return item -> !ItemUtil.isEmptyItem(item);
	}
	
	public static ItemPredicate onlyPartialStacks() {
		return item -> !ItemUtil.isEmptyItem(item) && item.getAmount() < item.getMaxStackSize();
	}
}
