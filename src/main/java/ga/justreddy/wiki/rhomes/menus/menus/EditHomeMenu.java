package ga.justreddy.wiki.rhomes.menus.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.menus.AbstractMenu;
import ga.justreddy.wiki.rhomes.menus.menus.anvil.AnvilMenu;
import ga.justreddy.wiki.rhomes.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditHomeMenu extends AbstractMenu {

  private final String name;

  public EditHomeMenu(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return "&aEdit home: " + name;
  }

  @Override
  public int size() {
    return 36;
  }

  @Override
  public void handle(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();
    ItemStack item = e.getCurrentItem();
    if (item.getType() == XMaterial.BARRIER.parseMaterial() && item.getItemMeta().getDisplayName().equalsIgnoreCase(
        Utils.format("&cBack"))) {
      new SelectHomeMenu().open(player);
      return;
    }
    if (item.getType() == XMaterial.PAPER.parseMaterial() && item.getItemMeta().getDisplayName().equalsIgnoreCase(
        Utils.format("&aRename Home"))) {
       player.closeInventory();
      AnvilMenu.openRenameHomeGui(name, player);
      return;
    }

    if (item.getType() == XMaterial.OAK_SIGN.parseMaterial() && item.getItemMeta().getDisplayName().equalsIgnoreCase(
        Utils.format("&aSet Displayname"))) {
      player.closeInventory();
      AnvilMenu.openDisplayNameGui(name, player);
      return;
    }

    if (e.getRawSlot() == 13) {
      player.closeInventory();
      new BlackListMenu(name).open(player);
      return;
    }



  }

  @Override
  public void setMenuItems(Player player) {
    setFillerGlass();
    ItemStack barrier = XMaterial.BARRIER.parseItem();
    ItemMeta barrierMeta = barrier.getItemMeta();
    barrierMeta.setDisplayName(Utils.format("&cBack"));
    barrier.setItemMeta(barrierMeta);
    inventory.setItem(31, barrier);
    ItemStack rename = XMaterial.PAPER.parseItem();
    ItemMeta renameMeta = rename.getItemMeta();
    renameMeta.setDisplayName(Utils.format("&aRename Home"));
    List<String> lore = new ArrayList<>();
    lore.add("&6%line%");
    lore.add("&7Click this item to rename your home");
    lore.add("&6%line%");
    renameMeta.setLore(Utils.format(lore));
    rename.setItemMeta(renameMeta);
    inventory.setItem(11, rename);
    ItemStack displayname = XMaterial.OAK_SIGN.parseItem();
    ItemMeta displaynameMeta = displayname.getItemMeta();
    displaynameMeta.setDisplayName(Utils.format("&aSet Displayname"));
    List<String> displayNameLore = new ArrayList<>();
    displayNameLore.add("&6%line%");
    displayNameLore.add("&7Click this item to set your homes displayname");
    displayNameLore.add("&6%line%");
    displaynameMeta.setLore(Utils.format(displayNameLore));
    displayname.setItemMeta(displaynameMeta);
    inventory.setItem(15, displayname);
    ItemStack blacklist = XMaterial.IRON_BARS.parseItem();
    ItemMeta blacklistMeta = blacklist.getItemMeta();
    blacklistMeta.setDisplayName(Utils.format("&aBlackisting"));
    List<String> blacklistLore = new ArrayList<>();
    blacklistLore.add("&6%line%");
    blacklistLore.add("&7Click this item to");
    lore.add("&7add/remove someone");
    lore.add("&7from your blacklisted home.");
    blacklistLore.add("&6%line%");
    blacklistMeta.setLore(Utils.format(blacklistLore));
    blacklist.setItemMeta(blacklistMeta);
    inventory.setItem(13, blacklist);


  }
}
