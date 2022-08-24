package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.database.Database;
import ga.justreddy.wiki.rhomes.menus.menus.HomesMenu;
import ga.justreddy.wiki.rhomes.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VisitCommand implements ICommand {

  @Override
  public String name() {
    return "visit";
  }

  @Override
  public String description() {
    return "Visit someone's home ( if public )";
  }

  @Override
  public String syntax() {
    return "/home visit [player] [name]";
  }

  @Override
  public String permission() {
    return "rhomes.command.visit";
  }

  @Override
  public void run(Player player, String[] args) {

    try {

      OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

      String name = args[1];

      RHomes.getHomes().getDatabase().teleportToHome(name, player, target);

    }catch (IndexOutOfBoundsException ex) {
      new HomesMenu().open(player);
    }

  }
}
