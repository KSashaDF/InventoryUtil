import me.sashak.inventoryutil.*;
import me.sashak.inventoryutil.slotgroup.SlotGroups;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyItemsExample {
	
	private static final ItemStack SETTINGS_ITEM = new ItemStack(Material.BOOK);
	private static final ItemStack GAME_MENU_ITEM = new ItemStack(Material.EMERALD);
	
	private static final ItemStack[] LOBBY_ITEMS = new ItemStack[] {
			null, null, null, null, GAME_MENU_ITEM, null, null, null, SETTINGS_ITEM
	};
	
	public static void giveLobbyItems(Player player) {
		ItemRemover.clearPlayerInv(player);
		ItemGiver.setItems(player, SlotGroups.PLAYER_HOTBAR, LOBBY_ITEMS);
	}
}
