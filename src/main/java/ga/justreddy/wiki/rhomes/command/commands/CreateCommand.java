package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.utils.Utils;
import ga.justreddy.wiki.rhomes.command.ICommand;
import org.bukkit.entity.Player;

public class CreateCommand implements ICommand {

  @Override
  public String name() {
    return "create";
  }

  @Override
  public String description() {
    return "Creates a home";
  }

  @Override
  public String syntax() {
    return "/home create <name>";
  }

  @Override
  public String permission() {
    return "rhomes.command.create";
  }

  @Override
  public void run(Player player, String[] args) {
    try {
      String name = args[1];
      RHomes.getHomes().getDatabase().createHome(name, player, player.getLocation());
    }catch (IndexOutOfBoundsException ex) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.invalid-arguments").replaceAll("<syntax>", syntax()));
    }

  }
}
