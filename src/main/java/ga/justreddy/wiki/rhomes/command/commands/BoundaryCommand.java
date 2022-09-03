package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.database.Database;
import ga.justreddy.wiki.rhomes.utils.Utils;
import org.bukkit.entity.Player;

public class BoundaryCommand implements ICommand {

  @Override
  public String name() {
    return "boundary";
  }

  @Override
  public String description() {
    return "Set the protection boundaries of your home";
  }

  @Override
  public String syntax() {
    return "/home boundary <name> <pos1/pos2/save>";
  }

  @Override
  public String permission() {
    return "rhomes.command.boundary";
  }

  @Override
  public void run(Player player, String[] args) {
    Database database = RHomes.getHomes().getDatabase();
    String location = Utils.toLocation(player.getLocation());
    try{
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
    }catch (IndexOutOfBoundsException ex) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.invalid-arguments").replaceAll("<syntax>", syntax()));

    }


  }
}
