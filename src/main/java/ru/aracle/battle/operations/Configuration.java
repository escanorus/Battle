package ru.aracle.battle.operations;

import ru.aracle.battle.configurations.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Configuration {
    public static String string(String path) {
        return Objects.requireNonNullElse(Settings.get().getString(path), "none");
    }
    public static List<String> strings(String path) {
        return Objects.requireNonNullElse(Settings.get().getStringList(path), new ArrayList<>());
    }
    public static Integer integer(String path) {
        return Objects.requireNonNullElse(Settings.get().getInt(path), 0);
    }
}
