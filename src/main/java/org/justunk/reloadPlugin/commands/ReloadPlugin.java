package org.justunk.reloadPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.justunk.reloadPlugin.functions.Utilities;

@SuppressWarnings("All")
public class ReloadPlugin implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Utilities.Message((Player) sender,"&cThis command can only be used by other players.");
            return false;
        }

        if (args.length != 2) {
            Utilities.Message((Player) sender,"&cUsage: /reloadplugin <reload|disable|enable> <plugin_name>");
            return false;
        }

        String subcmd = args[0].toLowerCase();
        String pluginName = args[1];
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin target = pluginManager.getPlugin(pluginName);

        if (target == null) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Plugin &f&n" + pluginName + "&7 was not found.");
            return false;
        }

        if (target.equals(plugin)) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7You cannot reload or modify this plugin.");
            return false;
        }

        try {
            switch (subcmd) {
                case "reload":
                    pluginManager.disablePlugin(target);
                    pluginManager.enablePlugin(target);
                    Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Reloaded &f&n" + pluginName + "&7 successfully.");
                    break;

                case "disable":
                    if (!target.isEnabled()) {
                        Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7This plugin is already disabled.");
                        return false;
                    }
                    pluginManager.disablePlugin(target);
                    Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Disabled &f&n" + pluginName + "&7 successfully.");
                    break;

                case "enable":
                    if (target.isEnabled()) {
                        Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7This plugin is already enabled.");
                        return false;
                    }
                    pluginManager.enablePlugin(target);
                    Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Enabled &f&n" + pluginName + "&7 successfully.");
                    break;

                default:
                    Utilities.Message((Player) sender,"&cUsage: /reloadplugin <reload|disable|enable> <plugin_name>");
                    return false;
            }
        } catch (Exception e) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Failed to process &f&n" + pluginName + "&7. Please check the console");
            e.printStackTrace();
        }

        return true;
    }
}
