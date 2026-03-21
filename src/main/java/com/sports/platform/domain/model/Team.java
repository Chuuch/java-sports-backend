package com.sports.platform.domain.model;

import java.util.UUID;

public class Team {
    private final UUID id;
    private final String name;
    private final String location;

    public Team(UUID id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
}
