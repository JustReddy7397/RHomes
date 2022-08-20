package ga.justreddy.wiki.rhomes.command.commands;

import ga.justreddy.wiki.rhomes.RHomes;
import ga.justreddy.wiki.rhomes.command.ICommand;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

public class ReloadCommand implements ICommand {

  @Override
  public String name() {
    return "reload";
  }

  @Override
  public String description() {
    return "Reloads the plugins config files";
  }

  @Override
  public String syntax() {
    return "/home reload";
  }

  @Override
  public String permission() {
    return "rhomes.command.reload";
  }

  @Override
  public void run(Player player, String[] args) {
    try {
      RHomes.getHomes().getMessagesConfig().reload();
      RHomes.getHomes().getSettingsConfig().reload();
      RHomes.getHomes().getDatabaseConfig().reload();
    } catch (InvalidConfigurationException | IOException e) {
      e.printStackTrace();
    }
  }
}
