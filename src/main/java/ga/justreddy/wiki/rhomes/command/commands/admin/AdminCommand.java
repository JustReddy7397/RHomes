package ga.justreddy.wiki.rhomes.command.commands.admin;

import ga.justreddy.wiki.rhomes.command.ICommand;
import org.bukkit.entity.Player;

public class AdminCommand implements ICommand {

  @Override
  public String name() {
    return "admin";
  }

  @Override
  public String description() {
    return "Admin Commands";
  }

  @Override
  public String syntax() {
    return "/home admin <args>";
  }

  @Override
  public String permission() {
    return "rhomes.command.admin";
  }

  @Override
  public void run(Player player, String[] args) {
    switch (args[1]) {

    }
  }

  // TODO

}
