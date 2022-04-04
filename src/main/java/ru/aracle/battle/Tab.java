package ru.aracle.battle;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.aracle.battle.configurations.Settings;
import ru.aracle.battle.operations.Components;
import ru.aracle.battle.operations.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Tab implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            String leader = Configuration.string("Permissions.leader");
            if (sender.hasPermission(leader)) {
                if (args.length > 0) {
                    if (args[0].equals("start")) {
                        start((Player) sender);
                    }
                    if (args[0].equals("pause")) {
                        stop();
                    }
                    if (args[0].equals("reset")) {
                        sender.sendActionBar(Components.LegacyComponent(Configuration.string("Messages.reset")));
                        reset();
                    }
                    if (args[0].equals("continue")) {
                        go();
                    }
                    if (args[0].equals("reload")) {
                        reload();
                        sender.sendActionBar(Components.LegacyComponent(Configuration.string("Messages.reload")));
                    }
                    if (args[0].equals("extra")) {
                        if (args.length > 1) {
                            int minute = Integer.parseInt(args[1]);
                            if (Integer.parseInt(args[1]) > 60) {
                                minute = 59;
                            }
                            Settings.get().set("Settings.Extra-minutes", minute);
                            Settings.save();
                            reload();
                            int h = Configuration.integer("Optional.remaining-running-hours");
                            int m = Configuration.integer("Optional.remaining-running-minutes");
                            if (m+minute < 60 ) { m = m+minute; Settings.get().set("Optional.remaining-running-minutes", m); }
                            else if (m+minute >= 60 ) { Settings.get().set("Optional.remaining-running-hours", h+1); Settings.get().set("Optional.remaining-running-minutes", m+minute-60); }
                            Settings.save();
                            reload();
                            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                String title = Configuration.string("Messages.extra-time.title");
                                String subtitle = Configuration.string("Messages.extra-time.subtitle").replace("%time%", format(0, minute, 0));
                                final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(subtitle));
                                online.showTitle(informing);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1){
            tabs.add("start");
            tabs.add("pause");
            tabs.add("reset");
            tabs.add("reload");
            tabs.add("extra");
            tabs.add("continue");
            return tabs;
        }

        if (args.length == 2){
            if (args[0].equals("extra")) {
                tabs.add("5");
                tabs.add("10");
                tabs.add("15");
                tabs.add("30");
                tabs.add("60");
            }
            return tabs;
        }
        return null;
    }

    public static void reload() {
        Settings.reload();
    }

    public static void go() {
        if (Configuration.string("Optional.Battle-status").equals("stopped")) {
            if (Configuration.integer("Optional.remaining-preparing-minutes") == 0
                    && Configuration.integer("Optional.remaining-preparing-seconds") == 0) {
                Settings.get().set("Optional.Battle-status", "running");
                Settings.save();
                reload();
                runningTimer();
            } else {
                Settings.get().set("Optional.Battle-status", "preparing");
                Settings.save();
                reload();
                prepareTimer();
            }
        }

    }

    public static void reset() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.cancelTasks(Battle.instance());
        Settings.get().set("Optional.Battle-status", "setup");
        Settings.save();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.setGameMode(GameMode.CREATIVE);
        }
        reload();
    }

    public static void end() {
        String spectator = Configuration.string("Settings.set-spectator-mode");
        if (spectator.equals("true")) {
            String leader = Configuration.string("Permissions.leader");
            String judge = Configuration.string("Permissions.judge");
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (!online.hasPermission(leader) && !online.hasPermission(judge)) {
                    online.setGameMode(GameMode.SPECTATOR);
                }
            }
        }
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            String title = Configuration.string("Messages.battle-end.title");
            String subtitle = Configuration.string("Messages.battle-end.subtitle");
            final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(subtitle));
            String message = Configuration.string("Messages.battle-end.actionbar");
            online.sendActionBar(Components.LegacyComponent(message));
            online.showTitle(informing);
        }
        reset();
    }

    public static void stop() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.cancelTasks(Battle.instance());
        Settings.get().set("Optional.Battle-status", "stopped");
        Settings.save();
        reload();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            String title = Configuration.string("Messages.stopped.title");
            String subtitle = Configuration.string("Messages.stopped.subtitle");
            final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(subtitle));
            String message = Configuration.string("Messages.stopped.actionbar");
            online.sendActionBar(Components.LegacyComponent(message));
            online.showTitle(informing);
        }
    }

    public static void start(Player player) {
        String status = Configuration.string("Optional.Battle-status");
        if (!status.equals("running") && !status.equals("preparing")) {
            int hour = Configuration.integer("Settings.Time.hours");
            int second = Configuration.integer("Settings.Time.seconds");
            int minute = Configuration.integer("Settings.Time.minutes");
            if (hour > 24) {
                hour = 24;
            }
            if (minute > 60) {
                minute = 60;
            }
            if (second > 60) {
                second = 60;
            }
            Settings.get().set("Optional.remaining-hours", hour);
            Settings.get().set("Optional.remaining-minutes", minute);
            Settings.get().set("Optional.remaining-seconds", second);
            Settings.save();
            reload();
            if (Configuration.string("Settings.preparing").equals("true")) {
                Settings.get().set("Optional.Battle-status", "preparing");
                Settings.save();
                reload();
                prepare();
            } else {
                Settings.get().set("Optional.Battle-status", "running");
                Settings.save();
                reload();
                running();
            }
        } else {
            String message = Configuration.string("Messages.already-started");
            player.sendActionBar(Components.LegacyComponent(message));
        }
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.setGameMode(GameMode.CREATIVE);
        }
    }

    public static void prepare() {
        int minute = Configuration.integer("Settings.preparing-time-minutes");
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            String title = Configuration.string("Messages.preparing.title");
            String subtitle = Configuration.string("Messages.preparing.subtitle");
            final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(subtitle));
            String message = Configuration.string("Messages.preparing.actionbar");
            message = message.replace("%time%", format(0, minute, 0));
            online.sendActionBar(Components.LegacyComponent(message));
            online.showTitle(informing);
        }
        Settings.get().set("Optional.remaining-preparing-minutes", minute);
        Settings.get().set("Optional.remaining-preparing-seconds", 0);
        Settings.save();
        reload();
        prepareTimer();
    }

    public static void prepareTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Battle.instance(), () -> {
            if (Configuration.string("Optional.Battle-status").equals("preparing")) {
                int m = Configuration.integer("Optional.remaining-preparing-minutes");
                int s = Configuration.integer("Optional.remaining-preparing-seconds");
                if (m != 0) {
                    m = m - 1;
                    s = 60;
                }
                if (s > 0) {
                    s--;
                }
                Settings.get().set("Optional.remaining-preparing-minutes", m);
                Settings.get().set("Optional.remaining-preparing-seconds", s);
                Settings.save();
                reload();
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    String message = Configuration.string("Messages.preparing.actionbar");
                    message = message.replace("%time%", format(0, m, s));
                    online.sendActionBar(Components.LegacyComponent(message));
                }
                if (Configuration.integer("Optional.remaining-preparing-minutes") == 0
                        && Configuration.integer("Optional.remaining-preparing-seconds") == 0) {
                    scheduler.cancelTasks(Battle.instance());
                    Settings.get().set("Optional.Battle-status", "running");
                    Settings.save();
                    reload();
                    running();
                }
            } else {
                scheduler.cancelTasks(Battle.instance());
                Settings.get().set("Optional.Battle-status", "running");
                Settings.save();
                reload();
                running();
            }
        }, 0L, 20L);
    }


    public static void running() {
        String theme = Configuration.string("Settings.Theme");
        int hour = Configuration.integer("Settings.Time.hours");
        int minute = Configuration.integer("Settings.Time.minutes");
        int second = Configuration.integer("Settings.Time.seconds");
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            String title = Configuration.string("Messages.running.title").replace("%theme%", theme);
            String subtitle = Configuration.string("Messages.running.subtitle").replace("%theme%", theme);
            final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(subtitle));
            String message = Configuration.string("Messages.running.actionbar");
            message = message.replace("%time%", format(hour, minute, second));
            online.sendActionBar(Components.LegacyComponent(message));
            online.showTitle(informing);
        }
        Settings.get().set("Optional.remaining-running-hours", hour);
        Settings.get().set("Optional.remaining-running-minutes", minute);
        Settings.get().set("Optional.remaining-running-seconds", second);
        Settings.save();
        reload();
        runningTimer();
    }
    public static void runningTimer() {
        String theme = Configuration.string("Settings.Theme");
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Battle.instance(), () -> {
            if (Configuration.string("Optional.Battle-status").equals("running")) {
                int h = Configuration.integer("Optional.remaining-running-hours");
                int m = Configuration.integer("Optional.remaining-running-minutes");
                int s = Configuration.integer("Optional.remaining-running-seconds");
                if (s == 0) {
                    if (m != 0) {
                        m = m - 1;
                        s = 60;
                    }
                }
                if (m == 0) {
                    if (h != 0) {
                        h = h - 1;
                        m = 59;
                    }
                }
                if (s > 0) {
                    s--;
                }
                Settings.get().set("Optional.remaining-running-hours", h);
                Settings.get().set("Optional.remaining-running-minutes", m);
                Settings.get().set("Optional.remaining-running-seconds", s);
                Settings.save();
                reload();
                if (Configuration.integer("Optional.remaining-running-hours") == 0
                        && Configuration.integer("Optional.remaining-running-minutes") == 0
                        && Configuration.integer("Optional.remaining-running-seconds") == 0) {
                    end();
                    return;
                }
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    String message = Configuration.string("Messages.running.actionbar");
                    message = message.replace("%time%", format(h, m, s)).replace("%theme%", theme);
                    online.sendActionBar(Components.LegacyComponent(message));
                }
                if (h == 1 && m == 59 && s == 59) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        String title = Configuration.string("Messages.timer.one-hour-remaining");
                        final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(""));
                        online.showTitle(informing);
                    }
                }
                if (h == 0 && m == 29 && s == 59) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        String title = Configuration.string("Messages.timer.half-hour-remaining");
                        final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(""));
                        online.showTitle(informing);
                    }
                }
                if (h == 0 && m == 9 && s == 59) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        String title = Configuration.string("Messages.timer.ten-minutes-remaining");
                        final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(""));
                        online.showTitle(informing);
                    }
                }
                if (h == 0 && m == 4 && s == 59) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        String title = Configuration.string("Messages.timer.five-minutes-remaining");
                        final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(""));
                        online.showTitle(informing);
                    }
                }
                if (h == 0 && m == 0 && s == 59) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        String title = Configuration.string("Messages.timer.one-minute-remaining");
                        final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(""));
                        online.showTitle(informing);
                    }
                }
                if (h == 0 && m == 0 && s <= 10) {
                    for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                        String title = Configuration.string("Messages.timer.ten seconds-countdown").replace("%seconds%", String.valueOf(s));
                        final Title informing = Title.title(Components.LegacyComponent(title), Components.LegacyComponent(""));
                        online.showTitle(informing);
                    }
                }
            }else {
                end();
            }
        }, 0L, 20L);
    }

    public static String format(int hour, int minute, int second) {
        String time = "%hours% ч. %minutes% мин. %seconds% сек.";
        if (hour == 0) {
            time = time.replace("%hours% ч. ", "");
        }
        if (minute == 0) {
            time = time.replace("%minutes% мин. ", "");
        }
        if (second == 0) {
            time = time.replace("%seconds% сек.", "");
        }

        time = time.replace("%hours%", String.valueOf(hour))
                .replace("%minutes%", String.valueOf(minute))
                .replace("%seconds%", String.valueOf(second));

        return time;
    }
}
