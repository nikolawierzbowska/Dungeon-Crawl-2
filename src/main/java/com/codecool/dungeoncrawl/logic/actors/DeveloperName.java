package com.codecool.dungeoncrawl.logic.actors;

public enum DeveloperName {
    LUKASZ("łukasz"),
    PIOTR("piotr"),
    NIKOLA("nikola");

    private final String name;

    DeveloperName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean isDeveloperName(String name) {
        for (DeveloperName devName : DeveloperName.values()) {
            if (devName.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
