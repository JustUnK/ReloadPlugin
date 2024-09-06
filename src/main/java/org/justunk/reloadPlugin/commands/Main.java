package org.justunk.reloadPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.justunk.reloadPlugin.functions.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.io.File;

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
            Utilities.Message((Player) sender, "&cThis command can only be used by players.");
            return true;
        }

        if (args.length < 1) {
            Utilities.ActionBar((Player) sender, "Usage &a&n/rp <update|enable|disable|reload> &fto use ReloadPlugin.");
            return true;
        }

        String subcmd = args[0].toLowerCase();
        PluginManager pluginManager = Bukkit.getPluginManager();

        // Commands that require a plugin name
        if (args.length == 2) {
            String pluginName = args[1];
            Plugin target = pluginManager.getPlugin(pluginName);

            if (target == null) {
                Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fwas not found");
                return true;
            }

            if (target.equals(plugin)) {
                Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fcannot be modified.");
                return true;
            }

            switch (subcmd) {
                case "reload":
                    File pluginDir = new File("plugins");
                    File pluginFile = null;

                    for (File file : pluginDir.listFiles()) {
                        if (file.getName().endsWith(".jar")) {
                            PluginDescriptionFile desc = pluginManager.getPlugin(pluginName).getDescription();
                            if (desc.getName().equalsIgnoreCase(pluginName)) {
                                pluginFile = file;
                                break;
                            }
                        }
                    }

                    if (pluginFile == null) {
                        Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fwas not found");
                        return true;
                    }

                    try {
                        pluginManager.disablePlugin(target);
                        pluginManager.loadPlugin(pluginFile);
                        pluginManager.enablePlugin(target);
                        Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&f was reloaded.");
                    } catch (InvalidPluginException e) {
                        Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&f was not reloaded. Please check the console");
                        e.printStackTrace();
                    } catch (InvalidDescriptionException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "disable":
                    if (!target.isEnabled()) {
                        Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fis already disabled");
                        return true;
                    }
                    pluginManager.disablePlugin(target);
                    Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fwas disabled successfully.");
                    break;

                case "enable":
                    if (target.isEnabled()) {
                        Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fis already enabled.");
                        return true;
                    }
                    pluginManager.enablePlugin(target);
                    Utilities.ActionBar((Player) sender, "Plugin &a&n" + pluginName + "&fwas enabled successfully.");
                    break;

                default:
                    Utilities.ActionBar((Player) sender, "Usage &a&n/rp <update|enable|disable|reload> &fto use ReloadPlugin.");
                    return false;
            }
        } else if (args.length == 1) {
            switch (subcmd) {
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
                    Utilities.ActionBar((Player) sender, "Usage &a&n/rp <update|enable|disable|reload> &fto use ReloadPlugin.");
                    return false;
            }
        } else {
            Utilities.ActionBar((Player) sender, "Usage &a&n/rp <update|enable|disable|reload> &fto use ReloadPlugin.");
            return false;
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