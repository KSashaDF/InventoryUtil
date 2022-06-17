package me.sashak.inventoryutil.filter.item;

public enum ItemFilterType {
	
	ALLOW_ALL(new ItemFilterImpl.AllowAllFilter()),
	NON_NULL(new ItemFilterImpl.NonNullFilter()),
	
	SAME_MATERIAL(new ItemFilterImpl.SameMaterialFilter()),
	SIMILAR_NO_DURABILITY_ITEMS(new ItemFilterImpl.SimilarNoDurabilityItemsFilter()),
	SIMILAR_ITEMS(new ItemFilterImpl.SimilarItemsFilter()),
	EQUAL_ITEMS(new ItemFilterImpl.EqualItemsFilter()),
	
	NON_FULL_STACKS(new ItemFilterImpl.NonFullStacksFilter()),
	;
	
	
	final ItemFilterImpl itemFilter;
	
	ItemFilterType(ItemFilterImpl itemFilter) {
		this.itemFilter = itemFilter;
	}
}
