package org.george.domains;

import lombok.*;

import org.george.enums.RaceEnum;

@Value
public class RebelDomain {
    String name;
    int age;
    RaceEnum race;
}
