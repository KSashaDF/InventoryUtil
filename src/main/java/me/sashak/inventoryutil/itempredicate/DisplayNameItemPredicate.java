package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

class DisplayNameItemPredicate implements ItemPredicate {
	
	private final Predicate<String> displayNamePredicate;
	private final boolean stripColors;
	
	public DisplayNameItemPredicate(Predicate<String> displayNamePredicate, boolean stripColors) {
		this.displayNamePredicate = displayNamePredicate;
		this.stripColors = stripColors;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean test(@Nullable ItemStack item) {
		if (ItemUtil.isEmptyItem(item)) {
			return false;
		}
		
		String displayName = item.getItemMeta().getDisplayName();
		if (stripColors) {
			displayName = ChatColor.stripColor(displayName);
		}
		
		return displayNamePredicate.test(displayName);
	}
}
