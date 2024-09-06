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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

@SuppressWarnings("All")
public class Main implements CommandExecutor {

    private final JavaPlugin plugin;
    private static final String REPO_URL = "https://api.github.com/repos/JustUnK/ReloadPlugin/releases/latest";

    public Main(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Utilities.Message((Player) sender,"&cThis command can only be used by other players.");
            return true;
        }

        if (args.length != 2) {
            Utilities.Message((Player) sender,"&cUsage: /reloadplugin <reload|disable|enable|update> <plugin_name>");
            return true;
        }

        String subcmd = args[0].toLowerCase();
        String pluginName = args[1];
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin target = pluginManager.getPlugin(pluginName);

        if (target == null) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Plugin &f&n" + pluginName + "&7 was not found.");
            return true;
        }

        if (target.equals(plugin)) {
            Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7You cannot reload or modify this plugin.");
            return true;
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

                case "update":
                    String currentVersion = plugin.getDescription().getVersion();
                    String latestVersion = getLatestVersion();

                    if (latestVersion == null) {
                        Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7Failed to check for updates. Please check the console.");
                        return false;
                    }

                    if (isUpdateAvailable(currentVersion, latestVersion)) {
                        Utilities.Message((Player) sender, "&c&lReloadPlugin &7| &7A new version is available: &f&n" + latestVersion + "&7. I'm still on &f&n" + currentVersion);
                        Utilities.Message((Player) sender, "&7Download at &f&nhttps://github.com/JustUnK/ReloadPlugin/releases");
                    } else {
                        Utilities.Message((Player) sender, "&a&lReloadPlugin &7| &7You are running the latest version: &f&n" + currentVersion + "&7.");
                    }
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
    private String getLatestVersion() {
        try {
            URL url = new URL(REPO_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    errorResponse.append(line);
                }
                in.close();
                System.out.println("Error: " + errorResponse.toString());
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getString("tag_name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isUpdateAvailable(String currentVersion, String latestVersion) {
        return !currentVersion.equals(latestVersion);
    }
}
