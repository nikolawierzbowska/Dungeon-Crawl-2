package com.codecool.dungeoncrawl.logic.actors;

import javafx.event.Event;
import javafx.event.EventType;

public class MonsterMovementEvent extends Event {
    public static final EventType<MonsterMovementEvent> MONSTER_MOVEMENT_EVENT
            = new EventType<>(ANY, "MONSTER_MOVEMENT_EVENT");

    public MonsterMovementEvent() {
        super(MONSTER_MOVEMENT_EVENT);
    }

}
