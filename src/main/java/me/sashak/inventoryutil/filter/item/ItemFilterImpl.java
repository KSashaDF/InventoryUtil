package me.sashak.inventoryutil.filter.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

interface ItemFilterImpl {
	
	boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems);
	
	
	
	
	class AllowAllFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			return true;
		}
	}
	
	class NonNullFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			return checkItem != null;
		}
	}
	
	class SameMaterialFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			Material checkItemMaterial = checkItem == null ? null : checkItem.getType();
			
			for (ItemStack filterItem : filterItems) {
				if (checkItemMaterial == (filterItem == null ? null : filterItem.getType())) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	class SimilarNoDurabilityItemsFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			if (checkItem != null) {
				checkItem = checkItem.clone();
				ItemMeta checkMeta = checkItem.getItemMeta();
				
				if (checkMeta instanceof Damageable) {
					Damageable damageable = (Damageable) checkMeta;
					damageable.setDamage(0);
				}
				checkItem.setItemMeta(checkMeta);
			}
			
			for (ItemStack filterItem : filterItems) {
				if (checkItem == filterItem) {
					return true;
				} else if (checkItem != null && filterItem != null) {
					filterItem = filterItem.clone();
					ItemMeta filterMeta = filterItem.getItemMeta();
					
					if (filterMeta instanceof Damageable) {
						Damageable damageable = (Damageable) filterMeta;
						damageable.setDamage(0);
					}
					filterItem.setItemMeta(filterMeta);
					
					return checkItem.isSimilar(filterItem);
				}
			}
			
			return false;
		}
	}
	
	class SimilarItemsFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			
			for (ItemStack filterItem : filterItems) {
				if (checkItem == filterItem || (checkItem != null && checkItem.isSimilar(filterItem))) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	class EqualItemsFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			
			for (ItemStack filterItem : filterItems) {
				if (Objects.equals(checkItem, filterItem)) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	class NonFullStacksFilter implements ItemFilterImpl {
		
		@Override
		public boolean canAffectItem(ItemStack checkItem, ItemStack[] filterItems) {
			return checkItem.getAmount() < checkItem.getMaxStackSize();
		}
	}
}
