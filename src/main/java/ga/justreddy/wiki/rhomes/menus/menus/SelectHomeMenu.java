package ga.justreddy.wiki.rhomes.menus.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.menus.AbstractPaginatedMenu;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SelectHomeMenu extends AbstractPaginatedMenu {

  @Override
  public String name() {
    return "&aEdit One Of Your Homes";
  }

  @Override
  public int size() {
    return 36;
  }

  @Override
  public void handle(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();
    ItemStack item = e.getCurrentItem();
    if (item.getType() == Material.IRON_DOOR) {
      player.closeInventory();
      new EditHomeMenu(ChatColor.stripColor(item.getItemMeta().getDisplayName())).open(player);
    }
  }

  @Override
  public void setMenuItems(Player player) {
    for (int i = 0; i < 9; i++) {
      if (inventory.getItem(i) == null) {
        inventory.setItem(i, FILLER_GLASS);
      }
    }
    for (int i = 27; i < 36; i++) {
      if (inventory.getItem(i) == null) {
        inventory.setItem(i, FILLER_GLASS);
      }
    }
    inventory.setItem(9, FILLER_GLASS);
    inventory.setItem(18, FILLER_GLASS);
    inventory.setItem(17, FILLER_GLASS);
    inventory.setItem(26, FILLER_GLASS);
    RHomes.getHomes()
        .getDatabase()
        .getHomes(player)
        .forEach(
            home -> {
              ItemStack itemStack = XMaterial.IRON_DOOR.parseItem();
              ItemMeta meta = itemStack.getItemMeta();
              meta.setDisplayName(Utils.format("&f" + home.getName()));
              Location location = Utils.getLocation(home.getLocation());
              List<String> lore = new ArrayList<>();
              lore.add("&6%line%");
              lore.add("&7Name: &a" + home.getName());
              lore.add("&7Displayname: &a" + home.getDisplayName());
              lore.add("");
              lore.add("&7Location:");
              lore.add("  &aWorld: " + location.getWorld().getName());
              lore.add("  &aX: " + Math.round(location.getX()));
              lore.add("  &aY: " + Math.round(location.getY()));
              lore.add("  &aZ: " + Math.round(location.getZ()));
              lore.add("");
              lore.add("&7Date created: " + Utils.getFormattedDate(home.getCreated()));
              lore.add("&7Private: " + (home.isPrivate() ? "&aYes" : "&cNo"));
              lore.add("&8Please know that if your home is not private, anyone can visit it.");
              lore.add("&6%line%");
              meta.setLore(Utils.format(lore));
              itemStack.setItemMeta(meta);
              inventory.addItem(itemStack);
            });
  }
}
