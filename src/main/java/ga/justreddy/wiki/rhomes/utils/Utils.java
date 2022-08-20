package ga.justreddy.wiki.rhomes.utils;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rhomes.RHomes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public class Utils {

  private Utils() {}

  public static final String CHAT_LINE = "&m-----------------------------------------------------";
  public static final String CONSOLE_LINE =
      "*-----------------------------------------------------*";
  public static final String LORE_LINE = "&m--------------------------";

  public static String format(String msg) {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }

  public static List<String> format(List<String> input) {
    List<String> list = new ArrayList<>();
    for (String line : input) list.add(format(line.replace("%line%", LORE_LINE)));
    return list;
  }

  public static void sendMessage(CommandSender sender, String message) {
    sender.sendMessage(
        format(
            message
                .replace("%line%", CHAT_LINE)
                .replace(
                    "<prefix>",
                    RHomes.getHomes().getMessagesConfig().getConfig().getString("prefix"))));
  }

  public static void sendMessage(CommandSender sender, String... message) {
    for (String line : message) {
      sendMessage(sender, line);
    }
  }

  public static void error(Throwable throwable, String description, boolean disable) {
    if (throwable != null) throwable.printStackTrace();

    if (disable) {
      sendConsole(
          "&4%line%",
          "&cAn internal error has occurred in " + RHomes.getHomes().getName() + "!",
          "&cContact the plugin author if you cannot fix this error.",
          "&cDescription: &6" + description,
          "&cThe plugin will now disable.",
          "&4%line%");
    } else {
      sendConsole(
          "&4%line%",
          "&cAn internal error has occurred in "
              + RHomes.getHomes().getDescription().getName()
              + "!",
          "&cContact the plugin author if you cannot fix this error.",
          "&cDescription: &6" + description,
          "&4%line%");
    }

    if (disable && Bukkit.getPluginManager().isPluginEnabled(RHomes.getHomes())) {
      Bukkit.getPluginManager().disablePlugin(RHomes.getHomes());
    }
  }

  public static void errorCommand(CommandSender sender, String description) {
    sendMessage(
        sender,
        "&4%line%",
        "&cAn error occurred while running this command",
        "&cDescription: &6" + description,
        "&4%line%");
  }

  public static void sendConsole(String message) {
    Bukkit.getConsoleSender().sendMessage(format(message.replace("%line%", CONSOLE_LINE)));
  }

  public static void sendConsole(String... message) {
    for (String line : message) {
      sendConsole(line);
    }
  }

  public static String toLocation(Location location) {
    return location.getWorld().getName()
        + " "
        + location.getX()
        + " "
        + location.getY()
        + " "
        + location.getZ()
        + " "
        + location.getYaw()
        + " "
        + location.getPitch();
  }

  public static Location getLocation(String path) {
    String[] split = path.split(" ");
    return new Location(
        Bukkit.getWorld(split[0]),
        Double.parseDouble(split[1]),
        Double.parseDouble(split[2]),
        Double.parseDouble(split[3]),
        Float.parseFloat(split[4]),
        Float.parseFloat(split[5]));
  }

  public static boolean isLocationSafe(Location location) {
    if (!RHomes.getHomes().getSettingsConfig().getConfig().getBoolean("bad-blocks.enabled")) return true;
    int radius = RHomes.getHomes().getSettingsConfig().getConfig().getInt("bad-blocks.radius");
    final List<String> materials = RHomes.getHomes().getSettingsConfig().getConfig().getStringList("bad-blocks.blocks");
    for (int x = -radius; x < radius; x++) {
      for (int y = -radius; y < radius; y++) {
        for (int z = -radius; z < radius; z++) {
          Block block =
              location
                  .getWorld()
                  .getBlockAt(
                      location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
          if (!XMaterial.matchXMaterial(block.getType().name().toUpperCase()).isPresent()) continue;
          if (materials.contains(XMaterial.matchXMaterial(block.getType().name().toUpperCase()).get().name().toUpperCase())) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public static String getFormattedDate(long ms) {
    SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyy");
    return SDF.format(ms);
  }

}
