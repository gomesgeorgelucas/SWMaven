package org.george.domains;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.Comparator;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RebelDomain implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    private final String name;
    @Min(value = 15)
    @Positive
    private final int age;
    @NotNull
    @NotEmpty
    @NotBlank
    private final String race;

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
            return o1.getRace().compareTo(o2.getRace());
        }
    }

    @Override
    public String toString() {
        return "[Name=" + name +
                ", Age=" + age +
                ", Race=" + race +
                "]";
    }
}
