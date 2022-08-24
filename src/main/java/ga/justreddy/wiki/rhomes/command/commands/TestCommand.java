package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.database.Database;
import ga.justreddy.wiki.rhomes.utils.Utils;
import org.bukkit.entity.Player;

public class TestCommand implements ICommand {

  @Override
  public String name() {
    return "test";
  }

  @Override
  public String description() {
    return "test";
  }

  @Override
  public String syntax() {
    return "test";
  }

  @Override
  public String permission() {
    return "test";
  }

  @Override
  public void run(Player player, String[] args) {
    Database database = RHomes.getHomes().getDatabase();
    String location = Utils.toLocation(player.getLocation());
    String name = args[1];
    switch (args[2]) {
      case "pos1":
        database.setHighBound(name, player, location);
        break;
      case "pos2":
        database.setLowBound(name, player, location);
        break;
      case "save":
        database.setClaimArea(name, player);
        break;
    }


  }
}
