package org.george.enums;

public enum RaceEnum {
    HUMAN("Human"),
    GREE("Gree"),
    RAKATA("Rakata");

    private String description;

    RaceEnum (String description) {this.description = description;}
    public String getDescription() {return this.description;}
}
