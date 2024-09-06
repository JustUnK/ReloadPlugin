package org.justunk.reloadPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class ReloadPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        Log("Loaded successfully");


    }

    @Override
    public void onDisable() {

        Log("Good bye!");

    }

    public void Log(String message) {
        getLogger().info("[ReloadPlugin] " + message);
    }

}
