package org.justunk.reloadPlugin.events;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("All")
public class TabCompletions implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (args.length == 1) {
            if (args[0].isEmpty()) {
                completions.addAll(Arrays.asList("reload", "disable", "enable", "update"));
            } else {
                for (String subcmd : Arrays.asList("reload", "disable", "enable", "update")) {
                    if (subcmd.startsWith(args[0].toLowerCase())) {
                        completions.add(subcmd);
                    }
                }
            }
        } else if (args.length == 2) {
            if (args[1].isEmpty()) {
                for (Plugin plugin : pluginManager.getPlugins()) {
                    completions.add(plugin.getName());
                }
            } else {
                for (Plugin plugin : pluginManager.getPlugins()) {
                    if (plugin.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(plugin.getName());
                    }
                }
            }
        }

        return completions;
    }
}
