package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class ItemPredicates {
	
	private static final ItemPredicate ALLOW_ALL = item -> true;
	private static final ItemPredicate ONLY_EMPTY_ITEMS = ItemUtil::isEmptyItem;
	private static final ItemPredicate NO_EMPTY_ITEMS = item -> !ItemUtil.isEmptyItem(item);
	private static final ItemPredicate ONLY_PARTIAL_STACKS = item -> !ItemUtil.isEmptyItem(item) && item.getAmount() < item.getMaxStackSize();
	
	public static ItemPredicate allowAll() {
		return ALLOW_ALL;
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
		return ONLY_EMPTY_ITEMS;
	}
	
	public static ItemPredicate noEmptyItems() {
		return NO_EMPTY_ITEMS;
	}
	
	public static ItemPredicate onlyPartialStacks() {
		return ONLY_PARTIAL_STACKS;
	}
	
	public static ItemPredicate checkNamePredicate(Predicate<String> displayNamePredicate, boolean stripColors) {
		return new DisplayNameItemPredicate(displayNamePredicate, stripColors);
	}
}
