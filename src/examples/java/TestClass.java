import me.sashak.inventoryutil.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.*;

public class TestClass {
	
	public static void test() {
	
	}
	
	static Inventory getInventory() {
		return getInventoryHolder().getInventory();
	}
	
	static InventoryHolder getInventoryHolder() {
		return Bukkit.getPlayer("");
	}
}
