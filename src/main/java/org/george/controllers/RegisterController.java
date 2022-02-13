package org.george.controllers;

import lombok.Cleanup;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RegisterController {
    CentralIntelligenceController cic = new CentralIntelligenceController();

    @Deprecated
    public void register(RebelDomain rebel) throws IOException {
        if (cic.verify(rebel)) {
            @Cleanup PrintWriter writer = new PrintWriter(new FileWriter(".\\rebels.txt", true));
            writer.println(Base64.getEncoder().encodeToString(rebel.toString().getBytes()));
        } else {
            System.out.println("DENIED - Nice try!");
            @Cleanup PrintWriter writer = new PrintWriter(new FileWriter(".\\suspects.txt", true));
            writer.println(Base64.getEncoder().encodeToString(rebel.toString().getBytes()));
        }
    }

    public List<RebelDomain> register(List<RebelDomain> candidatesList) {
        List<RebelDomain> rebelsList = new ArrayList<>();
        List<RebelDomain> suspectsList = new ArrayList<>();

        for (RebelDomain candidate : candidatesList) {
            if (cic.verify(candidate)) {
                rebelsList.add(candidate);
            } else {
                suspectsList.add(candidate);
            }
        }

        new CentralIntelligenceController().save(suspectsList, false);

        rebelsList.add(new RebelDomain("B", 24, RaceEnum.HUMAN.getDescription()));
        rebelsList.add(new RebelDomain("D", 12, RaceEnum.RAKATA.getDescription()));
        rebelsList.add(new RebelDomain("C", 67, RaceEnum.GREE.getDescription()));
        rebelsList.add(new RebelDomain("G", 28, RaceEnum.HUMAN.getDescription()));
        rebelsList.add(new RebelDomain("E", 62, RaceEnum.RAKATA.getDescription()));
        rebelsList.add(new RebelDomain("I", 15, RaceEnum.GREE.getDescription()));

        return rebelsList;
    }

    @Deprecated
    public void abort() throws IOException {
        Files.deleteIfExists(Path.of(".\\rebels.txt"));
        Files.deleteIfExists(Path.of(".\\suspects.txt"));
    }
}
