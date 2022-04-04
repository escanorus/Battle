package ru.aracle.battle.configurations;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.aracle.battle.Battle;
import ru.aracle.battle.operations.Logger;

import java.io.File;
import java.io.IOException;

public class Settings {
    static File file;
    static FileConfiguration settings;

    public static void setup() {
        file = new File(Battle.instance().getDataFolder(), "settings.yml");
        settings = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                settings.set("Settings.Theme","Пряничный домик");
                settings.set("Settings.Time.hours",0);
                settings.set("Settings.Time.minutes",60);
                settings.set("Settings.Time.seconds",60);
                settings.set("Settings.preparing","true");
                settings.set("Settings.preparing-time-minutes", 1);
                settings.set("Settings.set-spectator-mode","true");
                settings.set("Settings.Extra-minutes",0);
                settings.set("Permissions.leader","buildbattle.leader");
                settings.set("Permissions.judge","buildbattle.judge");
                settings.set("Permissions.participant","buildbattle.participant");
                settings.set("Messages.battle-didnt-started","Подождите начала битвы.");
                settings.set("Messages.prepare-didnt-started","Подождите начала подготовки к битве.");
                settings.set("Messages.reload","Конфигурация перезагружена.");
                settings.set("Messages.already-started","Битва уже запущена!");
                settings.set("Messages.preparing.title","Билдбатл скоро начнется!");
                settings.set("Messages.preparing.subtitle","Займите плот: /plot claim");
                settings.set("Messages.preparing.actionbar","Подготовка. До начала: %time%");
                settings.set("Messages.running.title","Okaay let's gooo!");
                settings.set("Messages.running.subtitle","Тема: %theme%");
                settings.set("Messages.running.actionbar","Тема: %theme% Время: %time%");
                settings.set("Messages.stopped.title","Битва приостановлена!");
                settings.set("Messages.stopped.subtitle","Ожидайте продолжения.");
                settings.set("Messages.stopped.actionbar","Ожидайте продолжения.");
                settings.set("Messages.timer.one-hour-remaining","Остался 1 час до конца");
                settings.set("Messages.timer.half-hour-remaining","Осталось 30 минут до конца");
                settings.set("Messages.timer.ten-minutes-remaining","Осталось 10 минут до конца");
                settings.set("Messages.timer.five-minutes-remaining","Осталось 5 минут до конца");
                settings.set("Messages.timer.one-minute-remaining","Оасталась одна минута до конца");
                settings.set("Messages.timer.ten seconds-countdown","Оасталось %seconds% до конца");
                settings.set("Messages.battle-end.title","Стоооооооп!");
                settings.set("Messages.battle-end.subtitle","Битва окончена!");
                settings.set("Messages.extra-time.title","Дополнительное время");
                settings.set("Messages.extra-time.subtitle","+ %time%");
                settings.set("Messages.reset","Битва переустановлена.");
                settings.set("Messages.battle-end.actionbar","Время оценок!");
                settings.set("Whitelisted-commands-and-contents", new String[]{"tp", "speed","msg","r","tell","claim","auto"});
                settings.set("Optional.Battle-status","setup");
                settings.set("Optional.remaining-running-hours",0);
                settings.set("Optional.remaining-running-minutes",0);
                settings.set("Optional.remaining-running-seconds",0);
                settings.set("Optional.remaining-preparing-minutes",0);
                settings.set("Optional.remaining-preparing-seconds",0);
                settings.save(file);
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }

        }
    }

    public static FileConfiguration get() {
        return settings;
    }

    public static void save() {
        try {
            settings.save(file);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    public static void reload(){
        settings = YamlConfiguration.loadConfiguration(file);
    }

}
