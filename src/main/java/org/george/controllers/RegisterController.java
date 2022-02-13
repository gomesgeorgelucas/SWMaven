package org.george.controllers;

import com.google.gson.Gson;
import lombok.Cleanup;
import org.apache.commons.io.FileUtils;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RegisterController {
    CentralIntelligenceController cic = new CentralIntelligenceController();

    public static final String REBELS_FILE_PATH = Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
            getResource("./rebels.txt")).getPath();
    public static final String SUSPECTS_FILE_PATH = Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
            getResource("./suspects.txt")).getPath();

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

    public List<List<RebelDomain>> register(List<RebelDomain> candidatesList) {
        List<RebelDomain> rebelsList = new ArrayList<>();
        List<RebelDomain> suspectsList = new ArrayList<>();

        for (RebelDomain candidate : candidatesList) {
            if (cic.verify(candidate)) {
                rebelsList.add(candidate);
            } else {
                suspectsList.add(candidate);
            }
        }


        //rebelsList.add(new RebelDomain("B", 24, RaceEnum.HUMAN.getDescription()));
        //rebelsList.add(new RebelDomain("D", 12, RaceEnum.RAKATA.getDescription()));
        //rebelsList.add(new RebelDomain("C", 67, RaceEnum.GREE.getDescription()));
        //rebelsList.add(new RebelDomain("G", 28, RaceEnum.HUMAN.getDescription()));
        //rebelsList.add(new RebelDomain("E", 62, RaceEnum.RAKATA.getDescription()));
        //rebelsList.add(new RebelDomain("I", 15, RaceEnum.GREE.getDescription()));

        //new CentralIntelligenceController().save(suspectsList, false);

        List<List<RebelDomain>> groups =  new ArrayList<>();
        groups.add(rebelsList);
        groups.add(suspectsList);

        return groups;
    }

    public List<RebelDomain> readFromFiles(Boolean rebels) {
        File file = null;

        List<RebelDomain> listToPrint = new ArrayList<>();

        if (rebels) {
            file = FileUtils.getFile(REBELS_FILE_PATH);
        } else {
            file = FileUtils.getFile(SUSPECTS_FILE_PATH);
        }

        try {
            File tempDir = FileUtils.getTempDirectory();
            FileUtils.copyFileToDirectory(file, tempDir);
            File newTempFile = FileUtils.getFile(tempDir, file.getName());
            @Cleanup Scanner scanner = new Scanner(newTempFile);
            while (scanner.hasNextLine()) {
                RebelDomain temp = new Gson().fromJson(scanner.nextLine(), RebelDomain.class);
                if (temp != null) {
                    listToPrint.add(temp);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return listToPrint;
    }

    @Deprecated
    public void abort() throws IOException {
        Files.deleteIfExists(Path.of(".\\rebels.txt"));
        Files.deleteIfExists(Path.of(".\\suspects.txt"));
    }
}
