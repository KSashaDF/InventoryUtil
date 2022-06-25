import me.sashak.inventoryutil.ItemGiver;
import me.sashak.inventoryutil.filter.InventoryFilter;
import me.sashak.inventoryutil.filter.slot.SlotGroups;
import org.bukkit.*;
import org.bukkit.inventory.*;

import java.util.Arrays;

public class TestClass {
	
	public static void test() {
		ItemGiver.giveItems(new InventoryFilter(SlotGroups.PLAYER_MAIN_INV), getInventory(), new ItemStack(Material.STONE));
		ItemGiver.putItemInMainHand(null, null);
		Arrays.asList();
	}
	
	static Inventory getInventory() {
		return getInventoryHolder().getInventory();
	}
	
	static InventoryHolder getInventoryHolder() {
		return Bukkit.getPlayer("");
	}
}
