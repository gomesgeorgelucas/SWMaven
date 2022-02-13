package org.george.controllers;

import org.apache.commons.io.FileUtils;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.exit;

public class CentralIntelligenceController {

    public static final String REBELS_FILE_PATH = Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
            getResource("./rebels.txt")).getPath();
    public static final String SUSPECTS_FILE_PATH = Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
            getResource("./suspects.txt")).getPath();

    private final String[] knownSiths = {"Ajunta Pall", "Belia Darzu", "The Dark Underlord",
            "Darth Andeddu", "Darth Andru", "Darth Bandon",
            "Darth Glovoc", "Darth Malak", "Darth Nihilus",
            "Darth Revan", "Darth Ruin", "Darth Sion",
            "Darth Traya", "Dathka Graush", "Exar Kun",
            "Freedon Nadd", "Kaan", "Ludo Kressh",
            "Marka Ragnos", "Naga Sadow", "Simus",
            "Tulak Hord", "Ulic Qel-Droma",
            "Darth Bane", "Darth Zannah", "Darth Cognus",
            "Darth Millennial", "Darth Vectivus", "Darth Tenebrous",
            "Darth Plagueis", "Darth Sidious", "Darth Maul",
            "Darth Tyranus", "Darth Vader", "Lady Kynthera"};

    public boolean verify(RebelDomain rebel) {
        File suspects = FileUtils.getFile(SUSPECTS_FILE_PATH);
        File rebels = FileUtils.getFile(REBELS_FILE_PATH);

        if (
                Arrays.asList(knownSiths).contains(rebel.getName())
                || (rebel.getRace() == RaceEnum.HUMAN && rebel.getAge() >= 65)
                || (rebel.getRace() == RaceEnum.HUMAN && rebel.getAge() < 15)
        ) {
            return false;
        } else if (new Random().nextBoolean()) {
            return false;
        }

        return true;

    }

    public boolean isDuplicate(File fileToCheck, RebelDomain rebel) {
        File file = fileToCheck;

        File tempDir = FileUtils.getTempDirectory();
        try {
            FileUtils.copyFileToDirectory(file, tempDir);
            File newTempFile = FileUtils.getFile(tempDir, file.getName());
            String data = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());

            if (data.contains(rebel.toString())) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<RebelDomain> save(List<RebelDomain> candidates, boolean accepted) {

        File file = null;

        if (accepted) {
            file = new File(REBELS_FILE_PATH);
        } else {
            file = new File(SUSPECTS_FILE_PATH);
        }

        Queue<RebelDomain> rebelsQueue = new ArrayDeque<>(candidates);
        List<RebelDomain> unique = new ArrayList<>();

        while (!rebelsQueue.isEmpty()) {
            if (!isDuplicate(file, rebelsQueue.peek())) {
                try {
                    unique.add(rebelsQueue.peek());
                    FileUtils.writeStringToFile(file, Objects.requireNonNull(rebelsQueue.poll()).toString() + "\r\n", StandardCharsets.UTF_8, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                rebelsQueue.poll();
            }
        }

        return unique;
    }

    /**
     * MergeSort Collections
     * @param rebels
     * @return
     */
    public List<RebelDomain> sortByName(List<RebelDomain> rebels) {
        System.out.println(rebels);
        Collections.sort(rebels, new RebelDomain.RebelNameSorter());
        System.out.println(rebels);
        return rebels;
    }

    /**
     * MergeSort Collections
     * @param rebels
     * @return
     */
    public List<RebelDomain> sortByAge(List<RebelDomain> rebels) {
        System.out.println(rebels);
        Collections.sort(rebels, new RebelDomain.RebelAgeSorter());
        System.out.println(rebels);
        return rebels;
    }

    /**
     * MergeSort Collections
     * @param rebels
     * @return
     */
    public List<RebelDomain> sortByRace(List<RebelDomain> rebels) {
        System.out.println(rebels);
        Collections.sort(rebels, new RebelDomain.RebelRaceSorter());
        System.out.println(rebels);
        return rebels;
    }
}
