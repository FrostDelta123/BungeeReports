package ru.frostdelta.bungeereports.action;

import java.util.HashMap;

public enum Action {
    SCREENSHOT("Screenshot"), SCREENSHOTS("Screenshots"), AUTHORIZATION("Authorization"), UNKNOWN("Unknown action"), PROCESS("Dump"), DUMPS("GetDumps"), CLASSES("CLASSES");

    private static final HashMap<String, Action> actions = new HashMap<>();

    static {
        for (Action ac : Action.values()) {
            actions.put(ac.action, ac);
        }
    }

    private final String action;

    private Action(String action) {
        this.action = action;
    }

    public String getActionName() {
        return action;
    }

    public static Action getAction(String name) {
        return actions.getOrDefault(name, Action.UNKNOWN);
    }

    public static boolean contains(String name) {
        return actions.containsKey(name);
    }
}