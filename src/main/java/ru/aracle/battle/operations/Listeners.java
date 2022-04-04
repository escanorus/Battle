package ru.aracle.battle.operations;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class Listeners implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        String status = Configuration.string("Optional.Battle-status");
        if (status.equals("running") || status.equals("preparing")) {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void build(BlockPlaceEvent event) {
        event.setCancelled(setup(event.getPlayer()));
        if (setup(event.getPlayer())) {
            String message = Configuration.string("Messages.battle-didnt-started");
            event.getPlayer().sendActionBar(Components.LegacyComponent(message));
        }

    }

    @EventHandler
    public void build(BlockBreakEvent event) {
        event.setCancelled(setup(event.getPlayer()));
        if (setup(event.getPlayer())) {
            String message = Configuration.string("Messages.battle-didnt-started");
            event.getPlayer().sendActionBar(Components.LegacyComponent(message));
        }
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent event) {
        List<String> whitelisted = Configuration.strings("Whitelisted-commands-and-contents");
        for (String command :whitelisted) {
            if (!event.getMessage().contains(command)) {
                event.setCancelled(setup(event.getPlayer()));
                if (setup(event.getPlayer())) {
                    String message = Configuration.string("Messages.battle-didnt-started");
                    event.getPlayer().sendActionBar(Components.LegacyComponent(message));
                }
            }
        }
        if (event.getMessage().contains("claim") || event.getMessage().contains("auto")) {
            if (setup(event.getPlayer())) {
                event.setCancelled(!prepare());
                if (event.isCancelled()) {
                    if (!event.getPlayer().hasPermission(Configuration.string("Permissions.leader")) ||
                            !event.getPlayer().hasPermission(Configuration.string("Permissions.judge"))) {
                        String message = Configuration.string("Messages.prepare-didnt-started");
                        event.getPlayer().sendActionBar(Components.LegacyComponent(message));
                    }
                }
            }
        }
    }

    public static boolean prepare() {
        boolean result = false;
        String status = Configuration.string("Optional.Battle-status");
        String preparing = Configuration.string("Settings.preparing");
        if (status.equals("preparing")) {
            if (preparing.equals("true")) {
                result = true;
            }
        }
        return result;
    }

    public static boolean setup(Player player) {
        boolean result = false;
        String status = Configuration.string("Optional.Battle-status");
        String leader = Configuration.string("Permissions.leader");
        String judge = Configuration.string("Permissions.judge");
        if (!status.equals("running")) {
            if (!player.hasPermission(leader) || !player.hasPermission(judge)) {
                result = true;
            }
        }
        return result;
    }
}
