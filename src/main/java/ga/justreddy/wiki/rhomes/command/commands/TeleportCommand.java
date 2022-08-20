package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.utils.Utils;
import org.bukkit.entity.Player;

public class TeleportCommand implements ICommand {

  @Override
  public String name() {
    return "teleport";
  }

  @Override
  public String description() {
    return "Teleport to one of your homes";
  }

  @Override
  public String syntax() {
    return "/home teleport <name>";
  }

  @Override
  public String permission() {
    return "rhomes.command.teleport";
  }

  @Override
  public void run(Player player, String[] args) {
    try {
      String name = args[1];
      RHomes.getHomes().getDatabase().teleportToHome(name, player);
    }catch (IndexOutOfBoundsException ex) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.invalid-arguments").replaceAll("<syntax>", syntax()));
    }
  }
}
