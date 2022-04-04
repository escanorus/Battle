package ru.aracle.battle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.aracle.battle.configurations.Settings;
import ru.aracle.battle.operations.Listeners;

import java.util.Objects;

public final class Battle extends JavaPlugin {

    public static Battle instance;

    public static Battle instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Settings.setup();
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
        Objects.requireNonNull(getCommand("battle")).setExecutor(new Tab());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
