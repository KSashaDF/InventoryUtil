package me.sashak.inventoryutil.itempredicate;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.Nullable;

class SimilarityNoDurabilityItemPredicate extends SimilarityItemPredicate {
	
	public SimilarityNoDurabilityItemPredicate(ItemStack[] items) {
		super(items);
		
		for (int i = 0; i < items.length; i++) {
			items[i] = removeDamage(items[i]);
		}
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		return super.test(removeDamage(item));
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