package ga.justreddy.wiki.rhomes.menus.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.database.Database;
import ga.justreddy.wiki.rhomes.database.Home;
import ga.justreddy.wiki.rhomes.database.SQLite;
import ga.justreddy.wiki.rhomes.menus.AbstractMenu;
import ga.justreddy.wiki.rhomes.menus.AbstractPaginatedMenu;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HomesMenu extends AbstractPaginatedMenu {


  @Override
  public String name() {
    return "&aView Homes";
  }

  @Override
  public int size() {
    return 45;
  }

  @SneakyThrows
  @Override
  public void handle(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();
    ItemStack item = e.getCurrentItem();

    if (item.getType() != XMaterial.IRON_DOOR.parseMaterial()) return;
    String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
    String owner = ChatColor.stripColor(item.getItemMeta().getLore().get(1).replaceAll("Owner: ", ""));
    RHomes.getHomes().getDatabase().teleportToHome(name, player, Bukkit.getOfflinePlayer(owner));
  }

  @Override
  public void setMenuItems(Player player) {
    addMenuBorder();
    List<Home> homes = RHomes.getHomes().getHomeList().stream().filter(home -> !home.isPrivate()).collect(Collectors.toList());
    for (int i = 0; i < maxItemsPerPage; i++) {
      index = maxItemsPerPage * page + i;
      if(index >= homes.size()) break;
      if(homes.get(i) != null) {
        ItemStack itemStack = XMaterial.IRON_DOOR.parseItem();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Utils.format("&f" + homes.get(i).getName()));
        Location location = Utils.getLocation(homes.get(i).getLocation());
        List<String> lore = new ArrayList<>();
        lore.add("&6%line%");
        lore.add("&7Owner: &a" + Bukkit.getOfflinePlayer(UUID.fromString(homes.get(i).getUuid())).getName());
        lore.add("&7Name: &a" + homes.get(i).getName());
        lore.add("&7Displayname: &a" + homes.get(i).getDisplayName());
        lore.add("");
        lore.add("&7Location:");
        lore.add("  &aWorld: " + location.getWorld().getName());
        lore.add("  &aX: " + Math.round(location.getX()));
        lore.add("  &aY: " + Math.round(location.getY()));
        lore.add("  &aZ: " + Math.round(location.getZ()));
        lore.add("");
        lore.add("&7Date created: " + Utils.getFormattedDate(homes.get(i).getCreated()));
        lore.add("&8Click to teleport to this home.");
        lore.add("&6%line%");
        meta.setLore(Utils.format(lore));
        itemStack.setItemMeta(meta);
        inventory.addItem(itemStack);
      }
    }

  }
}
