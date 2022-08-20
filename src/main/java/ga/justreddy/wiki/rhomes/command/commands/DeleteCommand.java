package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.utils.Utils;
import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import org.bukkit.entity.Player;

public class DeleteCommand implements ICommand {

  @Override
  public String name() {
    return "delete";
  }

  @Override
  public String description() {
    return "Delete one of your homes";
  }

  @Override
  public String syntax() {
    return "/home delete <name>";
  }

  @Override
  public String permission() {
    return "rhomes.command.delete";
  }

  @Override
  public void run(Player player, String[] args) {
    try {
      String name = args[1];
      RHomes.getHomes().getDatabase().deleteHome(name, player);
    }catch (IndexOutOfBoundsException ex) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.invalid-arguments").replaceAll("<syntax>", syntax()));
    }
  }
}
