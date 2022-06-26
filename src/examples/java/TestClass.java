import me.sashak.inventoryutil.ItemGiver;
import me.sashak.inventoryutil.slot.SlotFilter;
import me.sashak.inventoryutil.slotgroup.SlotGroups;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class TestClass {
	
	public static void test() {
		ItemGiver.giveItems(new SlotFilter(SlotGroups.PLAYER_MAIN_INV), getInventory(), new ItemStack(Material.STONE));
	}
	
	static Inventory getInventory() {
		return getInventoryHolder().getInventory();
	}
	
	static InventoryHolder getInventoryHolder() {
		return Bukkit.getPlayer("");
	}
}
