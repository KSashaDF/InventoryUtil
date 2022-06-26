package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.Nullable;

class SimilarityNoDurabilityItemPredicate implements ItemPredicate {
	
	private final ItemStack[] items;
	
	public SimilarityNoDurabilityItemPredicate(ItemStack[] items) {
		for (int i = 0; i < items.length; i++) {
			items[i] = removeDamage(items[i]);
		}
		
		this.items = items;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		item = removeDamage(item);
		
		for (ItemStack predicateItem : items) {
			if (ItemUtil.areItemsSimilar(item, predicateItem)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static ItemStack removeDamage(ItemStack item) {
		if (item != null) {
			ItemMeta itemMeta = item.getItemMeta();
			
			if (itemMeta instanceof Damageable) {
				Damageable damageable = (Damageable) itemMeta;
				damageable.setDamage(0);
				
				item = item.clone();
				item.setItemMeta(itemMeta);
			}
		}
		
		return item;
	}
}