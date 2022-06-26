import me.sashak.inventoryutil.*;
import me.sashak.inventoryutil.slotgroup.SlotGroups;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class ShopExample {
	
	public static String attemptTransaction(Player buyer, Inventory shopInv, ItemStack[] price, ItemStack[] items) {
		if (!ItemUtil.hasAllItems(buyer, SlotGroups.PLAYER_MAIN_INV, price)) {
			return "Buyer has insufficient funds";
		}
		if (!ItemUtil.hasAllItems(shopInv, SlotGroups.ENTIRE_INV, items)) {
			return "Shop is out of stock";
		}
		
		ItemRemover.removeItems(buyer, SlotGroups.PLAYER_MAIN_INV, price);
		ItemRemover.removeItems(shopInv, SlotGroups.ENTIRE_INV, items);
		
		if (!ItemUtil.hasRoomForItems(buyer, SlotGroups.PLAYER_MAIN_INV, items)) {
			ItemGiver.giveItems(buyer, SlotGroups.PLAYER_MAIN_INV, price);
			ItemGiver.giveItems(shopInv, SlotGroups.ENTIRE_INV, items);
			
			return "Buyer has no room in their inventory";
		}
		if (!ItemUtil.hasRoomForItems(shopInv, SlotGroups.ENTIRE_INV, price)) {
			ItemGiver.giveItems(buyer, SlotGroups.PLAYER_MAIN_INV, price);
			ItemGiver.giveItems(shopInv, SlotGroups.ENTIRE_INV, items);
			
			return "Shop cannot accept payment";
		}
		
		ItemGiver.giveItems(buyer, SlotGroups.PLAYER_MAIN_INV, items);
		ItemGiver.giveItems(shopInv, SlotGroups.ENTIRE_INV, price);
		
		return "Transaction successful";
	}
}
