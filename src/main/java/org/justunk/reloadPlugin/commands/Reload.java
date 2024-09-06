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
public class Reload implements CommandExecutor {

    private final JavaPlugin plugin;

    public Reload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return true;
        }

        String plugin = args[0];
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin target = pluginManager.getPlugin(plugin);

        if (target == null) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Plugin &f&n" + target.getName().toString() + "&7 was not found.");
            return false;
        }

        if (target.equals(plugin)) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7You cannot disable this plugin.");
            return false;
        }

        try {
            pluginManager.disablePlugin(target);
            pluginManager.enablePlugin(target);
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Reloaded &f&n" + target.getName().toString() + "&7 successfully.");
        } catch(Exception e) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Failed to reload &f&n" + target.getName().toString() + "&7. Please check the console");
            e.printStackTrace();
        }

        return true;
    }

}
