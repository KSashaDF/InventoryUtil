package me.sashak.inventoryutil.itempredicate;

import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class MaterialItemPredicate implements ItemPredicate {
	
	private final Material[] materials;
	
	public MaterialItemPredicate(Material[] materials) {
		this.materials = materials;
	}
	
	public MaterialItemPredicate(ItemStack[] items) {
		Material[] materials = new Material[items.length];
		
		for (int i = 0; i < items.length; i++) {
			materials[i] = ItemUtil.getItemType(items[i]);
		}
		
		this.materials = materials;
	}
	
	@Override
	public boolean test(@Nullable ItemStack item) {
		Material itemMaterial = ItemUtil.getItemType(item);
		
		for (Material material : materials) {
			if (itemMaterial == material) {
				return true;
			}
		}
		
		return false;
	}
}
