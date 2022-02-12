package org.george.controllers;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.george.domains.RebelDomain;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class RegisterController {
    CentralIntelligenceController cic = new CentralIntelligenceController();

    @SneakyThrows
    public void register(RebelDomain rebel) {
        if (cic.verify(rebel)) {
            @Cleanup PrintWriter writer = new PrintWriter(new FileWriter(".\\rebels.txt", true));
            writer.println(Base64.getEncoder().encodeToString(rebel.toString().getBytes()));
        } else {
            System.out.println("DENIED - Nice try!");
            @Cleanup PrintWriter writer = new PrintWriter(new FileWriter(".\\suspects.txt", true));
            writer.println(Base64.getEncoder().encodeToString(rebel.toString().getBytes()));
        }
    }

    @SneakyThrows
    public void abort() {
        Files.deleteIfExists(Path.of(".\\rebels.txt"));
        Files.deleteIfExists(Path.of(".\\suspects.txt"));
    }
}
