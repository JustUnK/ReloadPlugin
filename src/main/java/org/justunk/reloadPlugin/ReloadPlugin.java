package org.justunk.reloadPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.justunk.reloadPlugin.events.TabCompletions;
import org.justunk.reloadPlugin.commands.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import org.json.JSONObject;

public final class ReloadPlugin extends JavaPlugin {

    private static final String REPO_URL = "https://api.github.com/repos/JustUnK/ReloadPlugin/releases/latest";

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("reloadplugin")).setExecutor(new Main(this));
        Objects.requireNonNull(getCommand("reloadplugin")).setTabCompleter(new TabCompletions());

        Log("Loaded successfully");

        checkForUpdates();
    }

    @Override
    public void onDisable() {
        Log("Good bye!");
    }

    private void checkForUpdates() {
        new Thread(() -> {
            try {
                URL url = new URL(REPO_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log("Failed to fetch update information.");
                    return;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                String latestVersion = parseLatestVersion(response.toString());
                if (latestVersion == null) {
                    Log("Failed to parse update information.");
                    return;
                }

                String currentVersion = getDescription().getVersion();
                if (!latestVersion.equals(currentVersion)) {
                    Log("A new version is available: " + latestVersion + ".");
                    Log("Update at https://github.com/JustUnK/ReloadPlugin/releases");
                } else {
                    Log("You are using the latest version: " + currentVersion);
                }
            } catch (Exception e) {
                Log("Error checking for updates: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private String parseLatestVersion(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("tag_name");
        } catch (Exception e) {
            Log("Error parsing update information: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void Log(String message) {
        getLogger().info("[ReloadPlugin] " + message);
    }
}