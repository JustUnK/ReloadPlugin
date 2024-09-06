package org.justunk.reloadPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.justunk.reloadPlugin.events.TabCompletions;
import org.justunk.reloadPlugin.commands.*;

import java.util.Objects;

public final class ReloadPlugin extends JavaPlugin {



    @Override
    public void onEnable() {


        Objects.requireNonNull(getCommand("reloadplugin")).setExecutor(new ReloadPlugin());

        Objects.requireNonNull(getCommand("reloadplugin")).setTabCompleter(new TabCompletions());
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
