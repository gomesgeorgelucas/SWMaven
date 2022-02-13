package org.george.domains;

import jakarta.validation.constraints.*;
import lombok.Value;

import org.george.enums.RaceEnum;

import java.util.Comparator;

@Value
public class RebelDomain {
    @NotNull
    @NotEmpty
    @NotBlank
    String name;
    @Min(value = 15)
    @Positive
    int age;
    @NotNull
    RaceEnum race;

    public static class RebelNameSorter implements Comparator<RebelDomain> {

        @Override
        public int compare(RebelDomain o1, RebelDomain o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public static class RebelAgeSorter implements Comparator<RebelDomain> {

        @Override
        public int compare(RebelDomain o1, RebelDomain o2) {
            return Integer.compare(o1.getAge(),o2.getAge());
        }
    }

    public static class RebelRaceSorter implements Comparator<RebelDomain> {

        @Override
        public int compare(RebelDomain o1, RebelDomain o2) {
            return Integer.compare(o1.getRace().ordinal(), o2.getRace().ordinal());
        }
    }
}
