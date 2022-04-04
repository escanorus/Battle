package ru.aracle.battle.operations;

import ru.aracle.battle.Battle;

import java.util.logging.Level;

public class Logger {
        public static void error(String message) { Battle.instance().getLogger().log(Level.SEVERE, message); }
        public static void info(String message) {
            Battle.instance().getLogger().log(Level.INFO, message);
        }
        public static void warning(String message) {
            Battle.instance().getLogger().log(Level.WARNING, message);
        }
}
