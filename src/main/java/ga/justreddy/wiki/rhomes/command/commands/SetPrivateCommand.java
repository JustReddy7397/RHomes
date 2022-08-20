package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import ga.justreddy.wiki.rhomes.utils.Utils;
import org.bukkit.entity.Player;

public class SetPrivateCommand implements ICommand {

  @Override
  public String name() {
    return "setprivate";
  }

  @Override
  public String description() {
    return "Set one of your homes to private";
  }

  @Override
  public String syntax() {
    return "/home setprivate <name> <boolean>";
  }

  @Override
  public String permission() {
    return "rhomes.command.private";
  }

  @Override
  public void run(Player player, String[] args) {
    try {
      String name = args[1];
      boolean _private = Boolean.parseBoolean(args[2]);
      RHomes.getHomes().getDatabase().setPrivate(name, player, _private);
    }catch (IndexOutOfBoundsException ex) {
      Utils.sendMessage(player, RHomes.getHomes().getMessagesConfig().getConfig().getString("error.invalid-arguments").replaceAll("<syntax>", syntax()));
    }
  }
}
