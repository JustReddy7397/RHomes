package ga.justreddy.wiki.rhomes.menus.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.menus.AbstractMenu;
import ga.justreddy.wiki.rhomes.menus.menus.anvil.AnvilMenu;
import ga.justreddy.wiki.rhomes.utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlackListMenu extends AbstractMenu {

  private final String name;

  public BlackListMenu(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return "&aAdd Or Remove someone from your house blacklist?";
  }

  @Override
  public int size() {
    return 9;
  }

  @Override
  public void handle(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();
    if (e.getRawSlot() == 3) {
      AnvilMenu.addBlacklistedPlayer(name, player);
    } else if (e.getRawSlot() == 5) {
      AnvilMenu.removeBlacklistedPlayer(name, player);
    }
  }

  @Override
  public void setMenuItems(Player player) {

    ItemStack itemStack = XMaterial.GREEN_WOOL.parseItem();
    ItemMeta meta = itemStack.getItemMeta();
    meta.setDisplayName(Utils.format("&aAdd someone to your homes blacklist"));
    itemStack.setItemMeta(meta);

    ItemStack itemStack2 = XMaterial.RED_WOOL.parseItem();
    ItemMeta meta2 = itemStack2.getItemMeta();
    meta2.setDisplayName(Utils.format("&cRemove someone from your homes blacklist"));
    itemStack2.setItemMeta(meta2);
    inventory.setItem(3, itemStack);
    inventory.setItem(5, itemStack2);

    setFillerGlass();

  }
}
