import me.sashak.inventoryutil.ItemGiver;
import me.sashak.inventoryutil.filter.InventoryFilter;
import me.sashak.inventoryutil.filter.slot.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class TestClass {
	
	public static void test() {
		ItemGiver.giveItems(new InventoryFilter().setSlotGroup(new SlotFilter(SlotFilterType.MAIN_INVENTORY)).setItemFilter(), getInventory(), new ItemStack(Material.STONE));
	}
	
	static Inventory getInventory() {
		return getInventoryHolder().getInventory();
	}
	
	static InventoryHolder getInventoryHolder() {
		return Bukkit.getPlayer("");
	}
}
