package ga.justreddy.wiki.rhomes.menus.menus.anvil;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.menus.menus.EditHomeMenu;
import ga.justreddy.wiki.rhomes.utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AnvilMenu {

  public static void openRenameHomeGui(String name, Player player) {
    new AnvilGUI.Builder()
        .onComplete((p, newName) -> {
          RHomes.getHomes().getDatabase().renameHome(name, p, ChatColor.stripColor(newName));
          return AnvilGUI.Response.close();
        })
        .itemLeft(XMaterial.PAPER.parseItem())
        .title(Utils.format("&aEdit Home Name"))
        .text(Utils.format(name))
        .plugin(RHomes.getHomes())
        .open(player);
  }

  public static void openDisplayNameGui(String name, Player player) {
    new AnvilGUI.Builder()
        .onComplete((p, newName) -> {
          RHomes.getHomes().getDatabase().setDisplayName(name, p, newName);
          return AnvilGUI.Response.close();
        })
        .itemLeft(XMaterial.PAPER.parseItem())
        .title(Utils.format("&aEdit Home Displayname"))
        .text(Utils.format(name))
        .plugin(RHomes.getHomes())
        .open(player);
  }

  public static void addBlacklistedPlayer(String name, Player player) {
    new AnvilGUI.Builder()
        .onComplete((p, blacklistedPlayer) -> {
          RHomes.getHomes().getDatabase().addPlayerToBlackList(name, p, Bukkit.getOfflinePlayer(blacklistedPlayer));
          return AnvilGUI.Response.close();
        })
        .itemLeft(XMaterial.PAPER.parseItem())
        .title(Utils.format("&aAdd a player to your homes blacklist"))
        .text("Add someone to your homes blacklist")
        .plugin(RHomes.getHomes())
        .open(player);
  }

  public static void removeBlacklistedPlayer(String name, Player player) {
    new AnvilGUI.Builder()
        .onComplete((p, blacklistedPlayer) -> {
          RHomes.getHomes().getDatabase().removePlayerFromBlackList(name, p, Bukkit.getOfflinePlayer(blacklistedPlayer));
          return AnvilGUI.Response.close();
        })
        .itemLeft(XMaterial.PAPER.parseItem())
        .title(Utils.format("&aRemove a player from your homes blacklist"))
        .text("Remove someone from your homes blacklist")
        .plugin(RHomes.getHomes())
        .open(player);
  }

}
